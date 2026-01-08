#include <stdio.h>
#include <assert.h>

#include "inode.h"
#include "diskimg.h"

#define INODES_PER_BLOCK 16
#define BLOCKS_PER_INDIR_BLOCK 256
#define NUM_SINGLE_INDIR_BLOCKS 7
#define TOTAL_INDIR_BLOCKS (NUM_SINGLE_INDIR_BLOCKS * BLOCKS_PER_INDIR_BLOCK)

int inode_iget(struct unixfilesystem *fs, int inumber, struct inode *inp) {
  int block_num = INODE_START_SECTOR + (inumber - 1) / INODES_PER_BLOCK;
  struct inode inodes[INODES_PER_BLOCK];

  if(diskimg_readsector(fs->dfd, block_num, inodes) < 0) return -1;

  *inp = inodes[(inumber - 1) % INODES_PER_BLOCK];

  return 0;
}

int inode_indexlookup(struct unixfilesystem *fs, struct inode *inp, int blockNum) {
  // Small file
  if((inp->i_mode & ILARG) == 0) {
    return inp->i_addr[blockNum];
  }

  // Large file
  uint16_t buf[BLOCKS_PER_INDIR_BLOCK];

  int indir_block_num = blockNum / BLOCKS_PER_INDIR_BLOCK;
  if(blockNum < TOTAL_INDIR_BLOCKS) {
    // Singly indirect addressing
    if(diskimg_readsector(fs->dfd, inp->i_addr[indir_block_num], buf) < 0) return -1;
  } else {
    // Doubly indirect addressing
    if(diskimg_readsector(fs->dfd, inp->i_addr[7], buf) < 0) return -1;
    if(diskimg_readsector(fs->dfd, buf[indir_block_num], buf) < 0) return -1;
  }

  return buf[blockNum % BLOCKS_PER_INDIR_BLOCK];
}

int inode_getsize(struct inode *inp) {
  return ((inp->i_size0 << 16) | inp->i_size1); 
}
