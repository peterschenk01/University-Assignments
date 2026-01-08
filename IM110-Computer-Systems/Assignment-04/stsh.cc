/**
 * File: stsh.cc
 * -------------
 * Defines the entry point of the stsh executable.
 */

#include "stsh-parser/stsh-parse.h"
#include "stsh-parser/stsh-readline.h"
#include "stsh-parser/stsh-parse-exception.h"
#include "stsh-signal.h"
#include "stsh-job-list.h"
#include "stsh-job.h"
#include "stsh-process.h"
#include <assert.h>
#include <cstring>
#include <iostream>
#include <string>
#include <algorithm>
#include <fcntl.h>
#include <unistd.h>  // for fork
#include <signal.h>  // for kill
#include <setjmp.h>
#include <sys/wait.h>
using namespace std;

static STSHJobList joblist; // the one piece of global data we need so signal handlers can access it

static void fg(const pipeline& pipeline);
static void bg(const pipeline& pipeline);
static void slay(const pipeline& pipeline);
static void halt(const pipeline& pipeline);
static void cont(const pipeline& pipeline);

/**
 * Function: handleBuiltin
 * -----------------------
 * Examines the leading command of the provided pipeline to see if
 * it's a shell builtin, and if so, handles and executes it.  handleBuiltin
 * returns true if the command is a builtin, and false otherwise.
 */
static const string kSupportedBuiltins[] = {"quit", "exit", "fg", "bg", "slay", "halt", "cont", "jobs"};
static const size_t kNumSupportedBuiltins = sizeof(kSupportedBuiltins)/sizeof(kSupportedBuiltins[0]);
static bool handleBuiltin(const pipeline& pipeline) {
  const string& command = pipeline.commands[0].command;
  auto iter = find(kSupportedBuiltins, kSupportedBuiltins + kNumSupportedBuiltins, command);
  if (iter == kSupportedBuiltins + kNumSupportedBuiltins) return false;
  size_t index = iter - kSupportedBuiltins;

  switch (index) {
  case 0:
  case 1: exit(0);
  case 2: fg(pipeline); break;
  case 3: bg(pipeline); break;
  case 4: slay(pipeline); break;
  case 5: halt(pipeline); break;
  case 6: cont(pipeline); break;
  case 7: cout << joblist; break;
  default: throw STSHException("Internal Error: Builtin command not supported."); // or not implemented yet
  }

  return true;
}

static void fg(const pipeline& pipeline) {
  try {
    int job_num = std::stoi(pipeline.commands[0].tokens[0]);

    if(!joblist.containsJob(job_num)) {
      throw STSHException("No job with this job number!");
    }

    STSHJob& job = joblist.getJob(job_num);
    pid_t pgid = job.getGroupID();

    job.setState(kForeground);

    if(tcsetpgrp(STDIN_FILENO, pgid) == -1 && errno != ENOTTY) { // Give job terminal control
      throw STSHException("tcsetpgrp error!");
    }
    
    kill(-pgid, SIGCONT);
  } catch (const std::invalid_argument& e) {
    throw STSHException("Argument not a number!");
  }
}

static void bg(const pipeline& pipeline) {
  try {
    int job_num = std::stoi(pipeline.commands[0].tokens[0]);

    if(!joblist.containsJob(job_num)) {
      throw STSHException("No job with this job number!");
    }

    STSHJob& job = joblist.getJob(job_num);
    pid_t pgid = job.getGroupID();

    kill(-pgid, SIGCONT);
  } catch (const std::invalid_argument& e) {
    throw STSHException("Argument not a number!");
  }
}

static void slay(const pipeline& pipeline) {
  int count = 0;
  while(pipeline.commands[0].tokens[count] != nullptr) {
    count++;
  }

  if(count == 0) {
    throw STSHException("Not enough arguments!");
  } else if(count == 1) {
    try {
      int process_num = std::stoi(pipeline.commands[0].tokens[0]);

      if(kill(process_num, SIGINT) == ESRCH) {
        throw STSHException("Process with given id does not exist!");
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else if(count == 2) {
    try {
      int job_num = std::stoi(pipeline.commands[0].tokens[0]);
      int process_index = std::stoi(pipeline.commands[0].tokens[1]);

      if(process_index < 0) {
        throw STSHException("Index must be positive!");
      }

      if(!joblist.containsJob(job_num)) {
        throw STSHException("No job with given job number!");
      }

      STSHJob& job = joblist.getJob(job_num);
      auto& processes = job.getProcesses();
      STSHProcess& process = processes[process_index];

      if(kill(process.getID(), SIGINT) == ESRCH) {
        throw STSHException("Job has no process with given index does not exist!");
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else {
    throw STSHException("Too many arguments!");
  }
}

static void halt(const pipeline& pipeline) {
  int count = 0;
  while(pipeline.commands[0].tokens[count] != nullptr) {
    count++;
  }

  if(count == 0) {
    throw STSHException("Not enough arguments!");
  } else if(count == 1) {
    try {
      int process_num = std::stoi(pipeline.commands[0].tokens[0]);

      STSHJob& job = joblist.getJobWithProcess(process_num);
      STSHProcess& process = job.getProcess(process_num);

      if(process.getState() != kStopped) {
        if(kill(process_num, SIGTSTP) == ESRCH) {
          throw STSHException("Process with given id does not exist!");
        }
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else if(count == 2) {
    try {
      int job_num = std::stoi(pipeline.commands[0].tokens[0]);
      int process_index = std::stoi(pipeline.commands[0].tokens[1]);

      if(process_index < 0) {
        throw STSHException("Index must be positive!");
      }

      if(!joblist.containsJob(job_num)) {
        throw STSHException("No job with given job number!");
      }

      STSHJob& job = joblist.getJob(job_num);
      auto& processes = job.getProcesses();
      STSHProcess& process = processes[process_index];

      if(process.getState() != kStopped) {
        if(kill(process.getID(), SIGTSTP) == ESRCH) {
          throw STSHException("Job has no process with given index does not exist!");
        }
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else {
    throw STSHException("Too many arguments!");
  }
}

static void cont(const pipeline& pipeline) {
  int count = 0;
  while(pipeline.commands[0].tokens[count] != nullptr) {
    count++;
  }

  if(count == 0) {
    throw STSHException("Not enough arguments!");
  } else if(count == 1) {
    try {
      int process_num = std::stoi(pipeline.commands[0].tokens[0]);

      STSHJob& job = joblist.getJobWithProcess(process_num);
      STSHProcess& process = job.getProcess(process_num);

      if(process.getState() != kRunning) {
        if(kill(process_num, SIGCONT) == ESRCH) {
          throw STSHException("Process with given id does not exist!");
        }
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else if(count == 2) {
    try {
      int job_num = std::stoi(pipeline.commands[0].tokens[0]);
      int process_index = std::stoi(pipeline.commands[0].tokens[1]);

      if(process_index < 0) {
        throw STSHException("Index must be positive!");
      }

      if(!joblist.containsJob(job_num)) {
        throw STSHException("No job with given job number!");
      }

      STSHJob& job = joblist.getJob(job_num);
      auto& processes = job.getProcesses();
      STSHProcess& process = processes[process_index];

      if(process.getState() != kRunning) {
        if(kill(process.getID(), SIGCONT) == ESRCH) {
          throw STSHException("Job has no process with given index does not exist!");
        }
      }
    } catch (const std::invalid_argument& e) {
      throw STSHException("Argument not a number!");
    }
  } else {
    throw STSHException("Too many arguments!");
  }
}

static void handleSIGCHLD(int sig) {
    int status;
    pid_t pid;
    
    while((pid = waitpid(-1, &status, WNOHANG | WUNTRACED | WCONTINUED)) > 0) {
      STSHJob& job = joblist.getJobWithProcess(pid);
      STSHProcess& process = job.getProcess(pid);

      if (WIFSTOPPED(status)) {
        process.setState(kStopped);
      } else if (WIFCONTINUED(status)) {
        process.setState(kRunning);
      } else if(WIFEXITED(status) || WIFSIGNALED(status)){
        process.setState(kTerminated);
      }

      // Restore terminal control
      if(job.getState() == kForeground) {
        if (WIFCONTINUED(status)) {
          if(tcsetpgrp(STDIN_FILENO, job.getGroupID()) == -1 && errno != ENOTTY) {
            throw STSHException("tcsetpgrp error!");
          }
        } else {
          if(tcsetpgrp(STDIN_FILENO, getpgrp()) == -1 && errno != ENOTTY) {
            throw STSHException("tcsetpgrp error!");
          }
        }
      }

      joblist.synchronize(job);
    }
}

static void handleSIGINT(int sig) {
  cout << endl;
  if(joblist.hasForegroundJob()) {
    STSHJob& job = joblist.getForegroundJob();
    pid_t pgid = job.getGroupID();

    kill(-pgid, SIGINT);
  }
}

static void handleSIGTSTP(int sig) {
  cout << endl;
  if(joblist.hasForegroundJob()) {
    STSHJob& job = joblist.getForegroundJob();
    pid_t pgid = job.getGroupID();

    kill(-pgid, SIGTSTP);
  }
}

/**
 * Function: installSignalHandlers
 * -------------------------------
 * Installs user-defined signals handlers for four signals
 * (once you've implemented signal handlers for SIGCHLD,
 * SIGINT, and SIGTSTP, you'll add more installSignalHandler calls) and
 * ignores two others.
 */
static void installSignalHandlers() {
  installSignalHandler(SIGCHLD, handleSIGCHLD);
  installSignalHandler(SIGINT, handleSIGINT);
  installSignalHandler(SIGTSTP, handleSIGTSTP);
  installSignalHandler(SIGQUIT, [](int sig) { exit(0); });
  installSignalHandler(SIGTTIN, SIG_IGN);
  installSignalHandler(SIGTTOU, SIG_IGN);
}

/**
 * Function: createJob
 * -------------------
 * Creates a new job on behalf of the provided pipeline.
 */
static void createJob(const pipeline& p) {
  // Block signals
  sigset_t mask, oldmask;
  sigemptyset(&mask);
  sigaddset(&mask, SIGCHLD);
  sigaddset(&mask, SIGINT);
  sigaddset(&mask, SIGTSTP);
  sigprocmask(SIG_BLOCK, &mask, &oldmask);

  pid_t pids[kMaxArguments];
  pid_t pgid;

  // Create num commands - 1 pipes
  int fds[kMaxArguments - 1][2];
  for(size_t i = 0; i < p.commands.size() - 1; i++) {
    pipe(fds[i]);
  }
  
  // Create process for each command
  for(size_t i = 0; i < p.commands.size(); i++) {
    pids[i] = fork();
    pgid = pids[0];
    
    if(pids[i] == 0) {
      // Unblock signals
      sigprocmask(SIG_UNBLOCK, &mask, NULL);

      // Set pgid
      setpgid(pids[i], pgid);

      // Input redirection
      if(i == 0 && !p.input.empty()) {
        int input_fd = open(p.input.c_str(), O_RDONLY);
        dup2(input_fd, STDIN_FILENO);
      }

      // Output redirection
      if(i == p.commands.size() - 1 && !p.output.empty()) {
        int output_fd = open(p.output.c_str(), O_WRONLY | O_CREAT | O_TRUNC, 0644);
        dup2(output_fd, STDOUT_FILENO);
      }

      // If not first command: Redirect stdin
      if(i > 0) {
        dup2(fds[i - 1][0], STDIN_FILENO);
      }

      // If not last command: Redirect stdout
      if(i < p.commands.size() - 1) {
        dup2(fds[i][1], STDOUT_FILENO);
      }

      // Close all pipes
      for(size_t i = 0; i < p.commands.size() - 1; i++) {
        close(fds[i][0]);
        close(fds[i][1]);
      }

      // Convert command into args array
      const char *args[kMaxArguments + 2];
      args[0] = p.commands[i].command;

      size_t j;
      for (j = 0; p.commands[i].tokens[j] != NULL && j < kMaxArguments; j++) {
          args[j + 1] = p.commands[i].tokens[j];
      }

      args[j + 1] = NULL;

      // Execute command
      execvp(args[0], (char *const *)args);
      throw STSHException("execvp error!");
    }
  }
  // Parent

  // Set pgid of children
  for(size_t i = 0; i < p.commands.size(); i++) {
    setpgid(pids[i], pgid);
  }

  // Close all pipes
  for(size_t i = 0; i < p.commands.size() - 1; i++) {
    close(fds[i][0]);
    close(fds[i][1]);
  }

  // Add job and processes to joblist
  STSHJob& job = joblist.addJob(p.background ? kBackground : kForeground);
  for(size_t i = 0; i < p.commands.size(); i++) {
    job.addProcess(STSHProcess(pids[i], p.commands[i]));
  }

  if(p.background) {
    cout << "[" << job.getNum() << "] ";
    for(size_t i = 0; i < p.commands.size(); i++) {
      cout << pids[i] << " ";
    }
    cout << endl;
  }

  // Give children terminal control
  if(!p.background) {
    if(tcsetpgrp(STDIN_FILENO, pgid) == -1 && errno != ENOTTY) {
      throw STSHException("tcsetpgrp error!");
    }
  }

  // Unblock signals
  sigprocmask(SIG_UNBLOCK, &mask, NULL);
}

/**
 * Function: main
 * --------------
 * Defines the entry point for a process running stsh.
 * The main function is little more than a read-eval-print
 * loop (i.e. a repl).
 */
int main(int argc, char *argv[]) {
  pid_t stshpid = getpid();
  installSignalHandlers();
  rlinit(argc, argv);
  while (true) {
    string line;
    if (!readline(line)) break;
    if (line.empty()) continue;
    try {
      pipeline p(line);
      bool builtin = handleBuiltin(p);
      if (!builtin) createJob(p);

      sigset_t oldmask;
      sigprocmask(0, NULL, &oldmask);
      while(joblist.hasForegroundJob()){
        sigsuspend(&oldmask);
      }
    } catch (const STSHException& e) {
      cerr << e.what() << endl;
      if (getpid() != stshpid) exit(0); // if exception is thrown from child process, kill it
    }
  }

  return 0;
}
