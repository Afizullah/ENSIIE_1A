/**
 * \file	includes/grille.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Data structures and core functions
 */

# ifndef GRILLE_H
#	define GRILLE_H

# include "honshu.h"

/** \internal : on precise au compilateur que les structures existent */
typedef struct s_case t_case;
/** \internal : on precise au compilateur que les structures existent */
typedef struct s_tuile t_tuile;
/** \internal : on precise au compilateur que les structures existent */
typedef enum e_case_type t_case_type;

/**
 *	\struct s_grille
 *
 *	La grille n*n de jeu sur laquelle on pose les tuiles
 *	\attention :	si une tuile est retiré, 'minX', 'minY', 'maxX', 'maxY'
 *			reste inchangé.
 */
typedef struct	s_grille {
	/** taille de la grille */
	INDEX n;

	/**
	 * tableau à 1 dimension de 't_case *', contenant:
	 * les cases de la grille
	 *
	 * l'indicage est:
	 *
	 *  0   1   ... n
	 * n+1 n+2  ... 2n-1
	 *          ...
	 *          ... n*n-1
	 */
	t_case ** cases;

	/** les tuiles (dans la main ET sur la carte) (\see tuile.h) */
	t_tuile * tuiles;

	/** taille du tableau 'tuiles' */
	unsigned int nb_tuiles;

	/** nombre de tuiles dans la main */
	unsigned int nb_tuiles_main;

	/** nombre de tuiles sur la grille */
	unsigned int nb_tuiles_grille;

	/** la file qui permet de visiter les cases (grille_visit) */
	INDEX * queue;

	/** position la plus en haut à gauche où une tuile a jamais été inséré */
	INDEX minX, minY;
	
	/** position la plus en bas à droite où une tuile a jamais été inséré */
	INDEX maxX, maxY;

	/** les flags */
	BYTE flags;
}		t_grille;

/**
 *	\brief Initialises (et alloue) la grille
 *	\param grille : la grille
 *	\param n : la taille de la grille
 *	\return 1 si succès, 0 sinon
 */
int grille_init(t_grille * grille, INDEX n, unsigned int taille_main);

/**
 *	\brief Dé-alloue la grille
 *	\param grille : la grille
 */
void grille_deinit(t_grille * grille);

/**
 *	\brief Renvoie la case aux coordonnées \a x et \a y de la grille
 *	\param grille : la grille
 *	\param x : coordonnée x
 *	\param y : coordonnée y
 *	\return la case aux coordonnées (x, y) sur la grille (peut être NULL)
 *	\see includes/case.h
*/
t_case * grille_get(t_grille * grille, INDEX x, INDEX y);

 /**
  *	\brief Teste si la tuile peut être inséré aux coordonnées données.
  *	\param grille : la grille
  *	\param tuile : la tuile a inséré
  *	\param x : la coordonnée x
  *	\param y : la coordonnée y
  *	\return 1 si la tuile peut être inséré, 0 sinon
  */
int grille_can_insert(t_grille * grille, t_tuile * tuile, INDEX x, INDEX y);

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
int grille_insert(t_grille * grille, t_tuile * tuile, INDEX x, INDEX y);

/**
 *	\brief Supprimes la tuile de la grille
 *	\param grille : la grille
 *	\param tuile : la tuile
 *	\return le nombre de tuile retiré (car les tuiles 'au dessus'
 *		peuvent être retiré si elles ne passent plus les tests
 *		de superposition)
 *	\see includes/tuile.h
 */
int grille_remove(t_grille * grille, t_tuile * tuile);

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
unsigned int grille_visit(t_grille * grille, INDEX x, INDEX y, t_case_type type);

/**
* \brief : calcule le score de la grille avec les règles données
* \param grille : la grille
* \return : retourne le score de la grille
*/
unsigned int grille_score(t_grille * grille);

# endif
