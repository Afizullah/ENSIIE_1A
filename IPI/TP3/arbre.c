#include <stdlib.h>
#include <stdio.h>
#include "arbre.h"
#include <math.h>

struct Node{
	int data;
	struct Node* gauche;
	struct Node* droit;
};

struct Node* arbre_vide(void){
	return NULL;
}

int est_vide(struct Node* arbre){
	if (arbre == NULL){return 1;}
	else{return 0;}

}

struct Node* concat_arbre(int n, struct Node* sa_gauche, struct Node* sa_droit){

	struct Node* arbre = malloc(sizeof(struct Node));
	arbre->data = n;
	arbre->gauche = sa_gauche;
	arbre->droit = sa_droit;

	return arbre;
}

unsigned long cardinal(struct Node* arbre){
	
	if(est_vide(arbre)) return 0;
	return 1+cardinal(arbre->gauche)+cardinal(arbre->droit);
}

unsigned long hauteur(struct Node* arbre){
	
	if(est_vide(arbre)) return 0;
	unsigned long hg = hauteur(arbre->gauche);
	unsigned long hd = hauteur(arbre->droit);
	if (hg>=hd) return 1 + hg;
	else return 1 + hd;
}

int recherche (int n, struct Node* arbre){
/*RECURSIVITE*/
if(est_vide(arbre)) return 0;
return (arbre->data == n || recherche(n,arbre->gauche) || recherche(n,arbre->droit));

}
/*INTERDICTION DE RECYCLER DES ARBRES SINON ON FREE PLUSIEURS LE MÊME EMPLACEMENT MÉMOIRE*/
void clear(struct Node* arbre){
if(est_vide(arbre)) return;
clear(arbre->gauche); 
clear(arbre->droit);
free(arbre);
}
