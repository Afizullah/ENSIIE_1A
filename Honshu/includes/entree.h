/**
 * \file	includes/entree.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Fonctions relative aux entrées claviers
 */

#ifndef ENTREE_H
# define ENTREE_H

# include "honshu.h"

/** \internal : on signale que la structure existe */
typedef struct s_honshu t_hs;

/**
 *	\struct s_entreur
 *
 *	structure principal de l'afficheur
 */
typedef struct s_entreur t_entreur;

/**
 *	\brief initialise l'entreur
 *	\param hs : l'instance du jeu
 *	\return un nouvel entreur (ou NULL)
 */
t_entreur * entreur_new(t_hs * hs);

/**
 *	\brief de-initialise entreur
 *	\param entreur : l'instance de l'entreur
 */
void entreur_delete(t_entreur * entreur);

/**
 *	\brief met à jour les entrés utilisateurs
 *	\param honshu : l'instance du jeu
 */
void entrer(t_hs * hs);

#endif
