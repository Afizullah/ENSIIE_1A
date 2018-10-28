# include "honshu.h"

/**
 *	\brief "De-Alloue" le jeu
 *	\param hs : instance hs (partie jeu) qui va etre free
 *	(qui avait été allouée par hs_read/hs_rand)
 *	\see hs_read
 *	\see hs_rand
 */
void hs_delete(t_hs * hs) {
	entreur_delete(hs->entreur);
	afficheur_delete(hs->afficheur);
	grille_deinit(&(hs->grille));
	free(hs);
}

/**
 *	\internal Fonction interne qui alloue le jeu en mémoire
 */
static t_hs * hs_new(unsigned int n, unsigned int taille_main) {
	t_hs * hs = (t_hs *) malloc(sizeof(t_hs));
	if (hs == NULL) {
		return (NULL);
	}
	if (!grille_init(&(hs->grille), n, taille_main)) {
		free(hs);
		return (NULL);
	}
	hs->afficheur = afficheur_new(hs);
	hs->entreur   = entreur_new(hs);
	hs->flags     = 0;
	return (hs);
}

/**
 *	\brief Initialise le jeu avec le fichier donnée
 *	\param tuiles fichier où les tuiles sont déclarées
 *	\param save fichier ou le jeu est sauvegardé
 *	\return Une nouvelle partie d'honshu allouée et initialisée, NULL si erreur .
 */
t_hs * hs_read(char * tuiles, char * save) {

	FILE * fichier_tuile = fopen(tuiles, "r");
	if (fichier_tuile == NULL) {
		char * s = strerror(errno);
		fprintf(stderr, "Erreur de lecture (%s): %s\n", tuiles, s);
		return (NULL);
	}
	printf("Lecture du fichier : %s\n", tuiles);

	unsigned int nb_tuiles;
	if (fscanf(fichier_tuile, "%u\n", &nb_tuiles) != 1) {
		fprintf(stderr, "Erreur dans un fichier tuile: nombre"\
				"maximum de tuiles invalide\n");
		fclose(fichier_tuile);
		return (NULL);
	}

	char * cases;
	cases=malloc(sizeof(char)*nb_tuiles*6);

	unsigned int tuileID;
	char tuile_cases[6];
	while (fscanf(fichier_tuile, "%u\n%c %c\n%c %c\n%c %c\n", &tuileID, tuile_cases + 0,
				tuile_cases + 1, tuile_cases + 2, tuile_cases + 3,
				tuile_cases + 4, tuile_cases + 5) == 7) {
		int i;
		for (i = 0 ; i < 6 ; i++) {
			cases[tuileID * 6 + i] = tuile_cases[i];
		}
	}
	fclose(fichier_tuile);



	FILE * fichier_partie = fopen(save, "r");
	if (fichier_partie == NULL) {
		char * u = strerror(errno);
		fprintf(stderr, "Erreur lecture (%s): %s\n", save, u);
		free(cases);
		return(NULL);
	}
	printf("Lecture fichier partie : %s\n", save);

	unsigned int taille_grille, taille_main;
	if (fscanf(fichier_partie, "%u %u\n",&taille_grille, &taille_main) != 2) {
		fprintf(stderr, "Erreur dans le fichier, informations manquantes ligne 1\n");
		free(cases);
		return (NULL);
	}

	t_hs * hs = hs_new(taille_grille, taille_main);
	if (hs == NULL) {
		free(cases);
		return (NULL);
	}
	t_grille * grille = &(hs->grille);

	unsigned int i;
	for(i=0;i<taille_main;i++){
		t_tuile * tuile = grille->tuiles + i;
		char * t = cases;
		unsigned int tuileID;
		if (fscanf(fichier_partie, "%u", &tuileID) != 1) {
			fprintf(stderr, "Erreur de lecture de la tuile: %u\n", i);
		} else {
			t = cases + tuileID * 6;
		}
		BYTE caseID;
		for (caseID = 0 ; caseID < 6 ; caseID++) {
			tuile_set_case(tuile, case_type(t[caseID]), caseID);
		}

	}
	free(cases);

	unsigned int tuile_depart;
	if (fscanf(fichier_partie, "%u", &tuile_depart) != 1) {
		fprintf(stderr, "Aucunes tuiles de départ...");
	}
	t_tuile * tuile = grille->tuiles + tuile_depart;
	grille_insert(&(hs->grille), tuile, hs->grille.n / 2, hs->grille.n / 2);

	fclose(fichier_partie);
	return(hs);
}

/**
 *	\brief Initialise le jeu de manière aléatoire
 *	\param n : coté de la grille
 *	\param m : nombre de tuiles
 *	\return Une nouvelle partie d'honshu allouée et initialisée,
 *		generée de façon aléatoire
 *	, NULL si erreur 
 */
t_hs * hs_rand(INDEX n, unsigned int m) {
	/** on genere un 'n' aléatoire */
	if (n < 5) {
		n = 5;
	}
	if (m < 2) {
		m = 2;
	}
	t_hs * hs = hs_new(n, m);
	if (hs == NULL) {
		return (NULL);
	}

	/** on genere un nombre de tuile aléatoire */
	srand((unsigned int)time(NULL));
	INDEX i;
	for (i = 0 ; i < m ; i++) {
		/** on genere des tuiles aléatoires */
		t_tuile * tuile = hs->grille.tuiles + i;
		BYTE caseID;
		for (caseID = 0 ; caseID < 6 ; caseID++) {
			tuile_set_case(tuile, (unsigned int)rand() % TYPE_MAX, caseID);
		}
	}

	/** on insère une tuile aléatoire au centre */
	t_tuile * tuile = hs->grille.tuiles + ((INDEX)rand() % hs->grille.nb_tuiles);
	grille_insert(&(hs->grille), tuile, n / 2, n / 2);
	hs_export(hs, "tuiles.txt", "partie.txt");
	return (hs);
}

/**
 *	\brief sauvegarde les tuiles et la partie
 *	\param hs : une instance du jeu
 *	\param tuiles_chemin : le chemin de la sauvegarde du fichier tuile 
 *	\param partie_chemin : le chemin de la sauvegarde du fichier partie
 *	\return : 1 si succès, 0 sinon
 */
int hs_export(t_hs * hs, char * tuiles_chemin, char * partie_chemin) {
	/** on ouvre le fichier qui décrit les tuiles */
	FILE * tuiles_fichier = fopen(tuiles_chemin, "w+");
	if (tuiles_fichier == NULL) {
		char * err = strerror(errno);
		fprintf(stderr,"hs_export : (%s) %s\n", tuiles_chemin, err);
		return 0;
	}

	/** on ouvre le fichier de la partie */
	FILE * partie_fichier = fopen(partie_chemin, "w+");
	if (partie_fichier == NULL) {
		char * err = strerror(errno);
		fprintf(stderr,"hs_export 2: (%s) %s\n", partie_chemin, err);
		return 0;
	}

	/** on ecrit le nombre de tuile au total dans le fichier */
	fprintf(tuiles_fichier,"%u\n", hs->grille.nb_tuiles);

	/** on ecrit chaque tuiles */
	unsigned int i;
	for (i = 0 ; i < hs->grille.nb_tuiles ; i++) {
		t_tuile * tuile = hs->grille.tuiles + i;
		fprintf(tuiles_fichier, "%u\n", i);
		fprintf(tuiles_fichier, "%c %c\n%c %c\n%c %c\n",
				case_char(tuile->cases[0].type),
				case_char(tuile->cases[1].type),
				case_char(tuile->cases[2].type),
				case_char(tuile->cases[3].type),
				case_char(tuile->cases[4].type),
				case_char(tuile->cases[5].type));
	}
	fclose(tuiles_fichier);

	/** on écrit la partie */
	unsigned int taille_main = hs->grille.nb_tuiles;
	/** on écrit la taille de la carte et de la main */
	fprintf(partie_fichier,"%u %u\n", hs->grille.n, taille_main);

	for (i = 0; i < taille_main; i++) {
		/** on écrit l'id des tuiles */
		fprintf(partie_fichier, "%u ", i);
	}
	/** id de la tuile de départ (à insérer en (n/2, n/2) */
	fprintf(partie_fichier,"\n%u\n", 0);

	/** on ferme le fichier */
	fclose(partie_fichier);
	return (1);




}
