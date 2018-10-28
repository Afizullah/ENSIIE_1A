#ifndef CASE_GRILLE_MEMO_H
#define CASE_GRILLE_MEMO_H
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include<time.h>

/* définition de l'état d'une case */
#define DEJA_TROUVE 1
#define CACHE 0
/* La structure case*/
typedef struct s_case{
    char* motif;
    int etat;
}	t_case ;

/* La structure grille */
typedef struct s_grille {
    t_case * cases;
    int taille;
}	t_grille;

/* La structure du jeu*/
typedef struct s_memo {
    t_grille grille;
    int count;
    int score[2];
    int tour;
}	t_memo;

/**************************************************/

int case_init(t_case* caze, char* contenu);
t_case* case_get(t_grille* grille, int numero);

/**************************************************/

int grille_init(t_grille* grille, int taille);
int grille_rand(t_grille* grille);
void grille_delete(t_grille* grille);

/**************************************************/

t_memo* memo_new(int );
void memo_delete(t_memo*);

/**************************************************/
int afficher_carte(t_grille*, int i);
void entrer(t_memo*);

#endif
