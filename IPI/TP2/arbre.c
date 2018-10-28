#include <stdlib.h>
#include <stdio.h>

struct Node{
	int data;
	struct Node* gauche;
	struct Node* droit;
};

typedef struct Node* node;

node arbre_vide(void){
	node new = malloc(sizeof(struct node));
	return new;
}

