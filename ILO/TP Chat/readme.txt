Lancement du serveur : 

java RunChatServer <options>
options : 
	-v | --verbose
		pour afficher les messages de debug
	-p | --port <port>
		pour spécifier le port tcp à utiliser [par défaut 1394]
	-t | --timeout <timeout d'attente de la server socket en ms>
		pour spécifier le temps d'attente de la serverSocket en attente d'un
		client avant de terminer [par défaut 5000 ms]
	
Lancement du client

java RunChatClient <options>
options :
	-v | --verbose
		pour afficher les messages de debug
	-h | --host <nom ou adresse IP du serveur>
		pour sélectionner le serveur [par défaut localhost]
	-p | --port <port>
		pour spécifier le port tcp à utiliser [par défaut 1394]
	-n | --name <nom d'utilisateur>
		pour spécifier notre identifiant sur le serveur de chat [par défaut le 
		nom de login]
	-g | --gui <1 ou 2>
		pour lancer la version avec interface graphique