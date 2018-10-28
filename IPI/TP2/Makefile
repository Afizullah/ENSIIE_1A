all: main main2

main: liste.o main.o
	gcc $^ -o $@
main2: liste2.o main.o
	gcc $^ -o $@
%.o: %.c
	gcc -Wall -Wextra $^ -c -g

liste.o: liste.h
liste2.o: liste.h
main.o: liste.h

clean:
	rm *.o main main2 *~ -fv *.gch
