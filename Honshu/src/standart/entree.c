# include "honshu.h"

/**
 *	\struct s_entreur
 *
 *	structure principal de l'afficheur
 */
struct s_entreur {

};

/**
 *	\brief initialise l'entreur
 *	\param hs : l'instance du jeu
 *	\return un nouvel entreur (ou NULL)
 */
t_entreur * entreur_new(t_hs * hs) {
	(void)hs;
	return (NULL);
}

/**
 *	\brief de-initialise entreur
 *	\param entreur : l'instance de l'entreur
 */
void entreur_delete(t_entreur * entreur) {
	(void)entreur;
}

/** \internal : insère une tuile */
static void inserer_tuile(t_hs * hs, int indice) {
	int x, y;
	printf("Entrez la coordonnée x de la tuile\n> ");
	if (scanf("%d", &x) != 1) {
		return ;
	}
	printf("Entrez la coordonnée y de la tuile\n> ");
	if (scanf("%d", &y) != 1) {
		return ;
	}
		t_grille * grille = &(hs->grille);
	t_tuile * tuile = grille->tuiles + indice;
	if (grille_can_insert(&(hs->grille),tuile, x, y)) {
		grille_insert(&(hs->grille), tuile, x, y);
	} else {
		printf("La tuile ne peut pas etre insérée aux coordonnées données\n");
	}
}


/**
 *	\brief met à jour les entrés utilisateurs
 *	\param honshu : l'instance du jeu
 */
void entrer(t_hs * hs) {
	/* si plus de tuiles, fin de la partie */
	if (hs->grille.nb_tuiles_main == 0) {
		FLAG_UNSET(hs->flags, HS_RUNNING);
		return ;
	}

	t_grille * grille = &(hs->grille);
	unsigned int n;
	unsigned int indice;
	printf("Tapez:\n1. si vous voulez insérer une tuile\n2. si vous voulez tourner une tuile\n3. si vous voulez supprimer une tuile\n> ");
	if (scanf("%u", &n) <= 0) {
		return ;
	}
	printf("Entrez l'indice de la tuile dont vous voulez modifier l'état\n> ");
	if (scanf("%u",&indice) <= 0) {
		return ;
	}
	if (indice >= grille->nb_tuiles || n > 3) {
		/** on vide le buffer de scanf */
		char c;
		while (scanf("%c", &c) > 0 && c != '\n' && c != EOF);
		printf("Vous vous êtes trompé, veuillez tapez 1, 2 ou 3 et un indice valide\n");
		return ;
	}
	if(n==1){
		printf("Vous avez choisi d'insérer une tuile\n");
		inserer_tuile(hs, indice);

	}

	else if(n==2){

		printf("Vous avez choisi de tourner un tuile\n");
		tuile_rotation(grille->tuiles + indice);

	}

	else if(n==3){
		printf("Vous avez choisi de supprimer une tuile\n");
		grille_remove(&(hs->grille), grille->tuiles + indice);

	}
}
