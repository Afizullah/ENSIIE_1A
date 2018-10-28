/**
 * \file	includes/solveur.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	fonction sur les solveurs
 */

#ifndef SOLVEUR_H
# define SOLVEUR_H

# include "honshu.h"

/** structure qui enregistre une insertion de tuiles */
typedef struct	s_insertion {
	/** index de la tuile dans 'grille−>tuiles' */
	unsigned int tuileID;
	/** rotation de la tuile */
	BYTE rotationID;
	/** position ou la tuile a été inseré */
	INDEX x, y;
	/** score après cette insertion */
	unsigned int score;
}		t_insertion;

/** structure qui renvoie le résultat du solveur */
typedef struct	s_resultat {
	/** l'ordre dans lequel les tuiles doivent être insérés */
	t_insertion	* insertion;
	/** taille du tableau 'insertion' */
	unsigned int	nb_tuiles;
}		t_resultat;

/** typedef d'une fonction de résolution */
typedef t_resultat * (* t_solveur) (t_grille * grille) ;

/**
 *	\brief	trouves la solution optimal par brute force (optimum global),
 *		en fonction de l'état du jeu passé en paramètre.
 *	\param grille : l'état de la grille (et des tuiles)
 *	\return le resultat du solveur pour obtenir un score optimal
 *	\attention	Le resultat doit être 'free()' !
 *	\see struct s_resultat
 */
t_resultat * solve_brute(t_grille * grille);

/**
 *	\brief	trouves une solution par brute force en appliquant un filtre
 *	\param grille : l'état de la grille (et des tuiles)
 *	\return le resultat du solveur
 *	\attention	Le resultat doit être 'free()' !
 *	\see struct s_resultat
 */
t_resultat * solve_brute_filtre(t_grille * grille);


/**
 *	\brief	trouves une solution par un algorithme glouton (optimum local),
 *		en fonction de l'état du jeu passé en paramètre.
 *	\param grille : l'état de la grille (et des tuiles)
 *	\return le resultat du solveur pour obtenir un score optimal
 *	\attention	Le resultat doit être 'free()' !
 *	\see struct s_resultat
 */
t_resultat * solve_glouton(t_grille * grille);

#endif
