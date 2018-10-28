typedef struct Liste* liste;
liste liste_vide();
int est_vide(liste);
liste insert(int, liste);
liste concat(liste, liste);
int taille(liste);
void affiche(liste);
liste pop(liste);
int head (liste);

/*(){

	affiche(l);
	printf("%d\n",taille(l));
	if (est_vide(l)==1) printf("la liste est vide\n");


	else{ printf("la liste n'est pas vide\n"); }
	liste l2 =insert(2,l);
	affiche(l2);	
	return 0;
};*/
