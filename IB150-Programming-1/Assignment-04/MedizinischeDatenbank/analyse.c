/*
 * analyse.c
 *
 *  Created on: 27.11.2019
 *      Author: Peter
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "db.h"
#include "analyse.h"

float compute_bmi(patient p){
	float bmi;
	bmi = p.Gewicht / ((p.Koerpergroesse / 100.00) * (p.Koerpergroesse / 100.00));
	return bmi;
}

void identify_risks(){
	for(int i = 0; i < 100; i++){
		if(compute_bmi(patienten_db[i]) < 20 || compute_bmi(patienten_db[i]) > 25){
			printf("[%d] %s %s\n", i, patienten_db[i].Vorname, patienten_db[i].Nachname);
		}
	}
}

float compute_bmip(patient *pointer){
	float bmi;
	bmi = (*pointer).Gewicht / (((*pointer).Koerpergroesse / 100.00) * ((*pointer).Koerpergroesse / 100.00));
	return bmi;
}

void identify_risk_group(patient *patients, int size){
	for(int i = size; i < 100; i++){
		if(compute_bmip(patients + i) < 20 || compute_bmip(patients + i) > 25){
			printf("[%d] %s %s\n", i, (patients[i]).Vorname, (patients[i]).Nachname);
		}
	}
}

void identify_pregnant(){
	for(int i = 0; i < 100; i++){
		if(patienten_db[i].istSchwanger == 1){
			printf("[%d] %s %s\n", i, patienten_db[i].Vorname, patienten_db[i].Nachname);
		}
	}
}

void identify_missing_examination(){
	for(int i = 0; i < 100; i++){
			if(!strcmp(patienten_db[i].letzte_Lymphknotenuntersuchung, "unbekannt") || !strcmp(patienten_db[i].letzte_Brustuntersuchung, "unbekannt")){
				printf("[%d] %s %s\n", i, patienten_db[i].Vorname, patienten_db[i].Nachname);
			}
		}
}

void search_by_name(char name[]){
	for(int i = 0;i < 100; i++){
		if(!strcmp(patienten_db[i].Nachname, name)){
			printf("Vorname: %s\n", patienten_db[i].Vorname);
			printf("Nachname: %s\n", patienten_db[i].Nachname);
			printf("Alter: %d\n", patienten_db[i].Alter);
			printf("Geschlecht: %c\n", patienten_db[i].Geschlecht);
			printf("Gewicht: %d\n", patienten_db[i].Gewicht);
			printf("Koerpergroesse: %d\n", patienten_db[i].Koerpergroesse);
			if(patienten_db[i].Geschlecht == 'm'){
				printf("Letzte Lymphknotenuntersuchung: %s\n", patienten_db[i].letzte_Lymphknotenuntersuchung);
			}
			if(patienten_db[i].Geschlecht == 'w'){
			printf("Schwanger: %d\n", patienten_db[i].istSchwanger);
			printf("Letzte Brustuntersuchung: %s\n", patienten_db[i].letzte_Brustuntersuchung);
			}
		}
	}
}



