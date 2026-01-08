/**
 * File: subprocess.cc
 * -------------------
 * Presents the implementation of the subprocess routine.
 */

#include "subprocess.h"
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string>
#include <cerrno>
#include <cstring>

using namespace std;

subprocess_t subprocess(char *argv[], bool supplyChildInput, bool ingestChildOutput) throw(SubprocessException)
{
  subprocess_t process = {0, kNotInUse, kNotInUse};

  int stdinPipe[2] = {kNotInUse, kNotInUse};
  int stdoutPipe[2] = {kNotInUse, kNotInUse};

  // Create pipes if needed
  if (supplyChildInput)
  {
    if (pipe(stdinPipe) == -1)
    {
      throw SubprocessException(string("pipe creation for stdin failed: ") + strerror(errno));
    }
  }

  if (ingestChildOutput)
  {
    if (pipe(stdoutPipe) == -1)
    {
      // Clean up stdinPipe if it was created
      if (supplyChildInput)
      {
        close(stdinPipe[0]);
        close(stdinPipe[1]);
      }
      throw SubprocessException(string("pipe creation for stdout failed: ") + strerror(errno));
    }
  }

  // Fork the process
  pid_t pid = fork();

  if (pid == -1)
  {
    // Fork failed, clean up pipes
    if (supplyChildInput)
    {
      close(stdinPipe[0]);
      close(stdinPipe[1]);
    }
    if (ingestChildOutput)
    {
      close(stdoutPipe[0]);
      close(stdoutPipe[1]);
    }
    throw SubprocessException(string("fork failed: ") + strerror(errno));
  }

  if (pid == 0)
  { // Child process
    // Configure stdin
    if (supplyChildInput)
    {
      // Close the write end of the stdin pipe
      close(stdinPipe[1]);

      // Duplicate the read end to stdin
      if (dup2(stdinPipe[0], STDIN_FILENO) == -1)
      {
        _exit(1); // Use _exit in child process
      }

      // Close the original read end
      close(stdinPipe[0]);
    }

    // Configure stdout
    if (ingestChildOutput)
    {
      // Close the read end of the stdout pipe
      close(stdoutPipe[0]);

      // Duplicate the write end to stdout
      if (dup2(stdoutPipe[1], STDOUT_FILENO) == -1)
      {
        _exit(1);
      }

      // Close the original write end
      close(stdoutPipe[1]);
    }

    // Execute the program
    execvp(argv[0], argv);

    // If execvp fails, exit child process
    _exit(1);
  }

  // Parent process
  process.pid = pid;

  // Configure parent's pipe ends
  if (supplyChildInput)
  {
    // Close the read end in the parent
    close(stdinPipe[0]);
    // Save the write end in the subprocess_t struct
    process.supplyfd = stdinPipe[1];
  }

  if (ingestChildOutput)
  {
    // Close the write end in the parent
    close(stdoutPipe[1]);
    // Save the read end in the subprocess_t struct
    process.ingestfd = stdoutPipe[0];
  }

  return process;
}