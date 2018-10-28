#include "case_grille_memo.h"
/* début de l'implémentation */

int case_init(t_case* caze, char* contenu){
    if (caze == NULL || contenu == NULL) return -1;
    caze->motif = contenu;
    caze->etat = CACHE;
    return 0;
}

t_case* case_get(t_grille* grille, int numero){
    if(numero < 0 || numero >= (grille->taille*grille->taille)) {
        return NULL;
    }
    return grille->cases + numero;
}

/**************************************************/

/*	alloue une grille de dimensions taille*taille* cases	*/
int grille_init(t_grille * grille, int taille){
    grille->taille=taille;
    grille->cases = (t_case*) malloc(taille*taille*sizeof(t_case));
    if (grille->cases==NULL) return -1;
    memset(grille->cases, 0, sizeof(t_case) * taille * taille);
    return 0;
}

/* Pour l'instant : une grille avec que des "bonjour" dedans*/

int grille_rand(t_grille * grille){
    int size = grille->taille;
    char buf[100];
    for (int i = 0; i<(size*size/2); i++){
        printf("Choisissez le motif numéro %d pour cette partie\n", i);
        scanf("%s",buf);
        //    srand(time(NULL));
        //    int indice = (rand() % ( grille->taille*grille->taille + 1));

        case_init(grille->cases + i, buf);
        case_init(grille->cases + i + size*size/2, buf);
    }
    return 0;
}

void grille_delete(t_grille* grille){
    for (int i = 0; i<grille->taille*grille->taille;i++){
        free(case_get(grille, i)->motif);
    }
    free(grille->cases);
    grille->cases= NULL;
}

/**************************************************/

t_memo * memo_new(int taille){
    t_memo* memo = (t_memo*) malloc(sizeof(t_memo));
    if (memo ==NULL){
        return NULL;
    }
    /*alloue la grille vierge*/
    if(grille_init(&(memo->grille), taille)==-1){
        free (memo);
        return (NULL);
    }
    /*prend les motifs en entrée et les réparti aléatoirement sur la grille*/
    grille_rand(&(memo->grille));
    memo->count = 0;
    memo->score[0] = 0;
    memo->score[1] = 0;
    memo->tour = 0;
    return memo;
}

void memo_delete(t_memo* memo){
    grille_delete(&(memo->grille));
    free(memo);
    memo = NULL;
}

/***************************************************/
/* affiche la ième carte de la grille, renvoie -1 si elle était deja trouvé ou qu'elle n'existe pas */
int afficher_carte(t_grille * grille, int i) {
    t_case * c = case_get(grille, i);
    if (c == NULL) {
        printf("La carte n'existe pas !\n");
        return -1;
    }
    if (c->etat == DEJA_TROUVE) {
        printf("La carte a déjà été trouvé\n");
        return -1;
    }
    printf("carte %d : %s\n", i, c->motif);
    return 0;
}


void entrer(t_memo * memo) {
    t_grille * grille = &(memo->grille);
    int joueur = memo->tour % 2;
    int i1, i2;
    printf("-----------------------------------------\n");
    printf("Joueur %d : à ton tour :)\n",joueur);
    printf("-----------------------------------------\n");
    do {
        printf("Choisissez une première carte à retourner (donner son indice)\n");
        scanf("%d",&i1);
    } while(afficher_carte(grille, i1)==-1);

    do {
        printf("Choisissez une deuxième carte à retourner (donner son indice)\n");
        scanf("%d", &i2);
        if(i2==i1){
        printf("Vous ne pouvez pas sélectionner 2 fois la même carte\n");
        }
    } while((afficher_carte(grille, i2)==-1) || i1==i2);

    t_case * c1 = case_get(grille, i1);
    t_case * c2 = case_get(grille, i2);
    if(strcmp(c1->motif, c2->motif)==0) {
        printf("Les deux cartes sont identiques, le joueur %d gagne 1 point (score du joueur %d : %d)\n",joueur, joueur, memo->score[joueur]);
        c1->etat = DEJA_TROUVE;
        c2->etat = DEJA_TROUVE;
        memo->score[joueur]+=1;
        memo->count+=1;
    } else {
        c1->etat = CACHE;
        c2->etat = CACHE;
        printf("Les deux cartes sont différente, le joueur %d a un score de : %d)\n", joueur, memo->score[joueur]);
    }
    memo->tour+=1;
}
