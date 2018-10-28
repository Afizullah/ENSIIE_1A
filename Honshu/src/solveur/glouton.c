# include "honshu.h"

/**
 *	\internal :	fonction qui pour une grille (et un état des tuiles) donnée,
 *			et applique un algorithme glouton de résolution
 */
static void solve_glouton_insert(t_grille * grille, t_resultat * buff) {
	/** tant qu'il reste des tuiles a inséré */
	while (grille->nb_tuiles_main > 0) {
		/** on cherche la tuile optimal a inséré */
		INDEX	bestTuileID;
		BYTE	bestRotationID;
		INDEX 	bestX, bestY;
		unsigned int bestScore;

		/* initialisation */
		bestScore = 0;

		/* pour chaque tuile */
		unsigned int tuileID;
		for (tuileID = 0 ; tuileID < grille->nb_tuiles ; tuileID++) {
			/** si la tuile est déjà sur la carte, on passe à la suivante */
			t_tuile * tuile = grille->tuiles + tuileID;
			if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
				continue ;
			}
			/** pour chaque rotation */
			unsigned int rotationID;
			for (rotationID = 0 ; rotationID < 4 ; rotationID++) {
				tuile->rotation = rotationID;
				/** on teste d'inserer sur chaque case */
				INDEX beginX = grille->minX < 2 ? 0 : grille->minX - 2;
				INDEX beginY = grille->minY < 2 ? 0 : grille->minY - 2;
				INDEX x, y;
				for (x = beginX ; x <= grille->maxX ; x++) {
					for (y = beginY ; y <= grille->maxY ; y++) {
						/** si l'on peut inséré la tuile */
						if (grille_can_insert(grille, tuile, x, y)) {
							/** on l'insere */
							grille_insert(grille, tuile, x, y);
							unsigned int score = grille_score(grille);
							if (score > bestScore) {
								bestScore	= score;
								bestTuileID	= tuileID;
								bestRotationID	= rotationID;
								bestX		= x;
								bestY		= y;
							}

							/** on résout avec la configuration suivante */
							grille_remove(grille, tuile);
						}
					}
				}
			}
		}


		/** si aucunes tuiles ne peut être insérés */
		if (bestScore == 0) {
			/** fin de la recherche... */
			fprintf(stderr, "Plus aucunes tuiles ne peut être inséré... Le jeu est bloqué\n");
			break ;
		}

		/** on l'insère */
		t_tuile * tuile = grille->tuiles + bestTuileID;
		tuile->rotation = bestRotationID;
		grille_insert(grille, tuile, bestX, bestY);

		/** on enregistre l'insertion */
		t_insertion * insertion = buff->insertion + buff->nb_tuiles;
		insertion->tuileID = bestTuileID;
		insertion->rotationID = bestRotationID;
		insertion->x = bestX;
		insertion->y = bestY;
		insertion->score = bestScore;
		++buff->nb_tuiles;
	}

	/** on nettoie la grille pour la remettre dans son état initial */
	int i;
	for (i = buff->nb_tuiles - 1 ; i >= 0; i--) {
		/** si la tuile est déjà sur la carte, on passe à la suivante */
		t_insertion * insertion = buff->insertion + i;
		t_tuile * tuile = grille->tuiles + insertion->tuileID;
		if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
			grille_remove(grille, tuile);
		}
	}
}

/**
 *	\brief	trouves une solution par un algorithme glouton (optimum local),
 *		en fonction de l'état du jeu passé en paramètre.
 *	\param grille : l'état de la grille (et des tuiles)
 *	\return le resultat du solveur pour obtenir un score optimal
 *	\attention	Le resultat doit être 'free()' !
 *	\see struct s_resultat
 */
t_resultat * solve_glouton(t_grille * grille) {
	t_resultat * buff = (t_resultat *) malloc(sizeof(t_resultat) + sizeof(t_insertion) * grille->nb_tuiles_main);
	/** erreur malloc */
	if (buff == NULL) {
		free(buff);
		return (NULL);
	}
	buff->insertion	= (t_insertion *) (buff + 1);
	buff->nb_tuiles	= 0;

	/** on résout */
	solve_glouton_insert(grille, buff);

	return (buff);
}
