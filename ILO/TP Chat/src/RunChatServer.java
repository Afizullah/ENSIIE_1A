import java.io.IOException;
import java.net.SocketException;

import chat.Failure;
import chat.Vocabulary;
import chat.server.ChatServer;

/**
 * Chat server launcher
 * @author davidroussel
 */
public class RunChatServer extends AbstractRunChat
{
	/**
	 * Server socket timeout before it starts waiting for new clients
	 * connections
	 */
	private int timeout;

	/**
	 * Flag to quit server when last client logs out
	 */
	private boolean quitOnLastclient;

	/**
	 * Number of messages to keep on server and send to a user requesting a
	 * catchup
	 * @see Vocabulary#catchUpCmd
	 */
	private int history;

	/**
	 * Default time out to wait for client connection : 5 seconds
	 */
	public static final int DEFAULTTIMEOUT = 5000;

	/**
	 * Default number of messages to keep on the server
	 */
	public static final int DEFAULTHISTORY = 10;

	/**
	 * Chat server launcher constructor
	 * @param args program's arguments
	 */
	protected RunChatServer(String[] args)
	{
		super(args);
	}

	/**
	 * Sets attributes values based on argument parsing
	 * @param args arguments to parse for setting attributes values of
	 * {@link #timeout}, {@link #quitOnLastclient} and {@link #history}
	 */
	@Override
	protected void setAttributes(String[] args)
	{
		/*
		 * set attributes to default values
		 */
		timeout = DEFAULTTIMEOUT;
		quitOnLastclient = true;
		history = DEFAULTHISTORY;

		/*
		 * Common arguments parsing
		 * 	-v | --verbose
		 * 	-p | --port : serverSocket's port
		 */
		super.setAttributes(args);

		/*
		 * Server's specific arguments parsing
		 * 	-t | --timeout : server socket wait timeout
		 * 	-q | --quit : quits on last client logging out
		 * 	-h | --history : number of messages to record
		 */
		for (int i=0; i < args.length; i++)
		{
			if (args[i].equals("--timeout") || args[i].equals("-t"))
			{
				if (i < (args.length - 1))
				{
					// parse next arg for in port value
					Integer timeInteger = readInt(args[++i]);
					if (timeInteger != null)
					{
						timeout = timeInteger.intValue();
					}
					logger.info("Setting timeout to " + timeout);
				}
				else
				{
					logger.warning("invalid timeout value");
				}
			}
			if (args[i].equals("--quit") || args[i].equals("-q"))
			{
				quitOnLastclient = true;
				logger.info("Setting quit on last client to true");
			}
			if (args[i].equals("--noquit") || args[i].equals("-n"))
			{
				quitOnLastclient = false;
				logger.info("Setting quit on last client to false");
			}
			if (args[i].equals("--history") || args[i].equals("-h"))
			{
				if (i < (args.length - 1))
				{
					// parse next arg for in history value
					Integer historyInteger = readInt(args[++i]);
					if (historyInteger != null)
					{
						history = historyInteger.intValue();
					}
					logger.info("Setting history to " + history);
				}
				else
				{
					logger.warning("invalid history value");
				}
			}
		}
	}

	/**
	 * Chat server's launch
	 */
	@Override
	protected void launch()
	{
		/*
		 * Create and Launch server on local ip adress with port number and verbose
		 * status
		 */
		logger.info("Creating server on port " + port + " with timeout "
				+ timeout + " ms and verbose " + (verbose ? "on" : "off"));

		ChatServer server = null;
		try
		{
			server = new ChatServer(port,
			                        timeout,
			                        quitOnLastclient,
			                        history,
			                        logger);
		}
		catch (SocketException se)
		{
			logger.severe(Failure.SET_SERVER_SOCKET_TIMEOUT + ", abort ...");
			logger.severe(se.getLocalizedMessage());
			System.exit(Failure.SET_SERVER_SOCKET_TIMEOUT.toInteger());
		}
		catch (IOException e)
		{
			logger.severe(Failure.CREATE_SERVER_SOCKET + ", abort ...");
			e.printStackTrace();
			System.exit(Failure.CREATE_SERVER_SOCKET.toInteger());
		}

		// Wait for serverThread to stop
		Thread serverThread = null;
		if (server != null)
		{
			serverThread = new Thread(server);
			serverThread.start();

			logger.info("Waiting for server to terminate ... ");
			try
			{
				serverThread.join();
				logger.fine("Server terminated, program end.");
			}
			catch (InterruptedException e)
			{
				logger.severe("Server Thread Join interrupted");
				logger.severe(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Main prograù
	 * @param args program's arguments
	 * <ul>
	 * 	<li>--port <port number> : set host connection port</li>
	 * 	<li>--verbose : set verbose on</li>
	 * 	<li>--timeout <timeout in ms> : server socket waiting time out</li>
	 * 	<li>--quit : quits on last client logout</li>
	 * 	<li>--history <nb messages> : number of messages to keep</li>
	 * </ul>
	 */
	public static void main(String[] args)
	{
		RunChatServer server = new RunChatServer(args);

		server.launch();
	}
}
