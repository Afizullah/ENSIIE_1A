# include "honshu.h"
# include "ncurses/entree.h"
# include <ncurses.h>
/**
 *	\brief initialise l'entreur
 *	\param hs : l'instance du jeu
 *	\return un nouvel entreur (ou NULL)
 */
t_entreur * entreur_new(t_hs * hs) {
	(void)hs;
	t_entreur * entreur = (t_entreur *) malloc(sizeof(t_entreur));
	if (entreur == NULL) {
		return (NULL);
	}
	entreur->cursorX = 0;
	entreur->cursorY = 0;
	entreur->tuile   = 0;
	return (entreur);
}

/**
 *	\brief de-initialise entreur
 *	\param entreur : l'instance de l'entreur
 */
void entreur_delete(t_entreur * entreur) {
	free(entreur);
}

/**
 *	\internal :	fonction interne, effectue une action
 *			en foncton de l'état de l'entreur (insertion
 */
static void action(t_hs * hs) {
	t_grille * grille = &(hs->grille);
	t_tuile * tuile = grille->tuiles + hs->entreur->tuile;
	if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)
			&& hs->grille.nb_tuiles_grille > 1) {
		grille_remove(grille, tuile);
	} else {
		INDEX x = hs->entreur->cursorX;
		INDEX y = hs->entreur->cursorY;
		if (grille_can_insert(grille, tuile, x, y)) {
			grille_insert(grille, tuile, x, y);
			hs->entreur->tuile = (hs->entreur->tuile + 1) % grille->nb_tuiles;
		}
	}
}

/**
 *	\internal fonction interne qui deplace le curseur de selection
 */
static void move_cursor(t_hs * hs, int dx, int dy) {
	int cx = (int)hs->entreur->cursorX + dx;
	int cy = (int)hs->entreur->cursorY + dy;
	int n  = (int) hs->grille.n;
	if (cx < 0) {
		cx = cx % n + n;
	} else {
		cx = cx % n;
	}
	if (cy < 0) {
		cy = cy % n + n;
	} else {
		cy = cy % n;
	}

	hs->entreur->cursorX = (INDEX) cx;
	hs->entreur->cursorY = (INDEX) cy;
}

/**
 *	\brief met à jour les entrés utilisateurs
 *	\param honshu : l'instance du jeu
 */
void entrer(t_hs * hs) {
	t_grille * grille = &(hs->grille);
	int ch = getch();
	switch (ch) {
		case KEY_LEFT:
			move_cursor(hs, -1,  0);
			break ;
		case KEY_RIGHT:
			move_cursor(hs,  1,  0);
			break ;
		case KEY_DOWN:
			move_cursor(hs,  0,  1);
			break ;
		case KEY_UP:
			move_cursor(hs,  0, -1);
			break ;
		case 'q':
			move_cursor(hs, -1,  0);
			break ;
		case 'd':
			move_cursor(hs,  1,  0);
			break ;
		case 's':
			move_cursor(hs,  0,  1);
			break ;
		case 'z':
			move_cursor(hs,  0, -1);
			break ;
		case 'e':
			hs->entreur->tuile = (hs->entreur->tuile == hs->grille.nb_tuiles - 1) ? 0 : hs->entreur->tuile + 1;
			break ;
		case 'a':
			hs->entreur->tuile = (hs->entreur->tuile == 0) ?
						hs->grille.nb_tuiles - 1:
						hs->entreur->tuile - 1;
			break ;
		case 'r':
			if (!FLAG_ISSET((grille->tuiles + hs->entreur->tuile)->flags,
					TUILE_SUR_GRILLE)) {
				tuile_rotation(hs->grille.tuiles + hs->entreur->tuile);
			}
			break ;
		case ' ':
			action(hs);
			break ;
		case 27: /** ESCAPE */
			FLAG_UNSET(hs->flags, HS_RUNNING);
			break ;
		default:
			break ;
	}
}

