all: main

main: case_grille_memo.o main.o
	gcc $^ -o $@
%.o: %.c
	gcc -Wall -Wextra $< -c -g

case_grille_memo.o: case_grille_memo.h
main.o: case_grille_memo.h
clean:
	rm *.o main *~ -fv *.gch
