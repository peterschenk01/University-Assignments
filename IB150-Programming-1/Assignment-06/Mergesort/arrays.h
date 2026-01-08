/*
 * arrays.h
 *
 */

#ifndef ARRAYS_H_
#define ARRAYS_H_


/**
 * Generates an array of size 'size' filled with random integers from 1 .. 1000
 * and returns a pointer to that array. The program is aborted if the array
 * cannot be generated, e.g. because of insufficient memory.
 * The caller is responsible for deallocating the memory when no longer needed.
 */
int* generate_random_array(int size);
/**
 * Prints out the values of array 'array' in order, if NULL, nothing is printed.
 * array is assumed to be of size 'size'
 * The string 'message' is printed before the values are printed, it can be NULL
 * in which case only the array values will be printed.
 */
void print_array(const char* message, int* array, int size);

#endif /* ARRAYS_H_ */
