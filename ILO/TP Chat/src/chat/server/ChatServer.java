package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import chat.Failure;
import logger.LoggerFactory;
import models.Message;

/**
 * Chat server's class.
 * Each message from a client is sent to all other clients
 * @author davidroussel
 */
public class ChatServer implements Runnable
{
	/**
	 * The server socket
	 */
	private ServerSocket serverSocket;

	/**
	 * default port
	 */
	public final static int DEFAULTPORT = 1394;

	/**
	 * Default clients connection timeout (in ms). When this timeout is up a
	 * {@link SocketTimeoutException} is raised and we can choose to wait again
	 * for a client connection or quit the loop if there is no mode clients
	 * connected to the server
	 */
	public final static int DEFAULTTIMEOUT = 1000;

	/**
	 * Clients list, a client is made of
	 * <ul>
	 * <li>a {@link Socket}</li>
	 * <li>a name : {@link String}</li>
	 * <li>an input stream : {@link BufferedReader}</li>
	 * <li>an output stream {@link PrintWriter}</li>
	 * </ul>
	 * One should access this list atomically since several threads (one for
	 * each client)
	 * will be running in the server
	 */
	private Vector<InputOutputClient> clients;

	/**
	 * Clients handler list (one handler for each client)
	 */
	private Vector<ClientHandler> handlers;

	/**
	 * logger to display debug or info messages
	 */
	private Logger logger;

	/**
	 * Listining state of the server.
	 * Set to false when {@link #quitOnLastClient} is true and last client logs
	 * out
	 */
	private boolean listening;

	/**
	 * Flag to quit the server when last client logs out
	 */
	private final boolean quitOnLastClient;

	/**
	 * Number of messages to keep on server (used when a client sends a
	 * "catchup" request)
	 */
	private final int messagesHistory;

	/**
	 * Default number of messages to keep on server
	 */
	private final static int DefaultMessagesHistory = 200;

	/**
	 * List of all messages received from clients (resent during "catchup"
	 * processing)
	 */
	private Deque<Message> allMessages;

	/**
	 * Chat server constructor.
	 * Initialize the {@link ServerSocket}
	 * @param port TCP port used to listen to clients messages
	 * @param verbose indicates if logger displays messages in console
	 * @param timeout client wait timeout
	 * @param quitOnLastClient quits the server when last client logs out
	 * @param history number of messages to keep on server
	 * @param parentLogger parent logger
	 * @throws IOException if the {@link ServerSocket} could not be created
	 * properly
	 */
	public ChatServer(int port,
	                  int timeout,
	                  boolean quitOnLastClient,
	                  int history,
	                  Logger parentLogger)
	    throws IOException
	{
		this.quitOnLastClient = quitOnLastClient;
		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       parentLogger.getLevel());

		logger.info("ChatServer::ChatServer(port = " + port + ", timeout = "
		    + timeout + ", quit = " + (quitOnLastClient ? "true" : "false")
		    + ")");

		serverSocket = new ServerSocket(port);
		if (serverSocket != null)
		{
			serverSocket.setSoTimeout(timeout);
		}

		clients = new Vector<InputOutputClient>();
		handlers = new Vector<ClientHandler>();

		messagesHistory = history;
		allMessages = new LinkedList<Message>();
	}

	/**
	 * Chat server constructor with default timeout, quits on last client logout
	 * and default message number
	 * Initialize the {@link ServerSocket}
	 * @param port TCP port used to listen to clients messages
	 * @param parentLogger parent logger
	 * @throws IOException if the {@link ServerSocket} could not be created
	 * properly
	 */
	public ChatServer(int port, Logger parentLogger) throws IOException
	{
		this(port, DEFAULTTIMEOUT, true, DefaultMessagesHistory, parentLogger);
	}

	/**
	 * Chat server constructor with default port, default timeout, quits on last
	 * client logout
	 * and default message number
	 * Initialize the {@link ServerSocket}
	 * @param port TCP port used to listen to clients messages
	 * @param parentLogger parent logger
	 * @throws IOException if the {@link ServerSocket} could not be created
	 * properly
	 */
	public ChatServer(Logger parentLogger) throws IOException
	{
		this(DEFAULTPORT, parentLogger);
	}

	/**
	 * {@link #quitOnLastClient} accessor
	 * @return {@link #quitOnLastClient}'s value
	 */
	public boolean isQuitOnLastClient()
	{
		return quitOnLastClient;
	}

	/**
	 * listening state setter
	 * @param value new value of the listenig state
	 */
	public synchronized void setListening(boolean value)
	{
		listening = value;
	}

	/**
	 * Adds a message to the list of messages to keep on server
	 * {@link #allMessages} is accessed atomically to avoid multiple clients
	 * modifying this list.
	 * @param m the message to add to the list
	 */
	public synchronized void addMessage(Message m)
	{
		if (m != null)
		{
			synchronized (allMessages)
			{
				allMessages.add(m);
				if (allMessages.size() > messagesHistory)
				{
					allMessages.remove();
				}
//				System.out.println("Messages stored : [" + messagesHistory + "]");
//				allMessages.stream().forEach((Message ms) -> System.out.println(ms));
			}
		}
	}

	/**
	 * Factory method to get an iterator to the list of messages kept on the
	 * server
	 * @return an iterator to the list of messages kept on the server (might be
	 * invalidated if a client's thread add a new message during traversal)
	 */
	public Iterator<Message> messages()
	{
		synchronized (allMessages)
		{
			return allMessages.iterator();
		}
	}

	/**
	 * Chat server run loop: Awaits connection from a client, when a client
	 * connects a new {@link ClientHandler} is created and launched in a thread
	 * then the loop resume. Default behavior to clients time out is also to
	 * resume loop.
	 * When a {@link ClientHandler} terminates it triggers the
	 * {@link #cleanup()} method which might set the listening state to false,
	 * then the running loop ends.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		Vector<Thread> handlerThreads = new Vector<Thread>();
		listening = true;

		while (listening)
		{
			Socket clientSocket = null;
			String clientName = null;

			// Accept client's socket (until timeout is up)
			try
			{
				// wait for new client's connection
				clientSocket = serverSocket.accept(); // --> IOException
				logger.fine("ChatServer: client connection accepted");

			}
			catch (SocketTimeoutException ste)
			{
				// Wait again
				// logger.info("Socket timeout, rewaiting ...");
				continue;
			}
			catch (IOException e)
			{

				logger.severe(Failure.SERVER_CONNECTION.toString()
				    + ": " + e.getLocalizedMessage());
				System.exit(Failure.SERVER_CONNECTION.toInteger());
			}

			if (clientSocket != null)
			{
				// Get client's name
				BufferedReader reader = null;
				logger.info("ChatServer: Creatingc client input stream to get client's name ... ");
				try
				{
					reader = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
				}
				catch (IOException e1)
				{
					logger.severe("ChatServer: " + Failure.CLIENT_INPUT_STREAM);
					logger.severe(e1.getLocalizedMessage());
					System.exit(Failure.CLIENT_INPUT_STREAM.toInteger());
				}
				if (reader != null)
				{
					logger.info("ChatServer: reading client's name: ");
					try
					{
						// Read client's name
						clientName = reader.readLine();
						logger.info("ChatServer: client name " + clientName);
					}
					catch (IOException e)
					{
						logger.severe("ChatServer: "+ Failure.NO_NAME_CLIENT);
						logger.severe(e.getLocalizedMessage());
						System.exit(Failure.NO_NAME_CLIENT.toInteger());
					}

					/*
					 * NOTE: client input stream should NOT be close since
					 * it would close the socket
					 */
				}

				/*
				 * Before registering an new client's connection we
				 * should check if there is not already a client with
				 * this name
				 */
				if (searchClientByName(clientName) == null)
				{
					// new client instantiation
					InputOutputClient newClient =
							new InputOutputClient(clientSocket,
							                      clientName,
							                      logger);

					// Adds this client to the list of clients
					synchronized (clients)
					{
						clients.add(newClient);
					}

					// Create and launch a handler for this client
					ClientHandler handler = new ClientHandler(this,
					                                          newClient,
					                                          clients,
					                                          logger);
					handlers.add(handler);
					Thread handlerThread = new Thread(handler);
					handlerThread.start();
					handlerThreads.add(handlerThread);
				}
				else // a client with this name already exists
				{
					// sends denial message to client
					try
					{
						PrintWriter out = new PrintWriter(
								clientSocket.getOutputStream(), true);
						out.println("server > Sorry another client already use the name "
								+ clientName);
						out.println("Hit ^D to close your client and try another name");
						out.close();
					}
					catch (IOException e)
					{
						logger.severe("ChatServer: " + Failure.CLIENT_OUTPUT_STREAM);
						logger.severe(e.getLocalizedMessage());
					}
				}

				/*
				 * When a ClientHandler terminates it triggers the
				 * cleanup method which might change the listening status
				 * if this is the last client to log out
				 */
			}
		} // while listening

		// Wait for all ClientHandlers to terminate
		for (Thread t : handlerThreads)
		{
			try
			{
				t.join();
			}
			catch (InterruptedException e)
			{
				logger.severe("ChatServer::run: Client handlers join interrupted");
				logger.severe(e.getLocalizedMessage());
			}
		}

		logger.info("ChatServer::run: all client handlers terminated");

		handlerThreads.clear();
		handlers.clear();
		clients.clear();

		logger.info("ChatServer::run: Closing server socket ... ");
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			logger.severe("Close serversocket Failed !");
			logger.severe(e.getLocalizedMessage());
		}

	}

	/**
	 * Cleanup method invoked by {@link ClientHandler}s when they
	 * terminate which might change the {@link #listening} status if
	 * the last client terminates
	 */
	protected synchronized void cleanup()
	{
		// s'il ne reste plus de threads on arrête la boucle
		int nbThreads = ClientHandler.getNbThreads();
		if (nbThreads <= 0)
		{
			if (quitOnLastClient)
			{
				listening = false;
				logger.info("ChatServer::run: no more threads.");
			}
		}
		else
		{
			logger.info("ChatServer::run: still " + nbThreads +
					" threads remaining ...");
		}
	}

	/**
	 * Search a client by name
	 * @param clientName the name of the client to search in the
	 * {@link #clients} list
	 * @return the first client with this name of null if there is no such
	 * client
	 */
	protected InputOutputClient searchClientByName(String clientName)
	{
		/*
		 * Accessing the client list must be performed atomically
		 * to avoid modification by other threads
		 */
		synchronized (clients)
		{
			for (InputOutputClient c : clients)
			{
				if (c.getName().equals(clientName))
				{
					return c;
				}
			}
		}

		return null;
	}
}
