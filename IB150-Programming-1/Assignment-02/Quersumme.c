#include <stdio.h>

int main(){
	setvbuf(stdout, NULL, _IONBF,0);
	int eingabe;
	int quersumme = 0;
	int a;
	while(1){
		scanf("%d", &eingabe);
		if(eingabe == 0){
			break;
		}
		while(eingabe != 0){
			a = eingabe % 10;
			quersumme = quersumme + a;
			eingabe = eingabe / 10;
		}
		printf("Quersumme: %d\n", quersumme);
		quersumme = 0;
	}
	return 0;
}
