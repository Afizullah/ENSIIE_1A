package chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import chat.Failure;
import chat.UserOutputType;
import logger.LoggerFactory;
import models.Message;
/**import sun.print.resources.serviceui_pt_BR;**/

/**
 * Chat Client main class, contains
 * <ul>
 * 	<li>A socket to communicate with server</li>
 * 	<li>A {@link UserHandler} to handle messages from user</li>
 * 	<li>A {@link ServerHandler} to handle messages from server</li>
 * </ul>
 * @author davidroussel
 */
public class ChatClient implements Runnable
{
	/**
	 * User name to connect to server
	 */
	private String userName;

	/**
	 * Client socket to get input and output streams from/to server
	 */
	private Socket clientSocket;

	/**
	 * The input stream from server
	 */
	private InputStream serverIn;

	/**
	 * The output stream to server
	 */
	private OutputStream serverOut;

	/**
	 * Server output print writer (used only once to send our user name to
	 * server)
	 */
	private PrintWriter serverOutPW;

	/**
	 * The input stream from user
	 */
	private InputStream userIn;

	/**
	 * The output stream to user
	 */
	private OutputStream userOut;

	/**
	 * Handler managing data from server: reads messages from server and display
	 * server messages to user
	 */
	private ServerHandler serverHandler = null;

	/**
	 * Handler managing data from user: reads messages from user and send it to
	 * server
	 */
	private UserHandler userHandler = null;

	/**
	 * Common run status between {@link #serverHandler} and
	 * {@link #userHandler}.
	 * Since both handlers are {@link Runnable} and threaded, when one of these
	 * terminates its run loop, the other should also terminates
	 */
	private Boolean commonRun;

	/**
	 * Client readiness status: true when socket and streams have been
	 * initialized
	 */
	private boolean ready;

	/**
	 * Logger used to display info|error|warning messages
	 */
	private Logger logger;

	/**
	 * Chat client constructor
	 * @param host the server name or IP address
	 * @param port the port used to communicate with server
	 * @param name user name to register on server (server only accept users
	 * once)
	 * @param in input stream from user
	 * @param out output stream to user
	 * @param outType kind of data expected by the user (either text or
	 * {@link Message} objects)
	 * @param commonRun common run shared by another runnable or null if we
	 * should create our own common run between our handlers
	 * @param parentLogger parent logger
	 */
	public ChatClient(String host,
	                  int port,
	                  String name,
	                  InputStream in,
	                  OutputStream out,
	                  UserOutputType outType,
	                  Boolean commonRun,
	                  Logger parentLogger)
	{
		userName = name;
		ready = false;

		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       parentLogger.getLevel());

		/*
		 * DONE host/port socket creation
		 */
		clientSocket = null;
		try
		{
			clientSocket = new Socket(host, port);
			logger.info("ChatClient: socket created");
		}
		catch (UnknownHostException e)
		{
			/*
			 * DONE Please note how to use the logger from the example below,
			 * you'll have to do the same later
			 */
			logger.severe("ChatClient: " + Failure.UNKNOWN_HOST + ": " + host);
			logger.severe(e.getLocalizedMessage());
			System.exit(Failure.UNKNOWN_HOST.toInteger());
		}
		catch (IOException e)
		{
			logger.severe("ChatClient: " + Failure.CLIENT_CONNECTION
					+ " to: \"" + host + "\" at port \"" + port + "\"");
			logger.severe(e.getLocalizedMessage());
			System.exit(Failure.CLIENT_CONNECTION.toInteger());
		}

		/*
		 * TODO get output stream to server (serverOut) from clientSocket and use logger as follows
		 * 	- logger.info("ChatClient: got client output stream to server"); if serverOut is non null
		 * 	- logger.severe("ChatClient: null server out" + Failure.CLIENT_INPUT_STREAM); if serverOut is null
		 * 	- logger.severe("ChatClient: " + Failure.CLIENT_OUTPUT_STREAM); if and IOException occurs
		 */
		try{
			this.serverOut = clientSocket.getOutputStream();
			if (this.serverOut!=null) {
				logger.info("ChatCLient : got client output stream to server");
			}
			if (this.serverOut==null) {
				logger.severe("ChatClient: null server out" + Failure.CLIENT_INPUT_STREAM);
			}
		}
		
		catch (IOException e) {
			logger.severe("ChatClient: " + Failure.CLIENT_OUTPUT_STREAM);
			logger.severe(e.getLocalizedMessage());
			System.exit(Failure.CLIENT_OUTPUT_STREAM.toInteger());
		}

		/*
		 * TODO Create a temporary PrintWriter to serverOut (serverOutPW with
		 * autoFlush) and
		 * send our user name so that the server can create
		 * a thread dedicated to handling our messages
		 * Use the logger to log progression and/or errors
		 */
		if (serverOut != null)
		{	
			serverOutPW = new PrintWriter(serverOut);
			serverOutPW.println(name);
			logger.info("ChatClient: sending name to server ... ");
			
			if(serverOutPW.checkError()){
				logger.severe("Chat client : name not properly sent");
			}
			// TODO use serverOutPW to println user name and check for errors
		}

		/*
		 * TODO get input stream from clientSocket
		 * If an exception occurs, log severe and exit with
		 * Failure.CLIENT_INPUT_STREAM status
		 */
		
		try {
			serverIn = clientSocket.getInputStream();
		} catch (IOException e) {
			logger.severe("ChatClient:" + Failure.CLIENT_INPUT_STREAM);
			System.exit(Failure.UNKNOWN_HOST.toInteger());
		}
		userIn = in;
		userOut = out;

		if (commonRun == null)
		{
			this.commonRun = new Boolean(true);
		}
		else
		{
			this.commonRun = commonRun;
		}

		userHandler = new UserHandler(userIn,
		                              serverOut,
		                              this.commonRun,
		                              logger);

		serverHandler = new ServerHandler(userName,
		                                  serverIn,
		                                  userOut,
		                                  outType,
		                                  this.commonRun,
		                                  logger);

		ready = true;
	}

	/**
	 * Ready status accessor
	 * @return the ready status
	 */
	public boolean isReady()
	{
		return ready;
	}

	/**
	 * Run loop: Launch {@link UserHandler} and {@link ServerHandler}
	 * in their own threads and wait for them to finish.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		Thread[] threads = new Thread[2];

		threads[0] = new Thread(userHandler);

		threads[1] = new Thread(serverHandler);

		// threads launch
		for (int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}

		// wait for threads to finish
		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			}
			catch (InterruptedException e)
			{
				logger.warning("Join thread " + i + " interrupted");
			}
		}

		logger.info("ChatClient: All threads terminated");

		cleanup();
	}

	/**
	 * Cleanup: close intput / output streams and socket
	 */
	public void cleanup()
	{
		userHandler.cleanup();

		serverHandler.cleanup();

		logger.info("ChatClient: closing server output stream ... ");
		serverOutPW.close();

		logger.info("ChatClient: closing client socket ... ");
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			logger.severe("ChatClient: closing client socket failed");
			logger.severe(e.getLocalizedMessage());
		}
	}
}
