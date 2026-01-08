#include "directory.h"
#include "inode.h"
#include "diskimg.h"
#include "file.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>

int directory_findname(struct unixfilesystem *fs, const char *name, int dirinumber, struct direntv6 *dirEnt) {
  struct inode inode;
  if(inode_iget(fs, dirinumber, &inode) < 0) return -1;

  if((inode.i_mode & IFMT) != IFDIR) return -1;

  int dir_size = inode_getsize(&inode);
  int num_blocks = 1 + (dir_size / (DISKIMG_SECTOR_SIZE + 1));

  struct direntv6 buf[DISKIMG_SECTOR_SIZE / sizeof(struct direntv6)];

  for(int i = 0; i < num_blocks; i++) {
    int bytes_read = file_getblock(fs, dirinumber, i, buf);
    if(bytes_read < 0) return -1;

    int num_entries = bytes_read / sizeof(struct direntv6);
    struct direntv6 *entries = (struct direntv6 *)buf;

    for(int j = 0; j < num_entries; j++) {
      if(strncmp(name, entries[j].d_name, 14) == 0) {
        *dirEnt = entries[j];
        return 0;
      }
    }
  }

  return -1;
}