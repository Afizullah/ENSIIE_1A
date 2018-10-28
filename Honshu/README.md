# Dépot du projet d'informatique pour l'équipe formée de:

Douha OURIMI

GuangYue CHEN

Afizullah RAHMANY

Romain PEREIRA

----------

Pour compiler et lancer le projet:
```
git clone git@gitlab.com:ENSIIEDrag/Honshu.git
cd Honshu
make
./bin/honshu.out
```

----------

Pour lancer les tests unitaires:
```
make tests
./bin/honshu.test.out
```

En cas d'erreur, assurez vous d'avoir les dépendances installer (CUnit):

### Installer les dépendances:
Liste des dépendances:
- make
- doxygen
- cunit

Pour installer toutes les dépendances automatiquement (Linux ou MacOSX):
```
sudo ./configure.sh --install
```

----------

### Règle du Makefile:
Compiler avec l'interface graphique:
```
make MODE=ncurses
```

Pour regénérer la documentation:
```
make doc
```

Recompile tous les fichiers qui ne sont pas à jour,
puis lie les '.o' vers le programme 'bin/honshu.out'
```
make {all}
```

Supprime les fichiers compilés, sauf les executables:
```
make clean
```

Supprime tous les fichiers compilés:
```
make fclean
```

Compiles les tests unitaires:
```
make tests
```

