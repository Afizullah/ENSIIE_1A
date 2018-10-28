#include "liste.h"
#include <stdlib.h>
#include <stdio.h>

struct Maillon{
int data;
struct Maillon* next;
};

typedef struct Maillon* maillon;

struct Liste{
	maillon debut;
	maillon fin;
};

liste liste_vide(){
	liste l = malloc(sizeof(struct Liste));
	l->debut=NULL;
	l->fin=NULL;
	return l;
}

int est_vide(liste l){
	if (l==NULL) return 1;
	if (l->debut == NULL) return 1;
	return 0;
};
/* renvoie la liste l avec le nouveau maillon au début*/
liste insert(int a, liste l){
	/* on crée le nouveau maillon*/
	maillon prems = malloc(sizeof(struct Maillon)); 
	prems->data = a; 
	prems->next = l->debut;
	l->debut=prems;
	return l;
}

liste concat(liste a, liste b){

	if(a->debut==NULL) return b;
	if(b->debut==NULL) return a;
/*	maillon curseur = a;
	while((curseur->next)!=NULL){
		curseur=curseur->next;
	}
	curseur->next=b->debut;
	return a;*/
	a->fin->next = b->debut;
	return a;
}

int taille(liste l){
	if(est_vide(l)) return 0;
	int count = 1;
	maillon curseur = l->debut;
	while((curseur->next)!=NULL){
		curseur=curseur->next;
		count++;
	}
	return count;
}


void affiche(liste l){
	if (est_vide(l)) { 
		printf("{ }\n");
		return;
	}
	maillon curseur = l->debut;
	printf("{");
	while(curseur!=NULL){
		printf(" %d ",curseur->data);
		curseur=curseur->next;
	}
	printf("}\n");
}

int head(liste l){
	
	if(est_vide(l)) {
		fprintf(stderr,"head : la liste est vide\n");
		exit(0);
	}
	return l->debut->data;
}

liste pop(liste l){
	
	if(est_vide(l)) {
		fprintf(stderr,"pop : la liste est vide\n");
		exit(0);
	}
	l->debut=l->debut->next;	
	return l;
}
