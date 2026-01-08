#include <stdio.h>
#include <assert.h>

#include "file.h"
#include "inode.h"
#include "diskimg.h"

int file_getblock(struct unixfilesystem *fs, int inumber, int blockNum, void *buf) {
  struct inode inode;
  if(inode_iget(fs, inumber, &inode) < 0) return -1;

  int real_block_num = inode_indexlookup(fs, &inode, blockNum);
  if(real_block_num < 0) return -1;

  if(diskimg_readsector(fs->dfd, real_block_num, buf) < 0) return -1;

  int file_size = inode_getsize(&inode);
  int valid_bytes = file_size % DISKIMG_SECTOR_SIZE;
  if(valid_bytes == 0 && file_size > 0) valid_bytes = DISKIMG_SECTOR_SIZE;
  return valid_bytes;
}