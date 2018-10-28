#include "case_grille_memo.h"

int main (void){
    t_memo* memo = NULL;
    printf("\tChoisissez la taille de la grille pour cette partie \n\n");
    int taille;
    do {
        printf("La taille doit être PAIRE et entre 4 et 20)\n\n");
        scanf("%d",&taille);

    } while ( (taille%2)!=0||taille < 3 || taille  >20 );

    printf("\tVous jouerez avec une grille de taille %d et il y aura %d motifs différents à trouver.\n\n",taille, taille*taille/2);
    memo = memo_new(taille);

    if(memo == NULL){
        printf("memo_new : la mémoire n'a pas pu être alloué");
        return EXIT_FAILURE;
    }
    /*	grille* tuiles = malloc(taille*sizeof(case));
        grille_init(jeu,taille);
        grille_init(tuiles,taille):*/
    printf("\tDébut de la partie :)\n");

    while(memo->count < (taille*taille/2)){
        entrer(memo);
    }
    if (memo->score[1]>memo->score[0]) {
        printf("Le joueur 1 remporte la partie !\n");
        return 0;
    }

    if (memo->score[1]>memo->score[0]) {
        printf("Le joueur 0 remporte la partie !\n");
        return 0;
    }

    if (memo->score[1]==memo->score[0]) {
        printf("C'est un match nul !\n");
        return 0;
    }
    /* On libère la mémoire une fois que la partie est fini*/
    memo_delete(memo);
    return EXIT_SUCCESS;    
}
