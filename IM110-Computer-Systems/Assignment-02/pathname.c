#include "pathname.h"
#include "directory.h"
#include "inode.h"
#include "diskimg.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>

int pathname_lookup(struct unixfilesystem *fs, const char *pathname) {
  if(strncmp(pathname, "/", 14) == 0) return ROOT_INUMBER;

  // Cut out "/"
  char path_copy[strlen(pathname) + 1];
  strcpy(path_copy, pathname);
  char *rest = path_copy + 1;

  struct direntv6 dirEnt;
  int current_inumber = ROOT_INUMBER;

  char *token;
  while((token = strsep(&rest, "/")) != NULL) {
    if(directory_findname(fs, token, current_inumber, &dirEnt) < 0) return -1;

    current_inumber = dirEnt.d_inumber;
  }

  return current_inumber;
}