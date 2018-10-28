/**
 * \file	includes/tuile.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	fonction sur les tuiles
 */

#ifndef TUILE_H
# define TUILE_H

# include <stdio.h>
# include <stdlib.h>
# include "case.h"

/**
 *	\enum rotations
 *	
 *	Les différentes rotations des tuiles
 */
enum e_rotations {
	/**
	 *	0	1
	 *
	 *
	 *	2	3
	 *
	 *
	 *	4	5
	 */
	NORD,

	/**
	 *	4	2	0
	 *
	 *
	 *	5	3	1
	 */
	EST,

	/**
	 *	5	4
	 *
	 *
	 *	3	2
	 *
	 *
	 *	1	0
	 *
	 */
	SUD,

	/**
	 *	1	3	5
	 *
	 *
	 *	0	2	4
	 */
	OUEST
};


/** un état précisant si la tuile est sur la carte */
# define TUILE_SUR_GRILLE (1 << 0)
/** un état précisant que cette tuile est la 1ere inséré au centre de la grille */
# define TUILE_ORIGINE (1 << 1)

/**
 *	\struct s_tuile
 *
 *	Structure représente une tuile.
 *	Son origine est situé 'en haut à gauche'
 */
typedef struct	s_tuile {
	/** les cases de la tuile ('V', 'F', ...) */
	t_case cases[6];

	/** état de la tuile (permettant de savoir si la tuile est sur la carte
	    ou dans la main) */
	BYTE flags;

	/** la rotation de la tuile */
	enum e_rotations rotation;

	/** position de la tuile sur la carte
	    (<=> position de la case en haut à gauche de la tuile sur la carte) */
	INDEX x;

	/** position de la tuile sur la carte
	  (<=> position de la case en haut à gauche de la tuile sur la carte) */
	INDEX y;
}		t_tuile;

/**
 *	Les décalages des cases relativement à l'origine de la tuile,
 *	selon sa rotation.
 * e.g: DECALAGE[NORTH][0] == (0, 0)
 *	DECALAGE[NORTH][1] == (1, 0)
 */
extern BYTE DECALAGE[4][6][2];

/**
 *	\brief Initialise la tuile
 *	\attention :	les cases sont initialisées à l'état 'TYPE_MAX'
 *			il faudra 'tuile_set_case' pour déterminer le
 *			type de case explcitement
 *	\see tuile_set_case
 */
void tuile_init(t_tuile * tuile);

/**
 *	\brief met une case de la tuile à un état (P/F/L/V/R/U/Max)
 *	\param tuile : une tuile
 *	\param t : le type de case a définir
 *	\param index : l'index de la case sur la tuile
 *	\return 1 si la case a été modifié, 0 sinon
 */
int tuile_set_case(t_tuile * tuile, t_case_type t, BYTE index);

/**
 *	\brief effectue la rotation d'une tuile
 *	\param tuile : la tuile qui subit la rotation
 */
void tuile_rotation(t_tuile * tuile);

#endif
