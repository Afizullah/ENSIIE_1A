#include "honshu.h"
#include "sdl/sdl.h"

t_afficheur * afficheur_new(t_hs * hs){
	(void)hs;
	SDL_Init(SDL_INIT_VIDEO);

	SDL_Window * window = SDL_CreateWindow(
			"An SDL2 window",              
			SDL_WINDOWPOS_UNDEFINED,
			SDL_WINDOWPOS_UNDEFINED,          
			640,                               
			480,                               
			SDL_WINDOW_OPENGL                  
			);
    SDL_DestroyWindow(window);
	return (NULL);
}

void afficheur_delete(t_afficheur * afficheur) {
	BYTE i;
	for (i = 0 ; i < TYPE_MAX ; i++) {
		SDL_FreeSurface(afficheur->textures[i]);
	}
	SDL_DestroyRenderer(afficheur->renderer);
	SDL_DestroyWindow(afficheur->win);
}

void afficher(t_hs * hs) {
	(void)hs;
}


