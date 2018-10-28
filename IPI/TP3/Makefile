all: main

main: arbre.o main.o
	gcc $^ -o $@
%.o: %.c
	gcc -Wall -Wextra $< -c -g

arbre.o: arbre.h
main.o: arbre.h

clean:
	rm *.o main *~ -fv *.gch
