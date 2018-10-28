#include "affichage.h"
#include "honshu.h"

struct s_afficheur {
};



t_afficheur * afficheur_new(t_hs * hs){
	(void)hs;
	return (NULL);
}

void afficheur_delete(t_afficheur * afficheur) {
	(void)afficheur;
}

/** fonction qui va afficher la grille de la structure hs*/
static int affiche_grille(t_grille * grille){

	INDEX x;
	INDEX y;
	INDEX taille = grille->n;
	t_case * caze;
	char type;
	
	for (y=0;y<taille;y++){
		for (x=0;x<taille;x++){
			caze = grille_get(grille,x, y);
			type = (caze == NULL ? '.' : case_char(caze->type));
			printf("%c",type);
		}
		printf("\n");
	}
	return 0;
}
/** fonction qui affiche une seul tuile */
static int affiche_tuile(t_tuile * tuile, int index) {
	switch (tuile->rotation) {
		case NORD:
			printf("%d:\t%c %c\n\t%c %c\n\t%c %c\n",
				index,
				case_char(tuile->cases[0].type),
				case_char(tuile->cases[1].type),
				case_char(tuile->cases[2].type),
				case_char(tuile->cases[3].type),
				case_char(tuile->cases[4].type),
				case_char(tuile->cases[5].type));
			break ;
		case SUD:
			printf("%d:\t%c %c\n\t%c %c\n\t%c %c\n",
				index,
				case_char(tuile->cases[5].type),
				case_char(tuile->cases[4].type),
				case_char(tuile->cases[3].type),
				case_char(tuile->cases[2].type),
				case_char(tuile->cases[1].type),
				case_char(tuile->cases[0].type));
			break ;
		case EST:
			printf("%d:\t%c %c %c\n\t%c %c %c\n",
				index,
				case_char(tuile->cases[4].type),
				case_char(tuile->cases[2].type),
				case_char(tuile->cases[0].type),
				case_char(tuile->cases[5].type),
				case_char(tuile->cases[3].type),
				case_char(tuile->cases[1].type));
			break ;
		case OUEST:
			printf("%d:\t%c %c %c\n\t%c %c %c\n",
				index,
				case_char(tuile->cases[1].type),
				case_char(tuile->cases[3].type),
				case_char(tuile->cases[5].type),
				case_char(tuile->cases[0].type),
				case_char(tuile->cases[2].type),
				case_char(tuile->cases[4].type));
			break ;
	}
	return 0;
}

static int affiche_tuiles_main(t_hs* hs){
	t_grille * grille = &(hs->grille);
	unsigned int i;
	for (i=0;i<grille->nb_tuiles;i++){
		t_tuile * tuile = grille->tuiles + i;
		if (!FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
			printf("---------------------\n");
			affiche_tuile(tuile, i);
		}
	}
	return 0;
}


void afficher(t_hs * hs) {
	(void)hs;
	printf("\n\n\tGrille\n\n");
	affiche_grille(&(hs->grille));
	printf("\n\n\tMain du joueur\n");
	affiche_tuiles_main(hs);
}


