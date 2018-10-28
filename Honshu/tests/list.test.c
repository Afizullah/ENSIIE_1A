# include "tests.h"
# include "list.h"

/** \internal : test la fonction list_new */
static void test_list_new(void) {
	t_list * lst = list_new();
	CU_ASSERT(lst != NULL);
	CU_ASSERT(lst->size == 0);
	list_delete(lst);
}

/** \internal : test la fonction list_delete */
static void test_list_delete(void) {
	t_list * lst = list_new();
	list_delete(lst);
}

/** \internal : test la fonction list_add_head */
static void test_list_add_head(void) {
	t_list * lst = list_new();
	list_add_head(lst, "hello", strlen("hello") + 1);
	list_add_head(lst, "world", strlen("world") + 1);
	list_add_head(lst, "petit", strlen("petit") + 1);
	list_add_head(lst, "test",  strlen("test") + 1);
	CU_ASSERT(strcmp(list_get_head(lst), "test") == 0);
	CU_ASSERT(strcmp(list_get_tail(lst), "hello") == 0);
	list_delete(lst);
}

/** \internal : test la fonction list_add_tail */
static void test_list_add_tail(void) {
	t_list * lst = list_new();
	list_add_tail(lst, "hello", strlen("hello") + 1);
	list_add_tail(lst, "world", strlen("world") + 1);
	list_add_tail(lst, "petit", strlen("petit") + 1);
	list_add_tail(lst, "test",  strlen("test") + 1);
	CU_ASSERT(strcmp(list_get_tail(lst), "test") == 0);
	CU_ASSERT(strcmp(list_get_head(lst), "hello") == 0);
	list_delete(lst);
}


/** \internal : test la fonction list_remove_head */
static void test_list_remove_head(void) {
	t_list * lst = list_new();
	list_add_head(lst, "hello", strlen("hello") + 1);
	list_add_head(lst, "world", strlen("world") + 1);
	list_add_head(lst, "petit", strlen("petit") + 1);
	list_add_head(lst, "test",  strlen("test") + 1);
	
	CU_ASSERT(list_remove_head(lst) == 1);
	CU_ASSERT(strcmp(list_get_head(lst), "petit") == 0);
	
	CU_ASSERT(list_remove_head(lst) == 1);
	CU_ASSERT(strcmp(list_get_head(lst), "world") == 0);
	
	CU_ASSERT(list_remove_head(lst) == 1);
	CU_ASSERT(strcmp(list_get_head(lst), "hello") == 0);
	
	CU_ASSERT(list_remove_head(lst) == 1);
	CU_ASSERT(list_get_head(lst) == NULL);
	
	CU_ASSERT(list_remove_head(lst) == 0);

	list_delete(lst);
}

/** \internal : test la fonction list_remove_tail */
static void test_list_remove_tail(void) {

	t_list * lst = list_new();
	list_add_tail(lst, "hello", strlen("hello") + 1);
	list_add_tail(lst, "world", strlen("world") + 1);
	list_add_tail(lst, "petit", strlen("petit") + 1);
	list_add_tail(lst, "test",  strlen("test") + 1);
	
	CU_ASSERT(list_remove_tail(lst) == 1);
	CU_ASSERT(strcmp(list_get_tail(lst), "petit") == 0);
	
	CU_ASSERT(list_remove_tail(lst) == 1);
	CU_ASSERT(strcmp(list_get_tail(lst), "world") == 0);
	
	CU_ASSERT(list_remove_tail(lst) == 1);
	CU_ASSERT(strcmp(list_get_tail(lst), "hello") == 0);
	
	CU_ASSERT(list_remove_tail(lst) == 1);
	CU_ASSERT(list_get_tail(lst) == NULL);
	
	CU_ASSERT(list_remove_tail(lst) == 0);

	list_delete(lst);
}

/** \internal : test la fonction list_clear */
static void test_list_clear(void) {
	t_list * lst = list_new();
	list_add_tail(lst, "hello", strlen("hello") + 1);
	list_add_tail(lst, "world", strlen("world") + 1);
	list_add_tail(lst, "petit", strlen("petit") + 1);
	list_add_tail(lst, "test",  strlen("test") + 1);
	list_clear(lst);
	CU_ASSERT(lst->size == 0);
	list_delete(lst);
}


/** \internal : test la fonction list_map */
char buffer[100];
/** \internal : test la fonction list_map */
static void fmap(void const * value) {
	strcat(buffer, (const char *) value);
	strcat(buffer, ":");
}
/** \internal : test la fonction list_map */
static void test_list_map(void) {
	t_list * lst = list_new();
	list_add_tail(lst, "hello", strlen("hello") + 1);
	list_add_tail(lst, "world", strlen("world") + 1);
	list_add_tail(lst, "petit", strlen("petit") + 1);
	list_add_tail(lst, "test",  strlen("test") + 1);

	memset(buffer, 0, sizeof(buffer));
	list_map(lst, fmap);
	CU_ASSERT(strcmp(buffer, "hello:world:petit:test:") == 0);
	list_delete(lst);
}


/** \internal : test les macros LIST_ITERATE_* */
static void test_list_iterator(void) {
	t_list * lst = list_new();

	unsigned int x;
	x = 0; list_add_head(lst, &x, sizeof(unsigned int));
	x = 1; list_add_head(lst, &x, sizeof(unsigned int));
	x = 2; list_add_head(lst, &x, sizeof(unsigned int));
	x = 3; list_add_head(lst, &x, sizeof(unsigned int));
	x = 4; list_add_head(lst, &x, sizeof(unsigned int));
	x = 5; list_add_head(lst, &x, sizeof(unsigned int));
	x = 6; list_add_head(lst, &x, sizeof(unsigned int));
	CU_ASSERT(lst->size == 7);

	/** on fait la somme, et on retire le maillon qui contient la valeur '4' */
	unsigned int sum = 0;
	LIST_ITERATE_START(lst, unsigned int *, i) {
		sum += *i;
		if (*i == 4) {
			LIST_ITERATE_POP(lst, unsigned int *, i);
		}
	}
	LIST_ITERATE_STOP(lst, unsigned int *, i);

	CU_ASSERT(sum == 0 + 1 + 2 + 3 + 4 + 5 + 6);
	CU_ASSERT(lst->size == 6);

	/** on refait la sum (avec 4 retiré cette fois ci) */
	sum = 0;
	LIST_ITERATE_START(lst, unsigned int *, i) {
		sum += *i;
	}
	LIST_ITERATE_STOP(lst, unsigned int *, i);
	CU_ASSERT(sum == 0 + 1 + 2 + 3 + 0 + 5 + 6);


	/** on vide la liste avec un iterateur */
	LIST_ITERATE_START(lst, unsigned int *, i) {
		(void)i;
		LIST_ITERATE_POP(lst, unsigned int *, i);
	}
	LIST_ITERATE_STOP(lst, unsigned int *, i);

	CU_ASSERT(lst->size == 0);

	/** on teste de sommer sur la liste vide */
	sum = 0;
	LIST_ITERATE_START(lst, unsigned int *, i) {
		sum += *i;
	}
	LIST_ITERATE_STOP(lst, unsigned int *, i);
	CU_ASSERT(sum == 0);

	list_delete(lst);
}


/** \internal : ajoute les tests à la suite */
void test_list(CU_pSuite suite) {
	CU_add_test(suite, "list_new", test_list_new);
	CU_add_test(suite, "list_delete", test_list_delete);
	CU_add_test(suite, "list_add_head", test_list_add_head);
	CU_add_test(suite, "list_add_tail", test_list_add_tail);
	CU_add_test(suite, "list_remove_head", test_list_remove_head);
	CU_add_test(suite, "list_remove_tail", test_list_remove_tail);
	CU_add_test(suite, "list_clear", test_list_clear);
	CU_add_test(suite, "list_map", test_list_map);
	CU_add_test(suite, "list_iterator", test_list_iterator);
}

/** \internal : initialise la suite */
int test_init_list(void) {
	return (0);
}

/** \internal : deinitialise la suite */
int test_deinit_list(void) {
	return (0);
}

