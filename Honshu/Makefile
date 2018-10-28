NAME 		= honshu.out
TESTS_NAME	= honshu.test.out

SRCDIR	= src
INCDIR	= includes
BINDIR	= bin
OBJDIR	= obj
TESTDIR	= tests
DOCDIR	= doc

MODE=standart
#MODE=ncurses

INCLUDES =	$(wildcard $(INCDIR)/*.h)

SOURCES	 =	$(wildcard $(SRCDIR)/*.c)\
		$(wildcard $(SRCDIR)/solveur/*.c)\
		$(SRCDIR)/$(MODE)/affichage.c\
		$(SRCDIR)/$(MODE)/entree.c

OBJECTS	=	$(SOURCES:$(SRCDIR)/%.c=$(OBJDIR)/%.o)

TARGET		= $(BINDIR)/$(NAME)

TESTS_SOURCES	= $(shell find $(TESTDIR) -name "*.c")
TESTS_OBJECTS	= $(TESTS_SOURCES:$(TESTDIR)/%.c=$(OBJDIR)/$(TESTDIR)/%.o)
TESTS_TARGET	= $(BINDIR)/$(TESTS_NAME)

INC	= -I $(INCDIR)
FLAGS	= -ansi -Wall -Wextra -Werror -g

all: $(MODE) $(TARGET)

standart:

ncurses:
	$(eval LIB=$(LIB) -lncurses)

sdl:
	$(eval LIB=$(LIB) $(shell sdl-config --cflags --libs))

$(TARGET): $(OBJECTS)
	$(CC) $(FLAGS) $(OBJECTS) -o $(TARGET) $(LIB)

$(OBJDIR)/%.o : $(SRCDIR)/%.c
	@-mkdir -p $(dir $@)
	$(CC) $(FLAGS) $(INC) -c $< -o $@

$(TESTS_OBJECTS): $(OBJDIR)/$(TESTDIR)/%.o : $(TESTDIR)/%.c
	@-mkdir -p $(dir $@)
	$(CC) $(FLAGS) $(INC) -c $< -o $@ 

doc:
	cd doc ; doxygen

tests: $(TESTS_TARGET)

$(TESTS_TARGET): $(OBJECTS) $(TESTS_OBJECTS)
	$(CC) $(FLAGS) $(filter-out obj/main.o, $(OBJECTS)) $(TESTS_OBJECTS) -o $(TESTS_TARGET) -lcunit

clean:
	rm -rf $(OBJECTS)
	rm -rf $(TESTS_OBJECTS)
	rm -rf $(TESTS_TARGET)

fclean: clean
	rm -rf $(TARGET)
	rm -rf $(OBJDIR)

re: fclean all

.PHONY: all test clean doc tests standart ncurses sdl
