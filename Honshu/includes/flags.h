/**
 * \file	includes/flags.h
 * \authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * \brief	Des defines pour travailler avec des 'bit flags'
 */

# ifndef FLAGS_H
#  define FLAGS_H

/**
 *	reinitialise les flags
 */
# define FLAG_RESET(X) X = 0

/**
 *	renvoie vrai si le flag est vrai
 */
# define FLAG_ISSET(X, F) (X & (F))

/**
 *	définie un flag sur vrai
 */
# define FLAG_SET(X, F) X |= (F)

/**
 *	définie un flag sur faux
 */
# define FLAG_UNSET(X, F) X &= ~(F)

/**
 *	si le flag est vrai, il est mis sur faux
 *	si le flag est faux, il est mis sur vrai
 */
# define FLAG_SWITCH(X, F) X ^= (F)

# endif
