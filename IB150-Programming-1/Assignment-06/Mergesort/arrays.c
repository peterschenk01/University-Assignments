/*
 * arrays.c
 *
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "arrays.h"


int* generate_random_array(int size) {
	int* a = NULL;

	/* initialize random number generator to current time to generate new
	 * sequences in each call.
	 */
	srand((unsigned) time(NULL));
	a = calloc(size, sizeof(int));
	if (a == NULL) {
				perror("Cannot generate random array");
				exit(1);
	}
	for (int i = 0; i < size; i++) {
		a[i] = 1 + rand() % 1000;
	}
	return a;
}

void print_array(const char* message, int* array, int size) {
	if (message != NULL) {
		printf("%s\n", message);
	}
	if (array == NULL) return;
	for (int i = 0; i < size; i++) {
		printf("%d ", array[i]);
	}
	printf("\n");
}


