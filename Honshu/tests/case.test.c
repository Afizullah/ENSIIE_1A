# include "tests.h"

/** \internal test la fonction 'case_init' */
static void test_case_init(void) {
	t_case t1;
	case_init(&t1,NULL,TYPE_PLAINE);
	CU_ASSERT(t1.type==TYPE_PLAINE);
		
	case_init(&t1,NULL,TYPE_FORET);
	CU_ASSERT(t1.type==TYPE_FORET);

	case_init(&t1,NULL,TYPE_VILLE);
	CU_ASSERT(t1.type==TYPE_VILLE);
		
	case_init(&t1,NULL,TYPE_RESSOURCE);
	CU_ASSERT(t1.type==TYPE_RESSOURCE);
		
	case_init(&t1,NULL,TYPE_USINE);
	CU_ASSERT(t1.type==TYPE_USINE);

    case_init(&t1,NULL,TYPE_MAX);
	CU_ASSERT(t1.type==TYPE_MAX);	
		
	case_init(&t1,NULL,TYPE_LAC);
	CU_ASSERT(t1.type==TYPE_LAC);
		

}

/** \internal test la fonction 'case_char' */
static void test_case_char(void) {
	char x=case_char(TYPE_PLAINE);
	CU_ASSERT(x=='P');
		
	 x=case_char(TYPE_FORET);
    CU_ASSERT(x=='F');
		
	 x=case_char(TYPE_LAC);
	CU_ASSERT(x=='L');
		
	 x=case_char(TYPE_VILLE);
    CU_ASSERT(x=='V');
		
	 x=case_char(TYPE_RESSOURCE);
    CU_ASSERT(x=='R');
		
	 x=case_char(TYPE_USINE);
   CU_ASSERT(x=='U');

     x=case_char(TYPE_MAX);
    CU_ASSERT(x=='?');


}
/** \internal test la fonction 'case_set' */
static void test_case_set(void) {
	t_case t1;

	case_init(&t1,NULL,TYPE_PLAINE);
	case_set(&t1,TYPE_FORET);
	CU_ASSERT(t1.type==TYPE_FORET);

	case_set(&t1,TYPE_VILLE);
	CU_ASSERT(t1.type==TYPE_VILLE);
		
	case_set(&t1,TYPE_RESSOURCE);
	CU_ASSERT(t1.type==TYPE_RESSOURCE);
		
	case_set(&t1,TYPE_USINE);
	CU_ASSERT(t1.type==TYPE_USINE);

    case_set(&t1,TYPE_MAX);
	CU_ASSERT(t1.type==TYPE_MAX);

	case_set(&t1,TYPE_LAC);
	CU_ASSERT(t1.type==TYPE_LAC);



}

/** \internal : ajoute les tests à la suite */
void test_case(CU_pSuite suite) {
	CU_add_test(suite, "case_init", test_case_init);
	CU_add_test(suite, "case_char", test_case_char);
	CU_add_test(suite, "case_set", test_case_set);
}

/** \internal : initialise la suite */
int test_init_case(void) {
	return (0);
}

/** \internal : deinitialise la suite */
int test_deinit_case(void) {
	return (0);
}

