/**
 * 	\file		includes/list.h
 * 	\authors	Romain PEREIRA, Douha OURIMI, Afizullah RAHMANY, GuangYue CHEN
 * 	\brief		Listes doublement chainées circulaire
 *
 *	Data structure : liste doublement chainée circulaire
 *	(Peut être utilisé comme des listes/files/piles sans pertes de performances)
 */

#ifndef LIST_H
# define LIST_H

# include <stdio.h>
# include <stdlib.h>
# include <string.h>
# include <unistd.h>

/**
 *	\struct s_list_node
 *
 *	Un sommet de la liste
 */
typedef struct  s_list_node {
	/** sommet précèdement */
	struct s_list_node * next;
	/** sommet suivant */
	struct s_list_node * prev;
}               t_list_node;

/**
 *	\struct s_list
 *
 *	Une liste
 */
typedef struct  s_list {
	/** sommet central de la liste (ne contient aucunes données) */
	t_list_node         * main;
	/** nombre d'élément inséré dans la liste */
	unsigned long int   size;
}               t_list;

/**
 *	\brief Recuperes la donnée sur un sommet
 *	\param node : un sommet
 *	\return la donnée enregistré sur le sommet
 */
void * list_node_data(t_list_node * node);
/**
 *	\brief crée une nouvelle liste doublent chainée circulaire
 *	\return une nouvelle liste, ou NULL si erreur
 */
t_list * list_new(void);

/**
 *	\brief Ajoutes un élement en tête de liste
 *	\param lst : une liste
 *	\param content : la donnée à ajouter
 *	\param content_size : la taille de la donnée
 *	\return : l'adresse où la donnée a été copié
 */
void * list_add_head(t_list * lst, void const * content, unsigned int content_size);

/**
 *	\brief Ajoutes un élement en in liste
 *	\param lst : une liste
 *	\param content : la donnée à ajouter
 *	\param content_size : la taille de la donnée
 *	\return : l'adresse où la donnée a été copié
 */
void * list_add_tail(t_list * lst, void const * content, unsigned int content_size);

/**
 *	\brief Recuperes la donnée en tête de liste
 *	\param lst : une liste
 *	\return La donnée en tête de liste
 */
void * list_get_head(t_list * lst);
/**
 *	\brief Recuperes la donnée en fin de liste
 *	\param lst : une liste
 *	\return La donnée en fin de liste
 */
void * list_get_tail(t_list * lst);
/**
 *	\brief Supprimes un sommet de la liste
 *	\param lst : une liste
 *	\param node : un sommet de la liste
 */
void list_remove_node(t_list * lst, t_list_node * node);

/**
 *	\brief Supprimes l'élément en tête de liste
 *	\param lst : une liste
 *	\return 1 si succès, 0 sinon
 */
int list_remove_head(t_list * lst);

/**
 *	\brief Supprimes l'élément en fin de liste
 *	\param lst : une liste
 *	\return 1 si succès, 0 sinon
 */
int list_remove_tail(t_list * lst);
/**
 *	\brief Appliques une fonction sur chaque donnée de la liste
 *	\param lst : une liste
 *	\param f : une fonction
 *	\return 1 si succès, 0 sinon
 */
void list_map(t_list * lst, void (*f)(void const * value));

/**
 *	\brief Supprimes tous les éléments de la liste
 *	\param lst : une liste
 */
void list_clear(t_list * lst);

/**
 *	\brief Libères la mémoire dedié à la liste
 *	\param lst : une liste
 */
void list_delete(t_list * lst);

/**
 *	Une macro pour itérer dans la liste
 *
 *	exemple:
 *
 *	t_list * lst = list_new();
 *	list_add(lst, "hello", strlen("hello"));
 *	list_add(lst, "world", strlen("world"));
 *	list_add(lst, "I am toss", strlen("I am toss"));
 *
 *	LIST_ITERATE_START(lst, char *, string) {
 *		printf("%s\n", string);
 *	}
 *	LIST_ITERATE_STOP(lst, char *, string);
 */
# define LIST_ITERATE_START(L, T, V)\
	if (L != NULL) {\
		t_list_node * __node = L->main->next;\
		while (__node != L->main) {\
			T V = (T)(__node + 1);
# define LIST_ITERATE_STOP(L, T, V)\
			__node = __node->next;\
		}\
	}

/** Une macro qui peut être appelé lorsqu'on itére dans une liste
 *  (entre 2 macro 'LIST_ITERATE_START' et 'LIST_ITERATE_STOP'),
 *  pour supprimer de la liste le sommet courant.
 *  (opération en O(1))
 */
# define LIST_ITERATE_POP(L, T, V)\
			{\
				t_list_node * __next = __node->next;\
				list_remove_node(L, __node);\
				__node = __next;\
				continue ; \
			}


#endif
