#include <unistd.h>
#include <fcntl.h>
#include <iostream>

static void publish(const char *name) {
    printf("Publishing date and time to file named \"%s\".\n", name);
    int outfile = open(name, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    int savestdout = dup(STDOUT_FILENO); // Save stdout fd
    dup2(outfile, STDOUT_FILENO);
    close(outfile);
    if (fork() > 0) { 
        // Parent
        // Restore stdout
        dup2(savestdout, STDOUT_FILENO);
        close(savestdout);
        return;
    }
    // Child
    char date_cmd[] = "date";
    char *argv[] = { date_cmd, NULL };
    execvp(argv[0], argv);
}
 
int main(int argc, char *argv[]) {
    for (size_t i = 1; i < argc; i++) publish(argv[i]);
    return 0;
}