package chat.server;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import chat.Vocabulary;
import logger.LoggerFactory;
import models.Message;

/**
 * Class designed to handle each client's messages in a thread.
 * @author davidroussel
 */
public class ClientHandler implements Runnable
{
	/**
	 * The parent {@link ChatServer} which launched this handler
	 */
	private ChatServer parent;

	/**
	 * This handler main client
	 */
	private InputClient mainClient;

	/**
	 * Other clients connected to the server (in order to broadcast any message
	 * of the main client to all clients (including the main client))
	 */
	private Vector<InputOutputClient> allClients;

	/**
	 * Index of the main client among all clients (in order to send special
	 * messages to this client only, during a "catchup" request for instance)
	 */
	private int clientIndex;

	/**
	 * Threads (or ClientHandler) instance counter.
	 * Used to determine the number of remaining connected clients
	 * when run loop ends and {@link ChatServer#cleanup()} is triggered
	 */
	private static int nbThreads = 0;

	/**
	 * Logger used to display info or debug messages
	 */
	private Logger logger;

	/**
	 * Constructor
	 * @param parent the {@link ChatServer} launching this client handler
	 * @param mainClient the main client to listen to
	 * @param allClients all clients list to broadcast messages from the main
	 * client to
	 */
	public ClientHandler(ChatServer parent,
	                     InputClient mainClient,
	                     Vector<InputOutputClient> allClients,
	                     Logger parentLogger)
	{
		this.parent = parent;
		this.mainClient = mainClient;
		this.allClients = allClients;
		nbThreads++;
		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       parentLogger.getLevel());


		synchronized (allClients)
		{
			// Search for this client in allClients
			clientIndex = -1;
			int index = 0;
			for (InputOutputClient client : allClients)
			{
				if (client == mainClient)
				{
					break;
				}
				index++;
			}
			if (index < allClients.size())
			{
				clientIndex = index;
			}

			// send message "<ClientName> logged in" to all other clients
			Message m = new Message(mainClient.getName() + " logged in");

			for (InputOutputClient client : allClients)
			{
				if (client != mainClient)
				{
					if (client.isReady())
					{
						ObjectOutputStream out = client.getOut();
						try
						{
							out.writeObject(m);
						}
						catch (InvalidClassException ice)
						{
							logger.severe("ClientHandler["
							    + mainClient.getName() + "]: write "
							    + m.toString() + " to client invalid class "
							    + ice.getLocalizedMessage());
						}
						catch (NotSerializableException nse)
						{
							logger
							    .severe("ClientHandler[" + mainClient.getName()
							        + "]: write " + m.toString()
							        + " with not serializable exception "
							        + nse.getLocalizedMessage());
						}
						catch (IOException e)
						{
							logger
							    .severe("ClientHandler[" + mainClient.getName()
							        + "]: write " + m.toString() + " failed");

						}
					}
				}
			}

			/*
			 * This specific message may not be recorded in parent's recorded
			 * messages
			 */
			// parent.addMessage(m);

		}
	}

	/**
	 * Threads counter accessor
	 * @return the number of clients threads
	 */
	public static int getNbThreads()
	{
		return nbThreads;
	}

	/**
	 * Client handler run loop: Read a new line from main client. Process the
	 * line (for special commands such as "bye" or "kick") and eventually
	 * boradcast the message to all clients if the main client is not banned
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		boolean loggedOut = false;
		boolean killed = false;
		boolean catchup = false;
		String clientInput = null;


		try
		{
			/*
			 * Wait for a new line from client
			 */
			while (!loggedOut && !killed &&
			       ((clientInput = mainClient.getIn().readLine()) != null))
			{
				// Display the line on the console
				System.out.println(mainClient.getName() + " > " + clientInput);

				// Check if main client is banned
				if (mainClient.isBanned())
				{
					logger.info(mainClient.getName() + " is banned");
					loggedOut = true;
					break;
				}

				// Check for control messages (kick, bye, ...)
				boolean controlMessage = false;
				for (String command : Vocabulary.commands)
				{
					if (clientInput.toLowerCase().startsWith(command))
					{
						controlMessage = true;
						break;
					}
				}

				StringBuffer messageContent = new StringBuffer();

				if (controlMessage)
				{
					// Check if client wants to quit
					if (clientInput.toLowerCase().equals(Vocabulary.byeCmd))
					{
						messageContent.append(mainClient.getName() +
						                      " logged out");
						loggedOut = true;
					}
					// Check if client requested a server kill
					else if (clientInput.toLowerCase().startsWith(Vocabulary.killCmd))
					{
						// Only allowed if main client is first client (super user)
						if (allClients.get(0) == mainClient)
						{
							killed = true;
							parent.setListening(false);
							break;
						}
					}
					// Checks if client requested a kick
					else if (clientInput.toLowerCase().startsWith(Vocabulary.kickCmd))
					{
						messageContent.append(Vocabulary.kickCmd);
						// Atomic access to all clients during request processing
						synchronized (allClients)
						{
							// Only allowed if main client is first client
							if (allClients.get(0) == mainClient)
							{
								// Search for client to kick
								String kickedName = null;
								try
								{
									kickedName = clientInput.substring(
										Vocabulary.kickCmd.length() + 1);
								}
								catch (IndexOutOfBoundsException iob)
								{
									logger.warning("ClientHandler: Error retreiving client name to kick");
								}
								if (kickedName != null)
								{
									messageContent.append(" " + kickedName);
									InputOutputClient kickedClient =
										parent.searchClientByName(kickedName);
									if (kickedClient != null)
									{
										kickedClient.setBanned(true);
										logger.info("Clienthandler["
											+ mainClient.getName() + "] client "
											+ kickedName + " banned");
										messageContent.append(" [request granted by server]");
									}
									else
									{
										messageContent.append(" [client "
											+ kickedName + " does not exist]");
									}
								}
								else
								{
									messageContent.append(" [no client name to kick]");
								}
							}
							else
							{
								int cmdL = Vocabulary.kickCmd.length();
								messageContent.append(clientInput.substring(cmdL, (clientInput.length())));
								messageContent.append(" [request denied by server]");
							}
							messageContent.append(" by " + mainClient.getName());
						}
					}
					else if(clientInput.toLowerCase().startsWith(Vocabulary.catchUpCmd))
					{
						catchup = true;
					}
				}
				else
				{
					// regular message
					messageContent.append(clientInput);
				}

				/*
				 * Creates the message to broadcast
				 */
				Message message = null;

				if (!catchup)
				{
					if (controlMessage)
					{
						message = new Message(messageContent.toString());
					}
					else
					{
						message = new Message(messageContent.toString(),
						                      mainClient.getName());
					}

					/*
					 * DONE Add this message to parent
					 */
					parent.addMessage(message);

					/*
					 * Message broadcast to all clients in an allClients
					 * synchronized block to avoid any modification to
					 * this list during the broadcast
					 */
					synchronized (allClients)
					{
						for (InputOutputClient c : allClients)
						{
							if (c.isReady())
							{
								// get client output stream and send message object
								ObjectOutputStream out = c.getOut();
								out.writeObject(message);
							}
							else
							{
								logger.warning("ClientHandler["
										+ mainClient.getName() + "]Client "
										+ c.getName() + " not ready");
							}
						}
					}
				}
				else // catchup : resend all stored messages to main client
				{
					synchronized (allClients)
					{
						if ((clientIndex != -1) &&
							(clientIndex < allClients.size()))
						{
							InputOutputClient client =
							    allClients.get(clientIndex);
							if (client.isReady())
							{
								ObjectOutputStream out = client.getOut();
								Iterator<Message> itm = parent.messages();
								while (itm.hasNext())
								{
									out.writeObject(itm.next());
								}
							}
						}
						else
						{
							logger.warning("ClientHandler["
											+ mainClient.getName()
											+ "] invalid index : "
											+ String.valueOf(clientIndex));
						}
					}
					catchup = false;
				}
			}
		}
		catch (InvalidClassException ice)
		{
			logger.severe("ClientHandler["
				+ mainClient.getName() + "]: write to client invalid class " +
				ice.getLocalizedMessage());
		}
		catch (NotSerializableException nse)
		{
			logger.severe(
				"ClientHandler[" + mainClient.getName()
					+ "]: write to not serializable exception "
					+ nse.getLocalizedMessage());
		}
		catch (IOException e)
		{
			logger.severe("ClientHandler[" + mainClient.getName()
					+ "]: received or write failed, Closing client " + this);
		}

		// remove current client from allClients (should be atomic)
		synchronized (allClients)
		{
			boolean removed = allClients.remove(mainClient);
			if (!removed)
			{
				logger.warning("ClientHandler::run::end : failed to remove " +
					"main client from clients");
			}
		}
		// cleanup current client
		mainClient.cleanup();
		synchronized (parent)
		{
			nbThreads--;
			parent.cleanup();
		}
	}
}
