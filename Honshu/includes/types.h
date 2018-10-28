/**
 * \file	includes/types.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Définit les types de variables
 */

#ifndef TYPES_H
# define TYPES_H

/** le type a utilisé pour les coordonnéees sur la grille */
# define INDEX unsigned int

/** le type a utilisé pour representé 1 octet non signé */
# ifndef BYTE
#	define BYTE unsigned char
# endif

# ifndef MIN
#	define MIN(X, Y) ((X) < (Y) ? (X) : (Y))
# endif

/*
typedef struct s_honshu t_honshu;
typedef struct s_grille t_grille;
typedef struct s_tuile t_tuile;
typedef struct s_case t_case;
*/
#endif
