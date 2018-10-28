# include "honshu.h"

/*# define EMPIRIC_COMPLEXITY*/

/** \internal :	fonction qui pour une grille (et un état des tuiles) donnée,
 * 		insères les tuiles en main dans toutes les configurations
 *		possibles, et renvoie le score associé
 */
static void solve_brute_insert(t_grille * grille, t_resultat * best, t_resultat * buff, unsigned int next) {

	/** pour chaque tuile */
	unsigned int tuileID;
	for (tuileID = 0 ; tuileID < grille->nb_tuiles ; tuileID++) {

#ifdef EMPIRIC_COMPLEXITY
		unsigned int can_insert_compteur = 0;
#endif

		t_tuile * tuile = grille->tuiles + tuileID;
		/** si la tuile est déjà sur la carte, on passe à la suivante */
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
#ifdef EMPIRIC_COMPLEXITY
						++can_insert_compteur;
#endif
						/** on l'insere */
						grille_insert(grille, tuile, x, y);

						/** on l'ajoute au résultat temporaire */
						t_insertion * insertion = buff->insertion + next;
						insertion->tuileID = tuileID;
						insertion->rotationID = rotationID;
						insertion->x = x;
						insertion->y = y;
						insertion->score = grille_score(grille);

						/** si toutes les tuiles ont été insérés */
						if (grille->nb_tuiles_main == 0) {
							if (best->insertion[next].score < insertion->score) {
								memcpy(best->insertion, buff->insertion,
										sizeof(t_insertion) * (next + 1));
								best->nb_tuiles = next + 1;
							}
						/** filtre */
						} else if (insertion->score > 1.1f * best->insertion[next].score) {
							/** on résout récursivement */
							solve_brute_insert(grille, best, buff, next + 1);
						}

						/** on résout avec la configuration suivante */
						grille_remove(grille, tuile);
					}
				}
			}
		}
#ifdef EMPIRIC_COMPLEXITY
	printf("%d ; %d\n", grille->nb_tuiles_grille, can_insert_compteur);
#endif
	}
}

/**
 *	\brief trouves la solution optimal, en fonction de l'état du jeu passé en paramètre.
 *	\param grille : l'état de la grille (et des tuiles)
 *	\return le resultat du solveur pour obtenir un score optimal
 *	\attention	Le resultat doit être 'free()' !
 *	\see struct s_resultat
 */
t_resultat * solve_brute_filtre(t_grille * grille) {
	t_resultat * best = (t_resultat *) malloc(sizeof(t_resultat) + sizeof(t_insertion) * grille->nb_tuiles_main);
	t_resultat * buff = (t_resultat *) malloc(sizeof(t_resultat) + sizeof(t_insertion) * grille->nb_tuiles_main);
	/** erreur malloc */
	if (best == NULL || buff == NULL) {
		free(best);
		free(buff);
		return (NULL);
	}
	best->insertion = (t_insertion *) (best + 1);
	best->nb_tuiles = 0;

	buff->insertion = (t_insertion *) (buff + 1);
	buff->nb_tuiles = 0;

	/** on résout */
	memset(best->insertion, 0, grille->nb_tuiles_main * sizeof(t_insertion));
	solve_brute_insert(grille, best, buff, 0);

	/** on libère le buffer */
	free(buff);
	return (best);
}
