/*
 * flaschennachricht.c
 *
 *  Created on: 12.01.2020
 *      Author: Peter
 */

#include <stdio.h>

int main(){
	FILE *text;
	text = fopen("weihnachten.txt", "rb");
	if(text == NULL){
		printf("Falsche Eingabe");
	}
	int input;
	int output = 0;
	int n = 0;
	int binchar[8];

	while(1){
		input = fgetc(text);
		if(!(input == 49 || input == 48 || input == 32 || input == 10 || input == 13 ||input == EOF)){
			break;
		}
		if(input == 49 || input == 48){
			binchar[n] = input - 48;
			n++;
		}
		if(n == 8){
			n = 128;
			for(int i = 0; i < 8; i++){
				output += binchar[i] * n;
				n /= 2;
			}
			printf("%c", output);
			output = 0;
			n = 0;
		}
		if(input == EOF){
			if(n != 0){
				printf("Es sind noch folgende Zahlen übrig:");
				for(int x = 0; x <= n; x++){
					printf("%d", binchar[x]);
				}
			}
			break;
		}
	}
	return 0;
}
