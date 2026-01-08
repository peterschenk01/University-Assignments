/*
 * listenaddition.c
 *
 *  Created on: 19.12.2019
 *      Author: Peter
 */

#include <stdio.h>
#include <stdlib.h>

struct list {
	int val;
	struct list* next;
};
typedef struct list list_el;
typedef list_el* list_t;

list_t add_element(list_t liste, int val){
	list_t neueListe = malloc(sizeof(list_t));
	neueListe->val = val;
	neueListe->next = liste->next;
	liste->next = neueListe;
	return neueListe;
}

list_t add_lists(list_t liste1, list_t liste2){
	list_t addedlist = malloc(sizeof(list_t));
	addedlist->val = 0;
	addedlist->next = NULL;
	int y, x = 0,z = 0;
	while(liste1 != NULL && liste2 != NULL){
		y = liste1->val + liste2->val + x;
		x = y / 10;
		y = y % 10;
		if(z == 0){
			addedlist->val = y;
			addedlist->next = NULL;
			z++;
		}
		else{
			add_element(addedlist, y);
		}
		if(liste1->next != NULL && liste2->next == NULL){
			liste1 = liste1->next;
			liste2->val = 0;
		}
		else if(liste1->next == NULL && liste2->next != NULL){
			liste1->val = 0;
			liste2 = liste2->next;
		}
		else{
			liste1 = liste1->next;
			liste2 = liste2->next;
		}
	}
	return addedlist;
}

void create_random_lists(list_t listen[10]){
	for(int i = 0; i < 10; i++){
		list_t randomlist = NULL;
		for(int x = 1; x < rand(7); x++){

		}

	}
}

int main(){
	setvbuf(stdout, NULL, _IONBF,0);
	list_t liste1 = malloc(sizeof(list_t));
	list_t liste2 = malloc(sizeof(list_t));
	liste1->val = 7;
	liste1->next = malloc(sizeof(list_t));
	liste1->next->val = 1;
	liste1->next->next = malloc(sizeof(list_t));
	liste1->next->next->val = 3;
	liste1->next->next->next =NULL;
	liste2->val = 8;
	liste2->next = malloc(sizeof(list_t));
	liste2->next->val = 2;
	liste2->next->next = NULL;
	list_t addedlist = malloc(sizeof(list_t));
	addedlist = add_lists(liste1, liste2);
	while(addedlist != NULL){
		printf("%d",addedlist->val);
		addedlist = addedlist->next;
	}
	return 0;
}
