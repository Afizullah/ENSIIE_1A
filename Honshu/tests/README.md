Les tests unitaires sont a ajoutés ici.

Tous les fichiers sources de ce dossier,
et du dossier '../src' seront compilés pour effectuer les tests.

Le main utilisé sera 'main.test.c' présent dans ce dossier.
Il faut donc ajouter les tests CUnit dans ce main.

Par convention, si un test porte sur une fonction du fichier 'src/honshu.c',
on écrira le test dans le fichier 'tests/honshu.test.c'

NB: Le Makefile compile toute la hierarchie "tests" dans le dossier "obj/tests"
