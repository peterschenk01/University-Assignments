#include <stdio.h>

int ist_haesslich(unsigned int n);

int main(){
	unsigned int zaehler = 0;
	unsigned int zahl = 0;
	while(zaehler < 1000){
		zahl++;
		if(ist_haesslich(zahl)){
			zaehler++;
		}
	}
	printf("1000ste haessliche Zahl = %d\n", zahl);
	return 0;
}

int ist_haesslich(unsigned int n){
	int teilbar = 1;
	while(n > 1){
		if(n % 2 == 0){
			n = n / 2;
			teilbar = 1;
		}
		else if(n % 3 == 0){
			n = n / 3;
			teilbar = 1;
		}
		else if(n % 5 == 0){
			n = n / 5;
			teilbar = 1;
		}
		else{
			n = 1;
			teilbar = 0;
		}
	}

	return teilbar;
}
