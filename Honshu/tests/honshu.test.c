# include "tests.h"

/** \internal test la fonction 'hs_read' */
static void test_hs_read(void) {
	t_hs * honshu = hs_read("./bin/tuiles", "./bin/saves/partie1");
	CU_ASSERT_FATAL(honshu != NULL);
	CU_ASSERT(honshu->grille.tuiles != NULL);
	CU_ASSERT(honshu->grille.cases != NULL);
	hs_delete(honshu);
}

/** \internal test la fonction 'hs_read' */
static void test_hs_rand(void) {
	t_hs * honshu = hs_rand(32, 16);
	CU_ASSERT(honshu != NULL);
	CU_ASSERT(honshu->grille.tuiles != NULL);
	CU_ASSERT(honshu->grille.cases != NULL);
	hs_delete(honshu);
}

/** \internal test la fonction 'hs_delete' */
static void test_hs_delete(void) {
	t_hs * honshu = hs_read("./bin/tuiles", "./bin/saves/partie1");
	hs_delete(honshu);
}

/** \internal : ajoute les tests à la suite */
void test_honshu(CU_pSuite suite) {
	CU_add_test(suite, "hs_read", test_hs_read);
	CU_add_test(suite, "hs_rand", test_hs_rand);
	CU_add_test(suite, "hs_delete", test_hs_delete);
}

/** \internal : initialise la suite */
int test_init_honshu(void) {
	return (0);
}

/** \internal : deinitialise la suite */
int test_deinit_honshu(void) {
	return (0);
}

