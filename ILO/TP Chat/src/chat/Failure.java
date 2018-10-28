package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Enumation of all possible problems in the client/server chat system
 * @author davidroussel
 */
public enum Failure
{
	/**
	 * Unable to get local host IP address
	 */
	NO_LOCAL_HOST,
	/**
	 * Invalid port, usr ports should be > 1024
	 */
	INVALID_PORT,
	/**
	 * Unable to determin log name or user name
	 */
	NO_USER_NAME,
	/**
	 * Unable to access system env to get user or log names
	 */
	NO_ENV_ACCESS,
	/**
	 * Unable to set timeout on server
	 */
	SET_SERVER_SOCKET_TIMEOUT,
	/**
	 * Unable to create server socket
	 */
	CREATE_SERVER_SOCKET,
	/**
	 * Unkown host
	 */
	UNKNOWN_HOST,
	/**
	 * Unable to create client socket to host
	 */
	CLIENT_CONNECTION,
	/**
	 * Unable to obtain input stream from server on client
	 * OR to obtain input stream from client on server
	 */
	CLIENT_INPUT_STREAM,
	/**
	 * Unable to obtain output stream to server on client
	 * OR to obtain temporary print writer to server on client
	 * OR to obtain temporary print writer to client in server
	 */
	CLIENT_OUTPUT_STREAM,
	/**
	 * Unable to create {@link PipedInputStream} from gui client Out Pipe
	 * OR unable to create {@link BufferedReader} on {@link InputStreamReader}
	 * from user
	 */
	USER_INPUT_STREAM,
	/**
	 * Unable to create {@link PipedOutputStream} to GUI client In Pipe
	 * OR Unable to create {@link ObjectOutputStream} to user in client
	 */
	USER_OUTPUT_STREAM,
	/**
	 * Unable to accept new connection from client in server
	 */
	SERVER_CONNECTION,
	/**
	 * Not used yet
	 */
	SERVER_INPUT_STREAM,
	/**
	 * Not used yet
	 */
	SERVER_OUTPUT_STREAM,
	/**
	 * Unamed client [obsolete]
	 */
	NO_NAME_CLIENT,
	/**
	 * GUI Client launch failed
	 */
	CLIENT_NOT_READY,
	/**
	 * Other
	 */
	OTHER;

	/**
	 * String representation of possible errors
	 */
	@Override
	public String toString()
	{
		switch (this)
		{
		// RunChatClient Failures (3)
			case NO_LOCAL_HOST:
				return new String("Unable to get local host name");
			case INVALID_PORT:
				return new String("Port number should be > 1024");
			case NO_USER_NAME:
				return new String("Empty user name");
			case NO_ENV_ACCESS:
				return new String(
						"System does not allow access to environment variables");
		// RunChatServer Failures (2)
			case SET_SERVER_SOCKET_TIMEOUT:
				return new String("Unable to set Server socket timeout");
			case CREATE_SERVER_SOCKET:
				return new String("Unable to create Server socket");
		// Chat Client (4)
			case UNKNOWN_HOST:
				return new String("Unkown host");
			case CLIENT_CONNECTION:
				return new String("Couldn't get I/O for connection to host");
			case CLIENT_INPUT_STREAM:
				return new String("Could not get intput stream from client");
			case CLIENT_OUTPUT_STREAM:
				return new String("Could not get output stream to client");
		// ServerHandler (2)
			case USER_INPUT_STREAM:
				return new String("Could not get input stream from user");
		// ServerHandler
			case USER_OUTPUT_STREAM:
				return new String("Could not get output stream to user");
		// ChatServer#run (3)
			case SERVER_CONNECTION:
				return new String("Client connection to server failed");
			case SERVER_INPUT_STREAM:
				return new String("could not get input stream from server");
			case SERVER_OUTPUT_STREAM:
				return new String("could not get output stream to server");
			case NO_NAME_CLIENT:
				return new String("Unable to read client's name");
				// Client (1)
			case CLIENT_NOT_READY:
				return new String("Main Client not ready");
			case OTHER:
				return new String("Other cause");
		}
		throw new AssertionError("Failure: unknown op: " + this);
	}

	/**
	 * Error case conversion to integer
	 * @return the index of the error
	 * @code System.exit(Failure.CLIENT_NOT_READY.toInteger());
	 * @endcode
	 */
	public int toInteger()
	{
		return ordinal() + 1;
	}

}
