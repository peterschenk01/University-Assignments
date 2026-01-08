#include <stdio.h>
#include <time.h>

int ist_schaltjahr1(int jahr);
int ist_schaltjahr2(int jahr);

int main(){
	time_t startzeit;
	time_t endzeit;

	time(&startzeit);
	int anzahl = 0;
	int zahl = 0;
	while(zahl <= 1000000000){
		zahl++;
		if(ist_schaltjahr1(zahl)){
			anzahl++;
		}
	}
	time(&endzeit);
	printf("verstrichene Zeit für Funktion 1: %f\n", difftime(endzeit, startzeit));


	time(&startzeit);
	anzahl = 0;
	zahl = 0;
	while(zahl <= 1000000000){
		zahl++;
		if(ist_schaltjahr2(zahl)){
			anzahl++;
		}
	}
	time(&endzeit);
	printf("verstrichene Zeit für Funktion 2: %f\n", difftime(endzeit, startzeit));


	printf("Anzahl der Schaltjahre: %d\n", anzahl);
	return 0;
}

int ist_schaltjahr1(int jahr){
	if((jahr % 4 == 0) && !((jahr % 100 == 0) && !(jahr % 400 == 0))){
		return 1;
	}
	else{
		return 0;
	}
}

int ist_schaltjahr2(int jahr){
	if(((jahr % 400 == 0) || !(jahr % 100 == 0)) && (jahr % 4 == 0)){
		return 1;
	}
	else{
		return 0;
	}
}
