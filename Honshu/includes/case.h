/**
 * \file	includes/case.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Fonctions sur les cases
 */

#ifndef CASE_H
# define CASE_H

# include "honshu.h"

/**
 *	\enum case
 *	
 *	Les differentes type de case possibles sur une tuile
 */
typedef enum	e_case_type {
	TYPE_PLAINE,
	TYPE_FORET,
	TYPE_LAC,
	TYPE_VILLE,
	TYPE_RESSOURCE,
	TYPE_USINE,
	TYPE_MAX
}		t_case_type;

/** Flag indiquant si la case a déjà été visité lors du parcours en largeur
    pour le comptage des points villages. */
# define CASE_VISITED (1 << 0)

/**
 *	\struct s_case represente une case sur la grille
 */
typedef struct	s_case {
	/** case type id, \see enum e_cases */
	t_case_type type;

	/** case flags */
	BYTE flags;

	/** la case au dessus de celle ci (peut être NULL) */
	struct s_case * upper;

	/** la case en dessous de celle ci (peut être NULL) */
	struct s_case * under;
	
	/** pointeur vers la tuile a laquelle la case appartient */
	struct s_tuile * tuile;
}		t_case;


/**
 *	\brief initialise une case
 *	\param caze : la case en instance
 *	\param tuile : la tuile parent de cette case
 *	\param type : \see enum e_case_type
 *	\internal :	definit le type de case (TYPE_PLAINE, TYPE_FORET ...)
 *			definit la couche à 0
 *			initialise les pointeurs à NULL
 */
void case_init(t_case * caze, t_tuile * tuile, BYTE type);

/**
 * 	\brief renvoie le type associé à un caractère
 * 	\param charID : le caractère
 * 	\return le type associé au caractère
 */
enum e_case_type case_type(char charID);

/**
 *	\brief renvoie le caractère associé à un type de case
 *	\param type : une type de case
 *	\return le caractère associé
 */
char case_char(t_case_type type);

/**
 *	\brief définit le type d'une face
 *	\param caze : une case
 *	\param type : le type
 */
void case_set(t_case * caze, BYTE type);

#endif
