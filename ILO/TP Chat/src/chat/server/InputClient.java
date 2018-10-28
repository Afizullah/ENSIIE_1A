package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

import logger.LoggerFactory;

/**
 * Class representing a main client in a {@link ClientHandler} containing
 * <ul>
 * <li>{@link #clientSocket} : the client {@link Socket}</li>
 * <li>{@link #name} : the client name</li>
 * <li>{@link #inBR} : {@link BufferedReader} created from an
 * {@link InputStreamReader} on the {@link InputStream} of the {@link Socket}
 * allowing to read text from client</li>
 * <li>{@link #ready} indicates that a {@link BufferedReader} has been created
 * and we're ready to read lines from client</li>
 * <li>{@link #banned} indicates this client has been banned from server and
 * should not be processed anymore</li>
 * </ul>
 * @author davidroussel
 */
public class InputClient
{
	/**
	 * Client's socket
	 */
	protected Socket clientSocket;

	/**
	 * Client't name
	 */
	protected String name;

	/**
	 * Client's reader (used to read from client)
	 */
	protected BufferedReader inBR;

	/**
	 * Client ready flag (true when {@link #clientSocket} and {@link #inBR}
	 * are bith non null)
	 */
	protected boolean ready;

	/**
	 * Indicates if this client is currently banned.
	 */
	protected boolean banned;

	/**
	 * logger to display info or debug messages
	 */
	protected Logger logger;

	/**
	 * Constructor
	 * @param socket the client's socket
	 * @param name the client's name
	 * @param parentLogger parent logger
	 */
	public InputClient(Socket socket, String name, Logger parentLogger)
	{
		clientSocket = socket;
		this.name = name;
		inBR = null;
		ready = false;

		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       parentLogger.getLevel());

		if (socket != null)
		{
			logger.info("InputClient: Creating Input Stream ... ");
			try
			{
				inBR = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ready = true;
			}
			catch (IOException e)
			{
				logger.severe("InputClient: unable to get client socket input stream");
				logger.severe(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Client's name accessor
	 * @return the name of the client
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Client's input reader accessor
	 * @return the input {@link BufferedReader}
	 */
	public BufferedReader getIn()
	{
		return inBR;
	}

	/**
	 * Client's ready status accessor
	 * @return the ready status
	 */
	public boolean isReady()
	{
		return ready;
	}

	/**
	 * Banned status accessor
	 * @return the banned status
	 */
	public boolean isBanned()
	{
		return banned;
	}

	/**
	 * Banned status setter
	 * @param banned the new banned status
	 */
	public void setBanned(boolean banned)
	{
		this.banned = banned;
	}

	/**
	 * Client's cleanup: Closes input stream an socket
	 */
	public void cleanup()
	{
		ready = false;
		logger.info("MainClient::cleanup: closing input stream ... ");
		try
		{
			inBR.close();
		}
		catch (IOException e)
		{
			logger.severe("MainClient::cleanup: unable to close input stream");
			logger.severe(e.getLocalizedMessage());
		}

		logger.info("MainClient::cleanup: closing client socket ... ");
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			logger.severe("MainClient::cleanup: unable to close client socket");
			logger.severe(e.getLocalizedMessage());
		}
	}
}
