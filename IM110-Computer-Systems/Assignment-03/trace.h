#pragma once

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

int simple_trace(pid_t pid);
int full_trace(pid_t pid, bool rebuild);
static std::string readString(pid_t pid, unsigned long addr);