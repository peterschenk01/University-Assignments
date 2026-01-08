#include <cassert>
#include <ctime>
#include <cctype>
#include <cstdio>
#include <iostream>
#include <cstdlib>
#include <vector>
#include <sys/wait.h>
#include <unistd.h>
#include <sched.h>
#include "subprocess.h"

using namespace std;

struct worker {
  worker() {}
  worker(char *argv[]) : sp(subprocess(argv, true, false)), available(false) {}
  subprocess_t sp;
  bool available;
};

static const size_t kNumCPUs = sysconf(_SC_NPROCESSORS_ONLN);

static vector<worker> workers(kNumCPUs);
static size_t numWorkersAvailable = 0;

static void markWorkersAsAvailable(int sig) {
  while (true) {
    int status;
    pid_t pid = waitpid(-1, &status, WNOHANG | WUNTRACED);
    if (pid <= 0) break;
    if (WIFSTOPPED(status)) {
      for (auto& worker : workers) {
        if (worker.sp.pid == pid) {
          if (!worker.available) {
            worker.available = true;
            numWorkersAvailable++;
          }
        }
      }
    }
  }
}

static const char *kWorkerArguments[] = {"./factor.py", "--self-halting", NULL};
static void spawnAllWorkers() {
  cout << "There are this many CPUs: " << kNumCPUs << ", numbered 0 through " << kNumCPUs - 1 << "." << endl;

  for (size_t i = 0; i < kNumCPUs; i++) {
    cpu_set_t cpu_set;
    CPU_ZERO(&cpu_set);
    CPU_SET(i, &cpu_set);

    workers[i] = worker((char**)kWorkerArguments);

    if (sched_setaffinity(workers[i].sp.pid, sizeof(cpu_set_t), &cpu_set) != 0) {
      perror("sched_setaffinity");
      exit(1);
    }

    cout << "Worker " << workers[i].sp.pid << " is set to run on CPU " << i << "." << endl;
  }
}

static subprocess_t getAvailableWorker() {
  while(true) {
    for(auto& worker : workers) {
      if(worker.available) {
        worker.available = false;
        numWorkersAvailable--;
        return worker.sp;
      }
    }

    sleep(1);
  }
}

static void broadcastNumbersToWorkers() {
  while (true) {
    string line;
    getline(cin, line);
    if (cin.fail()) break;
    size_t endpos;
    stoll(line, &endpos);
    if (endpos != line.size()) break;
    
    subprocess_t sp = getAvailableWorker();
    dprintf(sp.ingestfd, "%s\n", line.c_str());
    kill(sp.pid, SIGCONT);
  }
}

static void waitForAllWorkers() {
  while(numWorkersAvailable != kNumCPUs) {
    sleep(1);
  }
}

static void closeAllWorkers() {
  for (auto& worker : workers) {
    close(worker.sp.ingestfd);
    kill(worker.sp.pid, SIGCONT);
  }

  for (auto& worker : workers) {
    int status;
    waitpid(worker.sp.pid, &status, 0);
  }
}

int main(int argc, char *argv[]) {
  signal(SIGCHLD, markWorkersAsAvailable);
  spawnAllWorkers();
  broadcastNumbersToWorkers();
  waitForAllWorkers();
  closeAllWorkers();
  return 0;
}
