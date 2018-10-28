# include "honshu.h"

/**
 *	\brief initialise une case
 *	\param caze : la case
 *	\param tuile : la tuile parente de cette case.
 *	\param type : \see enum e_cases
 *	\internal :	definit le type de case (T_PLAINE, T_FORET ...)
 *			definit la couche à 0
 *			initialise les pointeurs à NULL
 */
void case_init(t_case * caze, t_tuile * tuile, BYTE type) {
	caze->type	= type;
	caze->tuile	= tuile;
	FLAG_RESET(caze->flags);
	caze->upper	= NULL;
	caze->under	= NULL;
}

/**
 *	\brief renvoie le caractère associé à un type de case
 *	\param type : une type de case
 *	\return le caractère associé
 */
char case_char(t_case_type type) {
	switch (type) {
		case TYPE_PLAINE:
			return ('P');
		case TYPE_FORET:
			return ('F');
		case TYPE_LAC:
			return ('L');
		case TYPE_VILLE:
			return ('V');
		case TYPE_RESSOURCE:
			return ('R');
		case TYPE_USINE:
			return ('U');
		default:
			return ('?');
	}
	return ('?');
}

/**
 * 	\brief renvoie le type associé à un caractère
 * 	\param charID : le caractère
 * 	\return le type associé au caractère
 */
t_case_type case_type(char charID) {
	switch (charID) {
		case 'P':
			return (TYPE_PLAINE);
		case 'F':
			return (TYPE_FORET);
		case 'L':
			return (TYPE_LAC);
		case 'V':
			return (TYPE_VILLE);
		case 'R':
			return (TYPE_RESSOURCE);
		case 'U':
			return (TYPE_USINE);
		default:
			return (TYPE_MAX);
	}
	return (TYPE_MAX);
}

/**
 *	\brief définit le type d'une face
 *	\param caze : une case
 *	\param type : le type
 */
void case_set(t_case * caze, BYTE type) {
	caze->type = type;
}


