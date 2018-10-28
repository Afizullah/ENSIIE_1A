#ifndef TESTS_H
# define TESTS_H

# include <stdio.h>
# include <string.h>
# include "CUnit/CUnit.h"
# include "CUnit/Basic.h"
# include "honshu.h"


/** list.c tests */
void test_list(CU_pSuite suite);
int test_init_list(void);
int test_deinit_list(void);

/** honshu.c tests */
void test_honshu(CU_pSuite suite);
int test_init_honshu(void);
int test_deinit_honshu(void);	

/** case.c tests */
void test_case(CU_pSuite suite);
int test_init_case(void);
int test_deinit_case(void);

/** tuile.c tests */
void test_tuile(CU_pSuite suite);
int test_init_tuile(void);
int test_deinit_tuile(void);

/** grille.c tests */
void test_grille(CU_pSuite suite);
int test_init_grille(void);
int test_deinit_grille(void);

/** player.c tests */
void test_player(CU_pSuite suite);
int test_init_player(void);
int test_deinit_player(void);

#endif
