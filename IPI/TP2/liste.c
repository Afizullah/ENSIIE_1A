#include "liste.h"
#include <stdlib.h>
#include <stdio.h>
struct Liste{
	int data;
	liste next;
};

liste liste_vide(){
	return NULL;
}
int est_vide(liste l){
	if (l == NULL) return 1;
	else return 0;
};

liste insert(int a, liste l){

	liste aux = malloc(sizeof(struct Liste)); 
	aux->data = a; 
	aux->next = l;
	return aux;
}

liste concat(liste a, liste b){

	if(a==NULL) return b;
	if(b==NULL) return a;
	liste curseur = a;
	while((curseur->next)!=NULL){
		curseur=curseur->next;
	}
	curseur->next=b;
	return a;
}

int taille(liste l){

	if(l==NULL) return 0;
	int count = 1;
	liste curseur = l;
	while((curseur->next)!=NULL){
		curseur=curseur->next;
		count++;
	}
	return count;
}


void affiche(liste l){

	liste curseur = l;
	printf("{");
	while(curseur!=NULL){
		printf("%d, ",curseur->data);
		curseur=curseur->next;
	}
	printf("}\n");
}

int head(liste l){

	if(est_vide(l)) {
		fprintf(stderr,"head : la liste est vide\n");
		exit(0);
	}
	return l->data;
}

liste pop(liste l){

	if(est_vide(l)) {
		fprintf(stderr,"pop : la liste est vide\n");
		exit(0);
	}
	return l->next;
}
