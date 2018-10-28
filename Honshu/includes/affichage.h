/**
 * \file	includes/affichage.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Fonctions d'affichage de la grille
 */

#ifndef AFFICHAGE_H
# define AFFICHAGE_H

#include "honshu.h"

/** \internal : on signale que la structure existe */
typedef struct s_honshu t_hs;

/**
 *	\struct s_afficheur
 *
 *	structure principal de l'afficheur
 */
typedef struct s_afficheur t_afficheur;

/**
 *	\brief initialise l'afficheur
 *	\param hs : l'instance du jeu
 *	\return un nouvel afficheur (ou NULL)
 */
t_afficheur * afficheur_new(t_hs * hs);


/**
 *	\brief de-initialise l'afficheur
 *	\param afficheur : l'instance de l'afficheur
 */
void afficheur_delete(t_afficheur * afficheur);

/**
 *	\brief affiche l'état actuel du jeu
 *	\param honshu : l'instance du jeu
 */
void afficher(t_hs * hs);

#endif
