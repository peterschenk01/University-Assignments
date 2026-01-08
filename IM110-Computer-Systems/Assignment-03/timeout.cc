#include <iostream>
#include <unistd.h>
#include <sys/wait.h>

using namespace std;

int main(int argc, char *argv[]) {
    pid_t child1_pid = fork();

    if(child1_pid == 0) {
        // Child 1
        
        execvp(argv[2], argv + 2);

        // If the child gets here, there was an error
        fprintf(stderr, "execvp failed to invoke this: %s.\n", argv[2]);
        return -1;
    }
    
    pid_t child2_pid = fork();

    if(child2_pid == 0) {
        // Child 2

        int timeout = std::atoi(argv[1]);
        sleep(timeout);

        return 0;
    }

    // Parent

    int status;
    pid_t finished_pid = waitpid(-1, &status, 0);

    if(finished_pid == child1_pid) {
        kill(child2_pid, SIGKILL);
        return status;
    } else if(finished_pid == child2_pid) {
        kill(child1_pid, SIGKILL);
        return 124;
    } else {
        return -1;
    }
}