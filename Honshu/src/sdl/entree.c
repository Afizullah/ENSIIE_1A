# include "honshu.h"
# include "sdl/sdl.h"

/**
 *	\struct s_entreur
 *
 *	structure principal de l'afficheur
 */
struct s_entreur {
	/** position X de la souris sur l'écran */
	int mouseX;
	/** position Y de la souris sur l'écran */
	int mouseY;
};

/**
 *	\brief initialise l'entreur
 *	\param hs : l'instance du jeu
 *	\return un nouvel entreur (ou NULL)
 */
t_entreur * entreur_new(t_hs * hs) {
	(void)hs;
	return (NULL);
}

/**
 *	\brief de-initialise entreur
 *	\param entreur : l'instance de l'entreur
 */
void entreur_delete(t_entreur * entreur) {
	(void)entreur;
}

/**
 *	\brief met à jour les entrés utilisateurs
 *	\param honshu : l'instance du jeu
 */
void entrer(t_hs * hs) {
	SDL_GetMouseState(&(hs->entreur->mouseX), &(hs->entreur->mouseY));
	
	/* on lit les events */
	SDL_Event e;
	if (SDL_PollEvent(&e)) {
		/* test de fin */
		if (e.type == SDL_QUIT ||
				(e.type == SDL_KEYUP && e.key.keysym.sym == SDLK_ESCAPE)) {
			FLAG_UNSET(hs->flags, HS_RUNNING);
			return ;
		}
	} 
}
