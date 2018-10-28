package chat.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import chat.Failure;


/**
 * Classe stockant les caractéristiques d'un client :
 * Class representing a regular client in a {@link ClientHandler}
 * Regular client adds an output stream to parent's {@link InputClient}
 * in order to write messages to these clients
 * <ul>
 * 	<li>out : {@link ObjectOutputStream}</li>
 * </ul>
 * @author davidroussel
 */
public class InputOutputClient extends InputClient
{
	/**
	 * Object output stream to send messages to
	 */
	private ObjectOutputStream outOS;

	/**
	 * Constructor
	 * @param socket client's socket
	 * @param name client's name
	 * @param verbose verbose level for log messages
	 * @param parentLogger parent's logger
	 */
	public InputOutputClient(Socket socket, String name, Logger parentLogger)
	{
		super(socket, name, parentLogger);
		if (ready)
		{
			outOS = null;
			ready = false;

			if (clientSocket != null)
			{
				logger.info("Client: Creating Output Stream ... ");
				try
				{
					outOS = new ObjectOutputStream(clientSocket.getOutputStream());
					ready = true;
				}
				catch (IOException e)
				{
					logger.severe("Client: unable to get client output stream");
					logger.severe(e.getLocalizedMessage());
				}
			}
		}
		else
		{
			logger.severe("Client: " + Failure.CLIENT_NOT_READY + ", abort...");
			System.exit(Failure.CLIENT_NOT_READY.toInteger());
		}
	}

	/**
	 * client's output stream accessor
	 * @return this client's object output stream
	 */
	public ObjectOutputStream getOut()
	{
		return outOS;
	}

	/**
	 * Client's cleanup: Closes output stream an calls
	 * {@link InputClient#cleanup()}
	 */
	@Override
	public void cleanup()
	{
		logger.info("Client::cleanup: closing output stream ... ");
		try
		{
			outOS.close();
		}
		catch (IOException e)
		{
			logger.severe("Client: unable to close client output stream");
			logger.severe(e.getLocalizedMessage());
		}
		super.cleanup();
	}
}
