# include "honshu.h"

/**
 *	\internal : affiche les règles
 */
static void rules(void) {
	fprintf(stderr, "Règles par défaut:\n");
	fprintf(stderr, "  - Un village est composé de cases villes 'V' adjacentes. Si un village possède 'n' villes, il rapporte 'n' points.\n");
	fprintf(stderr, "  - Chaque case forêt apporte 2 points.\n");
	fprintf(stderr, "  - On regarde tous les Lacs. Le joueur marque 3 x (nombre de cases lac du Lac - 1) points\n");
	fprintf(stderr, "  - Les cases usines et ressources sont comptées 1 fois et apporte: 4 x min(ressources, usines) points.\n");
}

/**
 *	\internal affiche l'aide de lancement à l'utilisateur
 */
static void help(char * prog) {
	fprintf(stderr, ".-----------------------------------------------------------.\n");
	fprintf(stderr, "| usage: %s %-34s|\n", prog, "{-h}");
	fprintf(stderr, "|                         {-LPUCF}                          |\n");
	fprintf(stderr, "|                         {-t [TUILES]} {-p [PARTIE]}       |\n");
	fprintf(stderr, "|                         {[-n [n]] [-m [m]]}       	    |\n");
	fprintf(stderr, "|                         {-s [MODE]}                       |\n");
	fprintf(stderr, "|                                                           |\n");
	fprintf(stderr, "|  avec: - L l'option pour qu'une case Lac vaille 2 points  |\n");
	fprintf(stderr, "|  	 - P l'option pour qu'une plaine de 4 cases 	    |\n");
	fprintf(stderr, "|  	     vaillent 4 points				    |\n");
	fprintf(stderr, "|  	 - U l'option pour qu'une usine puisse recevoir     |\n");
	fprintf(stderr, "|  	      deux ressources				    |\n");
	fprintf(stderr, "|  	 - C l'option pour qu'un carré de 4 cases villages, |\n");
	fprintf(stderr, "| 	      s'il est dans la ville comptée pour le score, |\n");
	fprintf(stderr, "|  	 - F l'option pour que pour une forêt, la i-ème     |\n");
	fprintf(stderr, "|  	     case forêt donne i points dans la limite de 5  |\n");
	fprintf(stderr, "|  	     cases forêts				    |\n");
	fprintf(stderr, "|  	      						    |\n");
	fprintf(stderr, "|  	  - [TUILES] un fichier décrivant des tuiles.       |\n");
	fprintf(stderr, "|        - [PARTIE] un fichier décrivant une partie         |\n");
	fprintf(stderr, "|        - [n]      la taille d'une grille aléatoire (>= 5) |\n");
	fprintf(stderr, "|        - [m]      le nombre de tuiles aléatoires (>= 2)   |\n");
	fprintf(stderr, "|        - [MODE]   le mode a utilisé pour le solveur,      |\n");
	fprintf(stderr, "|                   'brute', 'brute_filtre', 'glouton'      |\n");
	fprintf(stderr, "'-----------------------------------------------------------'\n");
	fprintf(stderr, "\n");
	rules();
}

/**
 *	\internal : une fonction interne pour convertir une chaine de caractères
 *              en entier non signé
 */
static unsigned int parse_uint(char * str) {
	unsigned int i;
	for (i = 0 ; str[i] ; i++) {
		if (*str < '0' || *str > '9') {
			return (0);
		}
	}
	int v = atoi(str);
	return (v < 0 ? 0 : (unsigned int)v);
}

/**
 *	\internal : main
 *  1) lis les arguments (voir --help)
 *  2) alloues/initialise la mémoire
 *  3) boucle infini de jeu
 *  4) libération de la mémoire, fin du programme
 */
int main(int argc, char ** argv) {

	t_hs * hs = NULL;
	BYTE flags;
	char * tuiles = NULL;
	char * partie = NULL;
	char * solve_mode = NULL;
	INDEX random_n = 0;
	unsigned int random_m = 0;
	char opt;

	FLAG_RESET(flags);
	while ((opt = getopt(argc, argv, "hp:t:n:m:s:LPUCF"))!= -1) {
		switch (opt) {
			case 'h':
				help(argv[0]);
				if (hs != NULL) {
					hs_delete(hs);
				}
				return (EXIT_SUCCESS);
			case 'p':
				if (hs != NULL) {
					break ;
				}
				partie = optarg;
				if (tuiles != NULL) {
					hs = hs_read(tuiles, partie);
				}
				break ;
			case 't':
				if (hs != NULL) {
					break ;
				}
				tuiles = optarg;
				if (partie != NULL) {
					hs = hs_read(tuiles, partie);
				}
				break ;
			case 'n':
				if (hs != NULL) {
					break ;
				}
				random_n = parse_uint(optarg);
				if (random_m != 0) {
					hs = hs_rand(random_n, random_m);
				}
				break;
			case 'm':
				if (hs != NULL) {
					break ;
				}
				random_m = parse_uint(optarg);
				if (random_m != 0) {
					hs = hs_rand(random_n, random_m);
				}
				break;
			case 's':
				solve_mode = optarg;
				break ;
			case 'L':
				FLAG_SET(flags, HS_RULE_LAC);
				break ;
			case 'P':
				FLAG_SET(flags, HS_RULE_PLAINE);
				break ;
			case 'U':
				FLAG_SET(flags, HS_RULE_USINE);
				break ;
			case 'C':
				FLAG_SET(flags, HS_RULE_CARRE);
				break ;
			case 'F':
				FLAG_SET(flags, HS_RULE_FORET);
				break ;
		}
	}
	if (hs == NULL) {
		help(argv[0]);
		return (EXIT_FAILURE);
	}

	/** on sait que hs est non null */
	hs->grille.flags = flags;

	FLAG_SET(hs->flags, HS_INITIALIZED);
	fprintf(stderr, "Le jeu est initialisé\n");

	/* sinon, on recupere la fonction de solveur */
	if (solve_mode != NULL) {
		t_solveur solveur = NULL;
		if (strcmp(solve_mode, "brute") == 0) {
			solveur = solve_brute;
		} else if (strcmp(solve_mode, "brute_filtre") == 0) {
			solveur = solve_brute_filtre;
		} else if (strcmp(solve_mode, "glouton") == 0) {
			solveur = solve_glouton;
		}

		if (solveur == NULL) {
			fprintf(stderr, "Le solveur n'existe pas: %s\n", solve_mode);
		} else {
			fprintf(stderr, "Lancement du solveur '%s' sur une grille n=%u avec %u tuiles dans la main\n",
					solve_mode, hs->grille.n, hs->grille.nb_tuiles_main);
			t_resultat * resultat = solveur(&(hs->grille));
			unsigned int i;
			for (i = 0 ; i < resultat->nb_tuiles ; i++) {
				t_insertion * insertion = resultat->insertion + i;
				t_tuile * tuile = hs->grille.tuiles + insertion->tuileID;
				tuile->rotation = insertion->rotationID;
				grille_insert(&(hs->grille), tuile, insertion->x, insertion->y);
			}
			fprintf(stderr, "Score obtenu: %u\n", grille_score(&(hs->grille)));
			free(resultat);
		}
	}
	FLAG_SET(hs->flags, HS_RUNNING);
	fprintf(stderr, "Le jeu boucle\n");

	/* jeu normal */
	while (FLAG_ISSET(hs->flags, HS_RUNNING)) {
		/** affichage */
		afficher(hs);
		/** entrée clavier */
		entrer(hs);
	}


	fprintf(stderr, "Le jeu est arrêté\n");

	/** libère la mémoire */
	hs_delete(hs);

	return (EXIT_SUCCESS);
}
