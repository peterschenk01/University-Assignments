#include <stdio.h>

int count[256], i = 0;
char a[100];

void output_char_n(int n);

int main(){
	scanf("%s", a);
	while(a[i] != '\0'){
		count[a[i]] = count[a[i]] + 1;
		i++;
	}
	output_char_n(0);
	return 0;
}

void output_char_n(int n){
	for(int x = 1; x <= i; x++){
		for(n = 0; n <= 256; n++){
			if(count[n] == x){
				printf("%c %d\n", n, count[n]);
			}
		}
	}
}
