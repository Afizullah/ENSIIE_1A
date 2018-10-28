# include "honshu.h"

/**
 * La position des cases sur une tuile, en fonction de son orientation
 * et par rapport à son origine (case 'en haut à gauche')
 * ex:  DECALAGE[NORD][0] == (0, 0)
 *	DECALAGE[NORD][1] == (1, 0)
 * \internal :	ce tableau fait le lien entre la rotation, et la position
 *		des terrains relativement à la case 'en haut à gauche'.
 *		Ce tableau global permettra de facilement implémenter les différentes
 *		fonction par rapport à la rotation de la tuile.
 *		Il reste à compléter les 3 rotations 'EAST', 'WEST', 'SOUTH',
 *		(voir tile.h)
 */
BYTE DECALAGE[4][6][2] = {
	/* NORD */
	{
		{0, 0}, /* 0 */
		{1, 0}, /* 1 */
		{0, 1}, /* 2 */
		{1, 1}, /* 3 */
		{0, 2}, /* 4 */
		{1, 2}  /* 5 */
	},

	/* EST */
	{
		{2, 0}, /* 0 */
		{2, 1}, /* 1 */
		{1, 0}, /* 2 */
		{1, 1}, /* 3 */
		{0, 0}, /* 4 */
		{0, 1}  /* 5 */
	},

	/* SUD */
	{
		{1, 2}, /* 0 */
		{0, 2}, /* 1 */
		{1, 1}, /* 2 */
		{0, 1}, /* 3 */
		{1, 0}, /* 4 */
		{0, 0}  /* 5 */
	},

	/* OUEST */
	{
		{0, 1}, /* 0 */
		{0, 0}, /* 1 */
		{1, 1}, /* 2 */
		{1, 0}, /* 3 */
		{2, 1}, /* 4 */
		{2, 0}  /* 5 */
	}
};

/**
 *	\brief Initialise la tuile
 *	\attention :	les cases sont initialisées à l'état 'TYPE_MAX'
 *			il faudra 'tuile_set_case' pour déterminer le
 *			type de case explcitement
 *	\see tuile_set_case
 */
void tuile_init(t_tuile* tuile) {
	tuile->rotation	= NORD;
	tuile->flags = 0;
	tuile->x = 0;
	tuile->y = 0;

	BYTE caseID;
	for (caseID = 0 ; caseID < 6 ; caseID++) {
		t_case * caz = tuile->cases + caseID;
		case_init(caz, tuile, TYPE_MAX);
	}
}

/**
 *	\brief met une case de la tuile à un état (P/F/L/V/R/U/Max)
 *	\param tuile : une tuile 
 *	\param t : le type de case à définir
 *	\param index : l'index de la case sur la tuile
 *	\return 1 si la case a été modifié, 0 sinon
 */
int tuile_set_case(t_tuile * tuile, t_case_type t, BYTE index) {
	if (index > 6) {
		return (0);
	}
	case_set(tuile->cases + index, t);
	return (1);
}

/**
 *	\brief effectue la rotation d'une tuile
 *	\param tuile : la tuile qui subit la rotation
 */
void tuile_rotation(t_tuile * tuile) {
	tuile->rotation = (tuile->rotation + 1) % 4;
}
