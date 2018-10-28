# include "tests.h"

/** \internal test la fonction 'tuile_init' */
void test_tuile_init(void) {
	t_tuile tuiletest;
	tuile_init(&tuiletest);
	int i;
	for(i=0;i<6;i++)CU_ASSERT(tuiletest.cases[i].type==TYPE_MAX);
}

/** \internal test la fonction 'tuile_set_case' */
void test_tuile_set_case(void) {
	t_tuile tuiletest;
	int i ;
	i=tuile_set_case(&tuiletest,TYPE_PLAINE,0);
	CU_ASSERT(tuiletest.cases[0].type==TYPE_PLAINE&&i==1);
}

/** \internal : ajoute les tests à la suite */
void test_tuile(CU_pSuite suite) {
	CU_add_test(suite, "tuile_init", test_tuile_init);
	CU_add_test(suite, "tuile_set_case", test_tuile_set_case);
}

/** \internal : initialise la suite */
int test_init_tuile(void) {
	return (0);
}

/** \internal : deinitialise la suite */
int test_deinit_tuile(void) {
	return (0);
}

