/**
 * File: trace.cc
 * ----------------
 * Presents the implementation of the trace program, which traces the execution of another
 * program and prints out information about every single system call it makes.  For each system call,
 * trace prints:
 *
 *    + the name of the system call,
 *    + the values of all of its arguments, and
 *    + the system call return value
 */

#include <cassert>
#include <iostream>
#include <map>
#include <unistd.h> // for fork, execvp
#include <string.h> // for memchr, strerror
#include <sys/ptrace.h>
#include <sys/reg.h>
#include <sys/wait.h>
#include "trace-options.h"
#include "trace-error-constants.h"
#include "trace-system-calls.h"
#include "trace-exception.h"
#include "trace.h"
// the following does not exist, put your helper fcts into there if needed.
//#include "fork-utils.h" // this has to be the last #include statement in this file
using namespace std;

static std::map<int, int> registerNumbers = {{0, RDI}, {1, RSI}, {2, RDX}, {3, R10}, {4, R8}, {5, R9}};

int main(int argc, char *argv[]) {
  bool simple = false, rebuild = false;
  int numFlags = processCommandLineFlags(simple, rebuild, argv);
  if (argc - numFlags == 1) {
    cout << "Nothing to trace... exiting." << endl;
    return 0;
  }

  pid_t pid = fork();
  if (pid == 0) {
    ptrace(PTRACE_TRACEME);
    raise(SIGSTOP);
    execvp(argv[numFlags + 1], argv + numFlags + 1);
    return 0;
  }

  if(simple == true) {
    return simple_trace(pid);
  } else {
    return full_trace(pid, rebuild);
  }
}

int simple_trace(pid_t pid) {
  int status;

  waitpid(pid, &status, 0);
  assert(WIFSTOPPED(status));
  ptrace(PTRACE_SETOPTIONS, pid, 0, PTRACE_O_TRACESYSGOOD);

  while(!(WIFEXITED(status) || WIFSIGNALED(status))) {
    while (!(WIFEXITED(status) || WIFSIGNALED(status))) {
      ptrace(PTRACE_SYSCALL, pid, 0, 0);
      waitpid(pid, &status, 0);

      if (WIFSTOPPED(status) && (WSTOPSIG(status) == (SIGTRAP | 0x80))) {
        int num = ptrace(PTRACE_PEEKUSER, pid, ORIG_RAX * sizeof(long));
        cout << "syscall(" << num << ") = " << flush;

        break;
      }
    }

    while (!(WIFEXITED(status) || WIFSIGNALED(status))) {
      ptrace(PTRACE_SYSCALL, pid, 0, 0);
      waitpid(pid, &status, 0);

      if (WIFSTOPPED(status) && (WSTOPSIG(status) == (SIGTRAP | 0x80))) {
        long ret = ptrace(PTRACE_PEEKUSER, pid, RAX * sizeof(long));
        cout << ret << endl;
        break;
      }
    }
  }

  cout << "<no return>" << endl;
	cout << "Program exited normally with status " << status << endl;
  return status;
}

int full_trace(pid_t pid, bool rebuild) {
  int status;

  std::map<int, std::string> systemCallNumbers;
  std::map<std::string, int> systemCallNames;
  std::map<std::string, systemCallSignature> systemCallSignatures;
  compileSystemCallData(systemCallNumbers,
                        systemCallNames,
                        systemCallSignatures,
                        rebuild);

  std::map<int, std::string> errorConstants;
  compileSystemCallErrorStrings(errorConstants);

  waitpid(pid, &status, 0);
  assert(WIFSTOPPED(status));
  ptrace(PTRACE_SETOPTIONS, pid, 0, PTRACE_O_TRACESYSGOOD);

  while(!(WIFEXITED(status) || WIFSIGNALED(status))) {
    int num;

    while (!(WIFEXITED(status) || WIFSIGNALED(status))) {
      ptrace(PTRACE_SYSCALL, pid, 0, 0);
      waitpid(pid, &status, 0);

      if (WIFSTOPPED(status) && (WSTOPSIG(status) == (SIGTRAP | 0x80))) {
        num = ptrace(PTRACE_PEEKUSER, pid, ORIG_RAX * sizeof(long));

        std::string systemCallName = systemCallNumbers[num];
        cout << systemCallName << "(";

        if(systemCallSignatures[systemCallName].size() == 0) cout << "<signature-information-missing>";

        for(unsigned int i = 0; i < systemCallSignatures[systemCallName].size(); i++) {
          scParamType paramType = systemCallSignatures[systemCallName][i];
          
          if(paramType == SYSCALL_INTEGER) {
            int param = ptrace(PTRACE_PEEKUSER, pid, registerNumbers[i] * sizeof(long));
            cout << param;
          } else if(paramType == SYSCALL_POINTER) {
            void* param = reinterpret_cast<void*>(ptrace(PTRACE_PEEKUSER, pid, registerNumbers[i] * sizeof(long)));
            if(param == 0) {
              cout << "NULL";
            } else {
              cout << param;
            }
          } else if(paramType == SYSCALL_STRING) {
            long param = ptrace(PTRACE_PEEKUSER, pid, registerNumbers[i] * sizeof(long));
            cout << "\"" << readString(pid, param) << "\"";
          }

          if(i != systemCallSignatures[systemCallName].size() - 1) cout << ", ";
        }

        cout << ") = " << flush;

        break;
      }
    }

    while (!(WIFEXITED(status) || WIFSIGNALED(status))) {
      ptrace(PTRACE_SYSCALL, pid, 0, 0);
      waitpid(pid, &status, 0);

      if (WIFSTOPPED(status) && (WSTOPSIG(status) == (SIGTRAP | 0x80))) {
        long ret = ptrace(PTRACE_PEEKUSER, pid, RAX * sizeof(long));
        if(ret >= 0){
          if(num == 12 || num == 9) { //brk or mmap
            cout << reinterpret_cast<void*>(ret) << endl;
          } else {
            cout << static_cast<int>(ret) << endl;
          }
        } else {
          cout << -1 << " " << errorConstants[-ret] << " (" << strerror(-ret) << ")" << endl;
        }
        break;
      }
    }
  }
  
  cout << "<no return>" << endl;
	cout << "Program exited normally with status " << status << endl;
  return status;
}

static string readString(pid_t pid, unsigned long addr) { // addr is a char * read from an argument register via PTRACE_PEEKUSER
  string str; // start out empty
  size_t numBytesRead = 0;
  while (true) {
    long ret = ptrace(PTRACE_PEEKDATA, pid, addr + numBytesRead);

    char* bytes = reinterpret_cast<char*>(&ret);

    for (size_t i = 0; i < sizeof(long); ++i) {
      if (bytes[i] == '\0') {
          str.append(reinterpret_cast<char*>(bytes), i);
          return str;
      }
    }

    str.append(reinterpret_cast<char*>(bytes), sizeof(long));
    numBytesRead += sizeof(long);
  }
}