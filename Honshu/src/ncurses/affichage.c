# include "honshu.h"
# include "ncurses/entree.h"
# include <ncurses.h>

# define WELCOME_MESSAGE ("< Appuyer sur espace pour démarrer >")
# define WELCOME_PARTICLES (96)
# define TUILES_TITLE ("< Les tuiles restantes >")
# define TUILES_X (6)
# define TUILES_Y (6)
# define GRILLE_TITLE ("< Grille >")
# define GRILLE_X (24)
# define GRILLE_Y (TUILES_Y)
# define GRILLE_EMPTY ('.')

struct s_afficheur{
};

/**
 *	\internal : affiche l'animation de départ
 */
static void afficheur_start(t_hs * hs) {
	(void)hs;

	int x = COLS / 2 - 42;
	int y = LINES / 2 - 8;


	struct s_particle {
		float x, y;
		float vx, vy;
	};
	struct s_particle * particles = (struct s_particle *) malloc(sizeof(struct s_particle) * WELCOME_PARTICLES);
	unsigned int i;
	srand(time(NULL));
	for (i = 0 ; i < WELCOME_PARTICLES ; i++) {
		struct s_particle * particle = particles + i;
		particle->x = COLS / 2;
		particle->y = LINES / 2;
		particle->vx = ((rand() % 32) - 16) / 16.0f * 4.0f;
		particle->vy = ((rand() % 32) - 16) / 16.0f * 2.0f;
	}

	int c = 0;
	unsigned int j = 0;
	while (c <= 0) {
		c = getch();

		clear();

		attron(A_BOLD);
		for (i = 0 ; i < WELCOME_PARTICLES ; i++) {
			struct s_particle * particle = particles + i;
			particle->x += particle->vx;
			particle->y += particle->vy;
			if (particle->x < 0 || particle->x >= COLS || particle->y < 0 || particle->y >= LINES) {
				particle->x = COLS / 2;
				particle->y = LINES / 2;
				particle->vx = ((rand() % 32) - 16) / 16.0f;
				particle->vy = ((rand() % 32) - 16) / 16.0f;
			}
			mvprintw(particle->y, particle->x, "*");
		}
		attroff(A_BOLD);



		switch ((j / 8) % 4) {
			case 0:
				mvprintw(y +  0, x, "      ___           ___           ___           ___           ___           ___     ");
				mvprintw(y +  1, x, "     /\\__\\         /\\  \\         /\\__\\         /\\  \\         /\\__\\         /\\__\\    ");
				mvprintw(y +  2, x, "    /:/  /        /::\\  \\       /::|  |       /::\\  \\       /:/  /        /:/  /    ");
				mvprintw(y +  3, x, "   /:/__/        /:/\\:\\  \\     /:|:|  |      /:/\\ \\  \\     /:/__/        /:/  /     ");
				mvprintw(y +  4, x, "  /::\\  \\ ___   /:/  \\:\\  \\   /:/|:|  |__   _\\:\\~\\ \\  \\   /::\\  \\ ___   /:/  /  ___ ");
				mvprintw(y +  5, x, " /:/\\:\\  /\\__\\ /:/__/ \\:\\__\\ /:/ |:| /\\__\\ /\\ \\:\\ \\ \\__\\ /:/\\:\\  /\\__\\ /:/__/  /\\__\\");
				mvprintw(y +  6, x, " \\/__\\:\\/:/  / \\:\\  \\ /:/  / \\/__|:|/:/  / \\:\\ \\:\\ \\/__/ \\/__\\:\\/:/  / \\:\\  \\ /:/  /");
				mvprintw(y +  7, x, "      \\::/  /   \\:\\  /:/  /      |:/:/  /   \\:\\ \\:\\__\\        \\::/  /   \\:\\  /:/  / ");
				mvprintw(y +  8, x, "      /:/  /     \\:\\/:/  /       |::/  /     \\:\\/:/  /        /:/  /     \\:\\/:/  /  ");
				mvprintw(y +  9, x, "     /:/  /       \\::/  /        /:/  /       \\::/  /        /:/  /       \\::/  /   ");
				mvprintw(y + 10, x, "     \\/__/         \\/__/         \\/__/         \\/__/         \\/__/         \\/__/    ");
				break ;
			case 1:
				mvprintw(y +  0, x, "      ___           ___           ___           ___           ___           ___     ");
				mvprintw(y +  1, x, "     /\\  \\         /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\  \\    ");
				mvprintw(y +  2, x, "     \\:\\  \\       /::\\  \\        \\:\\  \\       /:/ _/_        \\:\\  \\        \\:\\  \\   ");
				mvprintw(y +  3, x, "      \\:\\  \\     /:/\\:\\  \\        \\:\\  \\     /:/ /\\  \\        \\:\\  \\        \\:\\  \\  ");
 				mvprintw(y +  4, x, "  ___ /::\\  \\   /:/  \\:\\  \\   _____\\:\\  \\   /:/ /::\\  \\   ___ /::\\  \\   ___  \\:\\  \\ ");
 				mvprintw(y +  5, x, " /\\  /:/\\:\\__\\ /:/__/ \\:\\__\\ /::::::::\\__\\ /:/_/:/\\:\\__\\ /\\  /:/\\:\\__\\ /\\  \\  \\:\\__\\");
 				mvprintw(y +  6, x, " \\:\\/:/  \\/__/ \\:\\  \\ /:/  / \\:\\~~\\~~\\/__/ \\:\\/:/ /:/  / \\:\\/:/  \\/__/ \\:\\  \\ /:/  /");
				mvprintw(y +  7, x, "  \\::/__/       \\:\\  /:/  /   \\:\\  \\        \\::/ /:/  /   \\::/__/       \\:\\  /:/  / ");
				mvprintw(y +  8, x, "   \\:\\  \\        \\:\\/:/  /     \\:\\  \\        \\/_/:/  /     \\:\\  \\        \\:\\/:/  /  ");
				mvprintw(y +  9, x, "    \\:\\__\\        \\::/  /       \\:\\__\\         /:/  /       \\:\\__\\        \\::/  /   ");
				mvprintw(y + 10, x, "     \\/__/         \\/__/         \\/__/         \\/__/         \\/__/         \\/__/    ");
				break ;

			case 2:
				mvprintw(y +  0, x, "      ___           ___           ___           ___           ___           ___     ");
				mvprintw(y +  1, x, "     /  /\\         /  /\\         /  /\\         /  /\\         /  /\\         /  /\\    ");
				mvprintw(y +  2, x, "    /  /:/        /  /::\\       /  /::|       /  /::\\       /  /:/        /  /:/    ");
				mvprintw(y +  3, x, "   /  /:/        /  /:/\\:\\     /  /:|:|      /__/:/\\:\\     /  /:/        /  /:/     ");
				mvprintw(y +  4, x, "  /  /::\\ ___   /  /:/  \\:\\   /  /:/|:|__   _\\_ \\:\\ \\:\\   /  /::\\ ___   /  /:/      ");
				mvprintw(y +  5, x, " /__/:/\\:\\  /\\ /__/:/ \\__\\:\\ /__/:/ |:| /\\ /__/\\ \\:\\ \\:\\ /__/:/\\:\\  /\\ /__/:/     /\\");
				mvprintw(y +  6, x, " \\__\\/  \\:\\/:/ \\  \\:\\ /  /:/ \\__\\/  |:|/:/ \\  \\:\\ \\:\\_\\/ \\__\\/  \\:\\/:/ \\  \\:\\    /:/");
				mvprintw(y +  7, x, "      \\__\\::/   \\  \\:\\  /:/      |  |:/:/   \\  \\:\\_\\:\\        \\__\\::/   \\  \\:\\  /:/ ");
				mvprintw(y +  8, x, "      /  /:/     \\  \\:\\/:/       |__|::/     \\  \\:\\/:/        /  /:/     \\  \\:\\/:/  ");
				mvprintw(y +  9, x, "     /__/:/       \\  \\::/        /__/:/       \\  \\::/        /__/:/       \\  \\::/   ");
				mvprintw(y + 10, x, "     \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/    ");
				break ;


			case 3:
				mvprintw(y +  0, x, "      ___           ___           ___           ___           ___           ___     ");
				mvprintw(y +  1, x, "     /__/\\         /  /\\         /__/\\         /  /\\         /__/\\         /__/\\    ");
				mvprintw(y +  2, x, "     \\  \\:\\       /  /::\\        \\  \\:\\       /  /:/_        \\  \\:\\        \\  \\:\\   ");
				mvprintw(y +  3, x, "      \\__\\:\\     /  /:/\\:\\        \\  \\:\\     /  /:/ /\\        \\__\\:\\        \\  \\:\\  ");
				mvprintw(y +  4, x, "  ___ /  /::\\   /  /:/  \\:\\   _____\\__\\:\\   /  /:/ /::\\   ___ /  /::\\   ___  \\  \\:\\ ");
				mvprintw(y +  5, x, " /__/\\  /:/\\:\\ /__/:/ \\__\\:\\ /__/::::::::\\ /__/:/ /:/\\:\\ /__/\\  /:/\\:\\ /__/\\  \\__\\:\\");
				mvprintw(y +  6, x, " \\  \\:\\/:/__\\/ \\  \\:\\ /  /:/ \\  \\:\\~~\\~~\\/ \\  \\:\\/:/~/:/ \\  \\:\\/:/__\\/ \\  \\:\\ /  /:/");
				mvprintw(y +  7, x, "  \\  \\::/       \\  \\:\\  /:/   \\  \\:\\  ~~~   \\  \\::/ /:/   \\  \\::/       \\  \\:\\  /:/ ");
				mvprintw(y +  8, x, "   \\  \\:\\        \\  \\:\\/:/     \\  \\:\\        \\__\\/ /:/     \\  \\:\\        \\  \\:\\/:/  ");
				mvprintw(y +  9, x, "    \\  \\:\\        \\  \\::/       \\  \\:\\         /__/:/       \\  \\:\\        \\  \\::/   ");
				mvprintw(y + 10, x, "     \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/    ");
				break ;
		}



		if (j % 16 < 8) {
			attron(A_BOLD);
			mvprintw(y + 18, (COLS - strlen(WELCOME_MESSAGE)) / 2, WELCOME_MESSAGE);
			attroff(A_BOLD);
		}

		refresh();

		j = j + 1;
	}
	free(particles);
}

/**
 *	\brief initialise l'afficheur
 *	\param hs : l'instance du jeu
 *	\return un nouvel afficheur (ou NULL)
 */
t_afficheur * afficheur_new(t_hs * hs){
	(void)hs;
	/* initialize afficheur */
	t_afficheur * afficheur=(t_afficheur *)malloc(sizeof (t_afficheur));
	if(afficheur==NULL)  return(NULL);

	/** initialisation de ncurses */
	initscr();
	noecho();
	curs_set(0);
	keypad(stdscr, TRUE);
	timeout(35);
	if (has_colors()) {
		start_color();
		init_pair(1, COLOR_GREEN, COLOR_BLACK);
		init_pair(2, COLOR_RED, COLOR_BLACK);
		init_pair(3, COLOR_YELLOW, COLOR_BLACK);
	}
	afficheur_start(hs);
	getch();
	return (afficheur);
}


/**
 *	\brief de-initialise l'afficheur
 *	\param afficheur : l'instance de l'afficheur
 */
void afficheur_delete(t_afficheur * afficheur){
	free(afficheur);
	endwin();
}

/**
 *	\internal : affiche une tuile
 */
static void afficher_une_tuile(t_tuile * tuile, INDEX x, INDEX y){
	BYTE i ;
	INDEX nx,ny;
	for(i=0;i<6;i++) {
		INDEX dx = DECALAGE[tuile->rotation][i][0];
		INDEX dy = DECALAGE[tuile->rotation][i][1];
		nx = x + dx * 2;
		ny = y + dy;
		mvprintw(ny, nx, "%c", case_char(tuile->cases[i].type));
	}
}

/**
 *	\internal : affiche toutes les tuiles dans la main
 */
static void afficher_tuiles(t_hs * hs){
	t_grille * grille = &(hs->grille);

	INDEX i;
	for(i=0;i<grille->nb_tuiles;i++){
		t_tuile * tuile = &(grille->tuiles[i]);
		INDEX x = TUILES_X + i%2*7 ;
		INDEX y = TUILES_Y + i/2*4;
		if (i == hs->entreur->tuile) {
			attron(A_BOLD);
		}
		if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
			attron(COLOR_PAIR(2));
		}
		afficher_une_tuile(tuile,x,y);
		if (i == hs->entreur->tuile) {
			attroff(A_BOLD);
		}

		if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
			attroff(COLOR_PAIR(2));
		}
	}
}

/**
 *	\internal : affiche les titres
 */
static void afficher_titre(t_hs * hs){
	(void)hs;
	mvprintw(LINES -1, 0 ,   "< Honshu >");

	mvprintw(TUILES_Y - 4, TUILES_X - 5, "Score actuel : %d", grille_score(&(hs->grille)));

	attron(A_BOLD);
	mvprintw(GRILLE_Y - 2,
		GRILLE_X + (hs->grille.n * 2 - strlen(GRILLE_TITLE)) / 2 - 1,
		GRILLE_TITLE);

	mvprintw(TUILES_Y - 2, TUILES_X - 5, TUILES_TITLE);
	attroff(A_BOLD);

}

/**
 *	\internal : affiche les touches pour jouer
 */
static void afficher_touches(t_hs * hs) {
	int x = GRILLE_X + 2 * hs->grille.n + 1;
	int y = GRILLE_Y + hs->grille.n / 2;
	mvprintw(y - 6, x, "Espace : action");
	mvprintw(y - 4, x, "Flèches : déplacer la tuile");
	mvprintw(y - 2, x, "E : tuile suivante");
	mvprintw(y + 0, x, "A : tuile précèdente");
	mvprintw(y + 2, x, "R : rotation de la tuile");
}

/**
 *	\internal : affiche la grille
 */
static void  afficher_grille(t_hs * hs){
	t_grille * grille = &(hs->grille);
	t_case * cases;
	INDEX i,j;
	for(i=0;i<grille->n;i++){
		for(j=0;j<grille->n;j++){
			cases=grille_get(grille,i,j);
			char c = GRILLE_EMPTY;
			if (cases != NULL) {
				c = case_char(cases->type);
				if (cases->tuile == hs->grille.tuiles + hs->entreur->tuile) {
					attron(COLOR_PAIR(2));
				}
			}
			mvprintw(GRILLE_Y + j, GRILLE_X + i * 2, "%c ", c);
			attroff(COLOR_PAIR(2));

		}
	}
}

/**
 *	\internal : affiche la tuile selectionné
 */
static void afficher_selection(t_hs * hs) {
	t_grille * grille = &(hs->grille);
	t_tuile * tuile = grille->tuiles + hs->entreur->tuile;
	if (FLAG_ISSET(tuile->flags, TUILE_SUR_GRILLE)) {
		return ;
	}
	INDEX ix = hs->entreur->cursorX;
	INDEX iy = hs->entreur->cursorY;
	INDEX x = GRILLE_X + ix * 2;
	INDEX y = GRILLE_Y + iy;
	attron(COLOR_PAIR(grille_can_insert(&(hs->grille), tuile, ix, iy) ? 1 : 2));
	afficher_une_tuile(tuile, x, y);
	attroff(COLOR_PAIR(1) | COLOR_PAIR(2));
}

/**
 *	\brief affiche l'état actuel du jeu
 *	\param honshu : l'instance du jeu
 */
void afficher(t_hs * hs){

	clear();

	afficher_titre(hs);

	afficher_tuiles(hs);

	afficher_grille(hs);
	
	afficher_selection(hs);

	afficher_touches(hs);

	refresh();

}
