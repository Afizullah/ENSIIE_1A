#include "arbre.h"
#include <stdlib.h>
#include <stdio.h>

int main (void){
	struct Node* arbre0 = arbre_vide();
	struct Node* arbre1g = concat_arbre(0,arbre0,arbre0);
	struct Node* arbre1d = concat_arbre(0,arbre0,arbre0);
	struct Node* arbre2g = concat_arbre(3,arbre1g, arbre0);
	struct Node* arbre2d= concat_arbre(3,arbre1d, arbre0);
	struct Node* arbre3 = concat_arbre(17,arbre2g,arbre2d);
	if(est_vide(arbre0)) printf("l'arbre0 est vide\n");
	else printf("l'arbre0 n'est pas vide\n");
	printf("la hauteur de l'arbre0 est : %ld\n", hauteur(arbre0));

	if(est_vide(arbre1g)) printf("l'arbre1g est vide\n");
	else printf("l'arbre1g n'est pas vide\n");
	printf("la hauteur de l'arbre1g est : %ld\n", hauteur(arbre1g));
	if(recherche(1,arbre1d)){
		printf("l'arbre1d contient 1\n");
	}
	else printf("l'arbre1d ne contient pas 1\n");
	
	if(recherche(0,arbre1d)){
		printf("l'arbre1d contient 0\n");
	}
	else printf("l'arbre1d ne contient pas 1\n");
	printf("la hauteur de l'arbre3 est : %ld\n", hauteur(arbre3));
	printf("le cardinal de l'arbre3 est : %ld\n", cardinal(arbre3));
	clear(arbre3);
	return 0;
}

/*
			17
		3		3
	  0		  0
*/
