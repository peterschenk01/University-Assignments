#include <stdio.h>

#define MAX 10
int zahl;

int find(int x, int f[MAX], int *index){
	for(int i = 0; i < MAX; i++){
		if(x == f[i]){
			index = &i;
			printf("%d\n", *index);
			return 1;
			break;
		}
	}
	return 0;
}

int main() {
	scanf("%d", &zahl);
    int vektor[MAX] = {3, 5, 3, 4, 2, 1, 7, 8, 1, 9};
    int vektor2[MAX] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    int *pointer = NULL;
    find(zahl,vektor,pointer);
    find(zahl,vektor2,pointer);
    return 0;
}
