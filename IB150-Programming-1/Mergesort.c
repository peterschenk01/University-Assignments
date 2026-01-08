#include <stdio.h>
#include <stdlib.h>

void merge(int* A, int* left, int* right, int nl, int nr) {
    int i = 0, j = 0, k = 0;
    while (i < nl&&j < nr) {
        if (left[i] < right[j]) {
            A[k++] = left[i++];
        }
        else {
            A[k++] = right[j++];
        }
    }
    while (i < nl) {
        A[k++] = left[i++];
    }
    while (j < nr) {
        A[k++] = right[j++];
    }
}

void mergesort(int* A, int n) {
    if (n < 2)
        return;
    int mid = n / 2;
    int* left = (int*)malloc(sizeof(int) * mid);
    int* right = (int*)malloc(sizeof(int) * (n - mid));
    for (int i = 0; i < mid; i++) {
        left[i] = A[i];
    }
    for (int i = mid; i < n; i++) {
        right[i - mid] = A[i];
    }
    mergesort(left, mid);
    mergesort(right, n - mid);
    merge(A, left, right, mid, n - mid);

    free(left);
    free(right);
}



int main(int argc, char** argv) {
setvbuf(stdout, NULL, _IONBF,0);
     int length1 = 0;
     printf ("Bitte Laenge eingeben");
     scanf ("%d", &length1);
     int *array1 = malloc(length1 * sizeof(int));
     printf ("Bitte geben sie die Werte ein");
     for(int i = 0; i < length1; i++){
         scanf ("%d", &array1[i]);
     }

     for(int i = 0; i < length1 - 1 ; i++){
         if(array1[i+1] <= array1[i]){
             free(array1);
         }
     }

     int length2 = 0;
     printf ("Bitte Laenge eingeben");
     scanf ("%d", &length2);
     if(length1 == length2){
         free(array1);
     }
     int *array2 = malloc(length2 * sizeof(int));
     printf ("Bitte geben sie die Werte ein");
     for(int i = 0; i < length2; i++){
         scanf ("%d", &array2[i]);
     }

      for(int i = 0; i < length2 - 1 ; i++){
         if(array2[i+1] <= array2[i]){
             free(array1);
             free(array2);
         }
     }

     printf("Array1:\n");

      for(int i = 0; i < length1; i++){
                 printf("%d", array1[i]);
     }
     printf("\nArray2:\n");
      for(int i = 0; i < length2; i++){
                 printf("%d", array2[i]);
     }

     int length3 = length1 + length2;
     int *array3 = malloc(length3 * sizeof(int));
     for(int i = 0; i < length1; i++){
             array3[i] = array1[i];
         }
     for(int i = 0 ; i < length2; i++){
             array3[length1 + i] = array2[i];
         }

     printf("\nArray3:\n");
     for(int i = 0; i < length3; i++){
    printf("%d", array3[i]);
     }

     mergesort(array3 , length3);

     printf("\nArray3 sortiert:\n");
     for(int i = 0; i < length3; i++){
    printf("%d", array3[i]);
     }
     free(array1);
     free(array2);
     free(array3);
     return 0;
}
