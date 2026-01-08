/*
 * main.c
 */


#include <stdio.h>
#include <stdlib.h>
#include "arrays.h"



void simple_mergesort(int* array, int from, int to) {

    if (from >= to) {
        return;
    }
    int m = (to + from) / 2;
    simple_mergesort(array, from, m);
    simple_mergesort(array, m + 1, to);

    int *temp = malloc((to-from)*sizeof(int));
    int i = from, j = m + 1, k = 0;

    while (i <= m && j <= to) {
        if (array[i] <= array[j])
            temp[k++] = array[i++];
        else
            temp[k++] = array[j++];
    }
    while (i <= m)
        temp[k++] = array[i++];

    while (j <= to)
        temp[k++] = array[j++];

    k--;
    while (k >= 0) {
        array[from + k] = temp[k];
        k--;
    }
    free(temp);
}

void generate_sort_and_print(const int size) {
	int* a = generate_random_array(size);
	print_array("Unsortiert:", a, size);
	simple_mergesort(a, 0, size -1);
	print_array("Sortiert:", a, size);
	free(a);
}

int main(int argc, char** argv) {
	setvbuf(stdout, NULL, _IONBF,0);
	generate_sort_and_print(10);
	generate_sort_and_print(20);
	generate_sort_and_print(7);
	generate_sort_and_print(2);
	generate_sort_and_print(1);
	return 0;
}
