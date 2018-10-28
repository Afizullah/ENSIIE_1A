# Executables
OSTYPE = $(shell uname -s)
JAVAC = javac
JAVA = java
A2PS = a2ps
GHOSTVIEW = gv
DOCP = javadoc
ARCH = zip
PS2PDF = ps2pdf -dPDFX=true -sPAPERSIZE=a4
DATE = $(shell date +%Y-%m-%d)
# Exécution de commandes dans un nouveau terminal (changer en fct de l'OS)
TERM = xterm
# Options de compilation
#CFLAGS = -verbose
CFLAGS =
CLASSPATH=.

JAVAOPTIONS = --verbose

PROJECT=Chat_Client_Serveur
# nom du fichier d'impression
OUTPUT = $(PROJECT)
# nom du répertoire ou se situera la documentation
DOC = doc
# lien vers la doc en ligne du JDK
WEBLINK = "https://docs.oracle.com/javase/8/docs/api/"
# lien vers la doc locale du JDK
LOCALLINK = "file:///Users/davidroussel/Documents/docs/java/api/"
# nom de l'archive
ARCHIVE = $(PROJECT)
# format de l'archive pour la sauvegarde
ARCHFMT = zip
# Répertoire source
SRC = src
# Répertoire bin
BIN = bin
# Répertoire Listings
LISTDIR = listings
# Répertoire Archives
ARCHDIR = archives
# Répertoire Figures
FIGDIR = graphics
# noms des fichiers sources
MAIN = \
examples/RunExampleFrame \
examples/RunListFrame \
RunChatServer \
RunChatClient
SOURCES = $(SRC)/AbstractRunChat.java \
$(SRC)/RunChatClient.java \
$(SRC)/RunChatServer.java \
$(SRC)/chat/client/ChatClient.java \
$(SRC)/chat/client/package-info.java \
$(SRC)/chat/client/ServerHandler.java \
$(SRC)/chat/client/UserHandler.java \
$(SRC)/chat/Failure.java \
$(SRC)/chat/package-info.java \
$(SRC)/chat/server/ChatServer.java \
$(SRC)/chat/server/ClientHandler.java \
$(SRC)/chat/server/InputClient.java \
$(SRC)/chat/server/InputOutputClient.java \
$(SRC)/chat/server/package-info.java \
$(SRC)/chat/UserOutputType.java \
$(SRC)/chat/Vocabulary.java \
$(SRC)/examples/package-info.java \
$(SRC)/examples/RunExampleFrame.java \
$(SRC)/examples/RunListFrame.java \
$(SRC)/examples/RunnableExample.java \
$(SRC)/examples/TestMessageStream.java \
$(SRC)/examples/widgets/ExampleFrame.java \
$(SRC)/examples/widgets/ListExampleFrame.java \
$(SRC)/logger/LoggerFactory.java \
$(SRC)/logger/package-info.java \
$(SRC)/models/Message.java \
$(SRC)/models/NameSetListModel.java \
$(SRC)/models/AuthorListFilter.java \
$(SRC)/models/package-info.java \
$(SRC)/widgets/AbstractClientFrame.java \
$(SRC)/widgets/ClientFrame.java \
$(SRC)/widgets/ClientFrame2.java \
$(SRC)/widgets/package-info.java \
$(foreach name, $(MAIN), $(SRC)/$(name).java)

OTHER = readme.txt \
reponses.txt \
Sujet.pdf \
MAJ.pdf \
$(SRC)/examples/icons/add_user-16.png \
$(SRC)/examples/icons/add_user-32.png \
$(SRC)/examples/icons/bg_blue-16.png \
$(SRC)/examples/icons/bg_blue-32.png \
$(SRC)/examples/icons/bg_color-32.png \
$(SRC)/examples/icons/bg_red-16.png \
$(SRC)/examples/icons/bg_red-32.png \
$(SRC)/examples/icons/delete_sign-16.png \
$(SRC)/examples/icons/delete_sign-32.png \
$(SRC)/examples/icons/erase-16.png \
$(SRC)/examples/icons/erase-32.png \
$(SRC)/examples/icons/remove_user-16.png \
$(SRC)/examples/icons/remove_user-32.png \
$(SRC)/icons/cancel-16.png \
$(SRC)/icons/cancel-32.png \
$(SRC)/icons/clock-16.png \
$(SRC)/icons/clock-32.png \
$(SRC)/icons/delete_database-16.png \
$(SRC)/icons/delete_database-32.png \
$(SRC)/icons/disconnected-16.png \
$(SRC)/icons/disconnected-32.png \
$(SRC)/icons/erase-16.png \
$(SRC)/icons/erase-32.png \
$(SRC)/icons/erase2-16.png \
$(SRC)/icons/erase2-32.png \
$(SRC)/icons/filled_filter-16.png \
$(SRC)/icons/filled_filter-32.png \
$(SRC)/icons/gender_neutral_user-16.png \
$(SRC)/icons/gender_neutral_user-32.png \
$(SRC)/icons/logout-16.png \
$(SRC)/icons/logout-32.png \
$(SRC)/icons/remove_user-16.png \
$(SRC)/icons/remove_user-32.png \
$(SRC)/icons/select_all-16.png \
$(SRC)/icons/select_all-32.png \
$(SRC)/icons/sent-16.png \
$(SRC)/icons/sent-32.png

.PHONY : doc ps

# Les targets de compilation
# pour générer l'application
all : $(foreach name, $(MAIN), $(BIN)/$(name).class)

#règle de compilation générique
$(BIN)/%.class : $(SRC)/%.java
	$(JAVAC) -sourcepath $(SRC) -classpath $(BIN):$(CLASSPATH) -d $(BIN) $(CFLAGS) $<

# Edition des sources $(EDITOR) doit être une variable d'environnement
edit :
	$(EDITOR) $(SOURCES) Makefile &

# nettoyer le répertoire
clean :
	find bin/ -type f -name "*.class" -exec rm -f {} \;
	rm -rf *~ *.log* $(DOC)/* $(LISTDIR)/*

#realclean : clean
#	rm -f $(ARCHDIR)/*.$(ARCHFMT) 

# générer le listing
$(LISTDIR) : 
	mkdir $(LISTDIR)

ps : $(LISTDIR)
	$(A2PS) -2 --file-align=fill --line-numbers=1 --font-size=10 \
	--chars-per-line=100 --tabsize=4 --pretty-print \
	--highlight-level=heavy --prologue="gray" \
	-o$(LISTDIR)/$(OUTPUT).ps Makefile $(SOURCES)

pdf : ps 
	$(PS2PDF) $(LISTDIR)/$(OUTPUT).ps $(LISTDIR)/$(OUTPUT).pdf

# générer le listing lisible pour Gérard
bigps :
	$(A2PS) -1 --file-align=fill --line-numbers=1 --font-size=10 \
	--chars-per-line=100 --tabsize=4 --pretty-print \
	--highlight-level=heavy --prologue="gray" \
	-o$(LISTDIR)/$(OUTPUT).ps Makefile $(SOURCES)

bigpdf : bigps
	$(PS2PDF) $(LISTDIR)/$(OUTPUT).ps $(LISTDIR)/$(OUTPUT).pdf

# voir le listing
preview : ps
	$(GHOSTVIEW) $(LISTDIR)/$(OUTPUT); rm -f $(LISTDIR)/$(OUTPUT) $(LISTDIR)/$(OUTPUT)~

# générer la doc avec javadoc
doc : $(SOURCES)
	$(DOCP) -private -d $(DOC) -author -link $(LOCALLINK) $(SOURCES)
#	$(DOCP) -private -d $(DOC) -author -linkoffline $(WEBLINK) $(LOCALLINK) $(SOURCES)

# générer une archive de sauvegarde
$(ARCHDIR) : 
	mkdir $(ARCHDIR)

archive : pdf $(ARCHDIR)
	$(ARCH) $(ARCHDIR)/$(ARCHIVE).$(ARCHFMT) $(SOURCES) $(LISTDIR)/*.pdf \
	$(FIGDIR)/*.pdf $(OTHER) $(BIN) Makefile $(FIGDIR)/*.pdf

# exécution des programmes de test
run : all
	$(foreach name, $(MAIN), $(TERM) -e $(JAVA) -classpath $(BIN):$(CLASSPATH) $(name) $(JAVAOPTIONS) & )

# Lancement d'un serveur
runserver : all
	$(TERM)  -title server -e $(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatServer --noquit &

# Lancement d'un client console
runclient : all
	$(TERM) -title "Zebulon" -e $(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --name Zébulon &

# Lancement d'un client graphique version 1
rungui1 : all
	$(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --name Ténéphore --gui 1 

# Lancement d'un client graphique version 2
rungui2 : all
	$(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --verbose --name Zéphirine --gui 2 

# Lancement d'un serveur, puis de 2 clients (l'un console, l'autre graphique)		
rundemo : all
	$(TERM)  -title server -e $(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatServer & \
	sleep 10;
	$(TERM) -title "Zébulon" -e $(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --name Zebulon & \
	$(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --name Ténéphore --gui 1 & \
	$(JAVA) -classpath $(BIN):$(CLASSPATH) RunChatClient --name Anatole --gui 2;
