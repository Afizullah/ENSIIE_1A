/**
 * \file	includes/ncurses/entree.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Fonctions relative aux entrées claviers
 */

#ifndef ENTREE_NCURSES_H
# define ENTREE_NCURSES_H

# include "entree.h"
/**
 *	\struct s_entreur
 *
 *	structure principal de l'afficheur
 */
typedef struct	s_entreur {
	/** position du curseur d'insertion de la tuile */
	INDEX cursorX, cursorY;

	/** index de la tuile selectionné dans le tableau 'honshu->tuiles' */
	INDEX tuile;
}		t_entreur;

#endif
