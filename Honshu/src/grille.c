# include "honshu.h"
#include <math.h>
/**
 *	\brief Initialises (et alloue) la grille
 *	\param grille : la grille
 *	\param n : la taille de la grille
 *	\return 1 si succès, 0 sinon
 */
int grille_init(t_grille * grille, INDEX n, unsigned int taille_main) {
	grille->cases = (t_case **) malloc(sizeof(t_case *) * n * n);
	if (grille->cases == NULL) {
		return (0);
	}
	grille->tuiles = (t_tuile *) malloc(sizeof(t_tuile) * taille_main);
	if (grille->tuiles == NULL) {
		free(grille->cases);
		return (0);
	}
	grille->queue = (INDEX *) malloc(sizeof(INDEX) * 2 * taille_main * 6);
	if (grille->queue == NULL) {
		free(grille->cases);
		free(grille->tuiles);
		return (0);
	}
	memset(grille->cases, 0, sizeof(t_case *) * n * n);
	unsigned int i;
	for (i = 0 ; i < taille_main ; i++) {
		tuile_init(grille->tuiles + i);
	}
	grille->nb_tuiles = taille_main;
	grille->nb_tuiles_main = taille_main;
	grille->nb_tuiles_grille = 0;
	grille->n = n;
	grille->minX = n;
	grille->minY = n;
	grille->maxX = 0;
	grille->maxY = 0;
	return (1);
}

/*
 *	\brief Dé-alloue la grille
 *	\param grille : la grille
 */
void grille_deinit(t_grille * grille) {
	free(grille->cases);
	free(grille->tuiles);
	free(grille->queue);
	grille->cases = NULL;
}

/**
 *	\brief Renvoie la case aux coordonnées \a x et \a y de la grille
 *	\param grille : la grille
 *	\param x : coordonnée x
 *	\param y : coordonnée y
 *	\return la case aux coordonnées (x, y) sur la grille (peut être NULL)
 *	\see includes/case.h
 */
t_case * grille_get(t_grille * grille, INDEX x, INDEX y) {
	return (grille->cases[y * grille->n + x]);
}

/** \internal : fonction interne pour définir une case de la grille */
static void grille_set(t_grille * grille, t_case * c, INDEX x, INDEX y) {
	grille->cases[y * grille->n + x] = c;
	if (c != NULL) {
		grille->minX = (grille->minX < x) ? grille->minX : x;
		grille->minY = (grille->minY < y) ? grille->minY : y;
		grille->maxX = (grille->maxX < x) ? x : grille->maxX;
		grille->maxY = (grille->maxY < y) ? y : grille->maxY;
	}
}

 /**
  *	\brief Teste si la tuile peut être inséré aux coordonnées données.
  *	\param grille : la grille
  *	\param tuile : la tuile a inséré
  *	\param x : la coordonnée x
  *	\param y : la coordonnée y
  *	\return 1 si la tuile peut être inséré, 0 sinon
  */
int grille_can_insert(t_grille * grille, t_tuile * tuile, INDEX x, INDEX y) {
	/** booléan afin de savoir si cette tuile en recouvre au moins une autre */
	BYTE atLeastOne = 0;

	/** liste des tuiles en dessous de celle qu'on insert */
	t_tuile * unders[6] = {NULL, NULL, NULL, NULL, NULL, NULL};
	/** nombre de tuile distinctes sous la tuile qu'on insère */
	BYTE tuilesUnder = 0;
	/** upper[i] est le nombre de fois que la tuile qu'on insère
	 *  apparait 'au dessus ' unders[i]
	 */
	BYTE uppers[6] = {0, 0, 0, 0, 0, 0};
	
	/** on vérifie que chaque case de la tuile respecte les regles d'insertion  */
	BYTE caseID;
	for (caseID = 0 ; caseID < 6 ; caseID++) {
		INDEX tx = x + DECALAGE[tuile->rotation][caseID][0];
		INDEX ty = y + DECALAGE[tuile->rotation][caseID][1];
		/** si hors de la carte, alors insertion impossible */
		if (tx >= grille->n || ty >= grille->n) {
			return (0);
		}
		/** on recupere le case en dessous */
		t_case * c = grille_get(grille, tx, ty);
		/** s'il n'y en a pas, on continue de vérifier les suivant */
		if (c == NULL) {
			continue ;
		}
		/** si c'est un lac, insertion impossible */
		if (c->type == TYPE_LAC) {
			return (0);
		}
		/** sinon, il y a une tuile en dessous */
		t_tuile * under = c->tuile;
		atLeastOne = 1;
		/** pour chaquue case déjà trouvé 'en dessous' */
		BYTE tuileID;
		for (tuileID = 0 ; tuileID < tuilesUnder ; tuileID++) {
			/** si c'est le meme case */
			if (unders[tuileID] == under) {
				/** on incrémente le nombre de case recouverte */
				++uppers[tuileID];
				break ;
			}
		}
		/** sinon, si c'est la 1ère fois qu'on rencontre ce case en dessous */
		if (tuileID == tuilesUnder) {
			/** on l'enregistre, et on initialise le compteur */
			unders[tuilesUnder] = under;
			uppers[tuilesUnder] = 1;
			++tuilesUnder;
		}
	}

	/** s'il n'y a aucunes autres tuiles en dessous de l'insertion...*/
	if (!atLeastOne) {
		return (0);
	}

	/** sinon, pour chaque tuile 'en dessous' de celle qu'on insère ... */
	BYTE tuileID;
	for (tuileID = 0 ; tuileID < tuilesUnder ; tuileID++) {
		t_tuile * under = unders[tuileID];
		for (caseID = 0 ; caseID < 6 ; caseID++) {
			t_case * c = under->cases + caseID;
			if (c->upper != NULL) {
				++uppers[tuileID];
			}
		}
		/** si plus de 6 cases sont recouverts, alors la tuile
		  est entierement recouverte: insertion impossible */
		if (uppers[tuileID] >= 6) {
			return (0);
		}
	}

	/** sinon, tous les tests sont passés */
	return (1);
}

/**
 *	\brief Insère la tuile dans la grille aux coordonnées (x, y)
 *	\param grille : la grille
 *	\param tuile : la tuile a inséré
 *	\param x : coordonnée x
 *	\param y : coordonnée y
 *	\return 1 si la tuile a été inséré, 0 sinon
 *	\attention : Aucuns tests d'insertion n'est effectué ici!
 *	\see grille_can_insert
 *	\see includes/tuile.h
 */
int grille_insert(t_grille * grille, t_tuile * tuile, INDEX x, INDEX y) {
	if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
		fprintf(stderr, "Tentative d'insertion d'une tuile déjà inséré!\n");
		return (0);
	}
	--grille->nb_tuiles_main;
	++grille->nb_tuiles_grille;

	/** on met à jour les pointeurs sur chaque case de la grille*/

	BYTE caseID;
	for (caseID = 0 ; caseID < 6 ; caseID++) {
		/** position du case sur la carte */
		INDEX tx = x + DECALAGE[tuile->rotation][caseID][0];
		INDEX ty = y + DECALAGE[tuile->rotation][caseID][1];
		/** le case de la tuile */
		t_case * c = tuile->cases + caseID;
		/** le case precedent sur cette case */
		t_case * under = grille_get(grille, tx, ty);

		/** on redefinit les pointeurs */
		c->upper	= NULL;
		c->under	= under;
		if (under != NULL) {
			under->upper = c;
		}

		/** on definit le case sur la carte */
		grille_set(grille, c, tx, ty);
	}
	tuile->x = x;
	tuile->y = y;
	FLAG_SET(tuile->flags, TUILE_SUR_GRILLE);
	/** si c'est la 1ere tuile */
	if (grille->nb_tuiles_grille == 1) {
		FLAG_SET(tuile->flags, TUILE_ORIGINE);
	}
	return (1);

}

/**
 *	\brief Supprimes la tuile de la grille
 *	\param grille : la grille
 *	\param tuile : la tuile
 *	\return le nombre de tuile retiré (car les tuiles 'au dessus'
 *		peuvent être retiré si elles ne passent plus les tests
 *		de superposition)
 *	\see includes/tuile.h
 */
int grille_remove(t_grille * grille, t_tuile * tuile) {
	/** si la tuile n'est pas sur la carte */
	if (!FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
		fprintf(stderr, "Retrait d'une tuile qui n'est pas sur la carte\n");
		return (0);
	}
	if (FLAG_ISSET(tuile->flags, TUILE_ORIGINE)) {
		fprintf(stderr, "Retrait de la tuile origine! Annulation.\n");
		return (0);
	}
	++grille->nb_tuiles_main;
	--grille->nb_tuiles_grille;

	/** on modifie le status de la tuile */
	FLAG_UNSET(tuile->flags, TUILE_SUR_GRILLE);

	/** liste des tuiles au dessus de celle qu'on retire */
	t_tuile * uppers[6] = {NULL, NULL, NULL, NULL, NULL, NULL};
	/** nombre de tuile distinctes au dessus de la tuile qu'on retire */
	BYTE tuilesUpside = 0;

	/** pour chaque case de la tuile */
	BYTE caseID;
	for (caseID = 0 ; caseID < 6 ; caseID++) {
		/** on recupere la case */
		t_case * c = tuile->cases + caseID;

		/** position du case sur la carte */
		INDEX tx = tuile->x + DECALAGE[tuile->rotation][caseID][0];
		INDEX ty = tuile->y + DECALAGE[tuile->rotation][caseID][1];

		/** s'il n'y a pas de case au dessus,
		  le case en dessous passe au dessus */
		if (c->upper == NULL) {
			grille_set(grille, c->under, tx, ty);
		} else {
			/** sinon, s'il y a un case au dessus */
			/** met à jour les pointeurs */
			c->upper->under = c->under;
			BYTE tuileID = 0;
			while (tuileID < tuilesUpside) {
				/** si le case est deja dans la liste, stop */
				if (uppers[tuileID] == c->upper->tuile) {
					break ;
				}
				++tuileID;
			}
			/** sinon, si c'est la 1ère fois qu'on rencontre ce case */
			if (tuileID == tuilesUpside) {
				/** on l'enregistre */
				uppers[tuilesUpside] = c->upper->tuile;
				++tuilesUpside;
			}
		}

		/** on met à jour les pointeurs */
		if (c->under != NULL) {
			c->under->upper = c->upper;
		}
		c->upper = NULL;
		c->under = NULL;
	}

	/** il ne reste plus qu'a vérifié que toutes les tuiles au dessus
	  de la tuile que l'on vient de retirer, vérifie bien les règles d'insertion,
	  sinon on les retire recursivement */
	int tuileRemoved = 1;
	BYTE tuileID = 0;
	/** pour chaque tuile au dessus */
	while (tuileID < tuilesUpside) {
		/** on recupere un case 'au dessus' */
		t_tuile * upper = uppers[tuileID];
		/** on vérifie qu'il y ait au moins un case
		  d'une autre tuile sous cette tuile */
		for (caseID = 0 ; caseID < 6 ; caseID++) {
			t_case * c = upper->cases + caseID;
			if (c->under != NULL) {
				/** il y a au moins un case en dessous,
				  cette tuile ne doit pas être retiré,
				  on passe à la tuile suivante */
				goto next_tuile ;
			}
		}
		/** sinon, cette tuile 'au dessus' n'a plus aucun case
		  'en dessous', on la retire de la grille récursivement */
		tuileRemoved += grille_remove(grille, upper);
next_tuile:	++tuileID;
	}
	return (tuileRemoved);
}

/**
 *	\brief : Comptes le nombre de cases contiguement adjacentes du même type
 *	\param grille : la grille
 *	\param x : coordonnée x d'une case
 *	\param y : coordonnée y d'une case
 *		(taille du Lac, taille de la ville...)
 *	\attention	Les cases contiguement adjacentes sont marqués
 *			par le flag 'CASE_VISITED'
 *	\see case.h
 */
unsigned int grille_visit(t_grille * grille, INDEX x, INDEX y, t_case_type type) {
	/** si la 1ere case n'est pas du bon type,
	 *  ou qu'elle a déjà été visité, renvoie 0 */
	t_case * c = grille_get(grille, x, y);
	if (c == NULL || c->type != type
			|| FLAG_ISSET(c->flags, CASE_VISITED)) {
		return (0);
	}

	/** sinon, on crée une file de visite, et on va visité toutes les cases
	  adjacentes du meme type */

	/** un buffer pour ajouter des positions du tableau 'queue' */
	INDEX * queue = grille->queue;
	INDEX pos[2] = {x, y};
	memcpy(queue, pos, sizeof(INDEX) * 2);
	FLAG_SET(c->flags, CASE_VISITED);

	/** le nombre de cases visitées */
	unsigned int visited = 0;

	/** le prochain index dans la file */
	unsigned int nextPos = 1;

	while (nextPos > 0) {
		/** pop le prochain case a visité de la file */
		--nextPos;
		INDEX x = queue[2 * nextPos + 0];
		INDEX y = queue[2 * nextPos + 1];

		/** un case de plus a été visité */
		++visited;

		/** pour chaque cases voisines */
		static int offset[4][2] = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
		BYTE i;
		for (i = 0 ; i < 4 ; i++) {
			/** on recupere le case voisin */
			INDEX nx = (INDEX)((int)x + offset[i][0]);
			INDEX ny = (INDEX)((int)y + offset[i][1]);
			/** si hors de la carte, pas de case voisin */
			if (nx >= grille->n || ny >= grille->n) {
				continue ;
			}
			/** sinon, si c'est le même type, et qu'il
			 *  n'a pas deja été visité, on le visite */
			t_case * next = grille_get(grille, nx, ny);
			if (next == NULL || next->type != type
					|| FLAG_ISSET(next->flags, CASE_VISITED)) {
				continue ;
			}
			/** on l'ajoute à la file */
			FLAG_SET(next->flags, CASE_VISITED);
			pos[0] = nx;
			pos[1] = ny;
			memcpy(queue + 2 * nextPos, pos, sizeof(INDEX) * 2);
			++nextPos;
		}
	}
	return (visited);
}


/**
 * \brief : calcule le score de la grille avec les règles données
 * \param grille : la grille
 * \param flags : les flags
 * \return : retourne le score de la grille
 */
unsigned int grille_score(t_grille * grille) {
	/** reset des flags de la grille */
	INDEX x, y;
	for (x = 0 ; x < grille->n ; x++) {
		for (y = 0 ; y < grille->n ; y++) {
			t_case * c = grille_get(grille, x, y);
			if (c != NULL) {
				FLAG_UNSET(c->flags, CASE_VISITED);
			}
		}
	}

	BYTE flags = grille->flags;

	/** calcul du score */
	unsigned int score = 0;
	unsigned int nb_usines = 0;
	unsigned int nb_ressources = 0;
	unsigned int nb_forets = 0;
	unsigned int nb_plaines = 0;
	unsigned int max_village = 0;
	unsigned int village;
	for (x = 0 ; x < grille->n ; x++) {
		for (y = 0 ; y < grille->n ; y++) {
			t_case * c = grille_get(grille, x, y);
			if (c != NULL && !FLAG_ISSET(c->flags, CASE_VISITED)) {
				switch (c->type) {
					case TYPE_VILLE:
						village = grille_visit(grille, x, y, TYPE_VILLE);
						if (village > max_village) {
							max_village = village;
						}
						break ;
					case TYPE_LAC:
						if (FLAG_ISSET(flags, HS_RULE_LAC)) {
							score += 2;
						} else {
							score += 3 * (grille_visit(grille, x, y, TYPE_LAC) - 1);
						}
						break ;
					case TYPE_FORET:
						if (FLAG_ISSET(flags, HS_RULE_FORET)) {
							nb_forets = grille_visit(grille, x, y, TYPE_FORET);
							if(nb_forets<=5) {
								score += nb_forets * (nb_forets + 1) / 2;
							} else{
								score += (1 + 2 + 3 + 4 + 5) + 5 * (nb_forets - 5);
							}
						} else {
							score += 2;
						}
						break ;
					case TYPE_USINE:
						++nb_usines;
						break;
					case TYPE_RESSOURCE:
						++nb_ressources;
						break ;
					case TYPE_PLAINE:
						nb_plaines = grille_visit(grille, x, y, TYPE_PLAINE);
						if (FLAG_ISSET(flags, HS_RULE_PLAINE) && nb_plaines == 4) {
							score += 4;
						}
						break;
					default:
						break ;
				}
				FLAG_SET(c->flags, CASE_VISITED);
			}
		}
	}
	score += max_village;
	score += MIN(FLAG_ISSET(flags, HS_RULE_FORET) ? nb_usines * 2 : nb_usines, nb_ressources) * 4;
	return (score);
}
