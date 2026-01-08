/*
 * main.c
 *
 *  Created on: 27.11.2019
 *      Author: Peter
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "db.h"
#include "analyse.h"

void zurueck();

int eingabe;
int eingabe3;
int eingabe2 = 1;
int eingabe4;
char a[32];

int main(){
	setvbuf(stdout, NULL, _IONBF,0);
	initialize_db();
	printf("Willkommen!\n");
	while(eingabe2){
		printf("Bitte geben Sie die Zahl der Funktion ein, die Sie aufrufen wollen.\n");
		printf("1: compute_bmi\n");
		printf("2: identify_risks\n");
		printf("3: compute_bmip\n");
		printf("4: identify_risk_group\n");
		printf("5: identify_pregnant\n");
		printf("6: identify_missing_examination\n");
		printf("7: search_by_name\n");
		printf("0: Verlassen\n");
		scanf("%d", &eingabe);
		switch(eingabe){
			case 1:
				printf("Bitte Patientennummer eingeben:\n");
				scanf("%d", &eingabe3);
				printf("%f\n", compute_bmi(patienten_db[eingabe3]));
				zurueck();
				break;
			case 2:
				identify_risks();
				zurueck();
				break;
			case 3:
				printf("Bitte Patientennummer eingeben!\n");
				scanf("%d", &eingabe3);
				printf("%f\n", compute_bmip(&patienten_db[eingabe3]));
				zurueck();
				break;
			case 4:
				printf("Bitte geben sie eine Patientennummer ein, ab welcher die Risikopatienten ausgegeben werden sollen!\n");
				scanf("%d", &eingabe4);
				identify_risk_group(&patienten_db[0], eingabe4);
				zurueck();
				break;
			case 5:
				identify_pregnant();
				zurueck();
				break;
			case 6:
				identify_missing_examination();
				zurueck();
				break;
			case 7:
				printf("Geben Sie einen Nachnamen ein!\n");
				scanf("%s", a);
				search_by_name(a);
				zurueck();
				break;
			case 0:
				eingabe2 = 0;
				break;
			default:
				printf("Ungueltige Zahl\n");
				eingabe2 = 0;
				break;
		}
		if(eingabe2 == 0){
			printf("Auf Wiedersehen!\n");
		}
	}


	return 0;
}

void zurueck(){
	eingabe = 0;
	printf("Hauptmenue: 1     Verlassen: 0\n");
	scanf("%d", &eingabe2);
}
