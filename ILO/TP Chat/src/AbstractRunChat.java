import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.Failure;
import logger.LoggerFactory;

/**
 * Abstract base class to run a chat client or server
 * @author davidroussel
 */
public abstract class AbstractRunChat
{
	/**
	 * Connection port number between server and clients
	 */
	protected int port;

	/**
	 * Default connection port number
	 */
	public static final int DEFAULTPORT = 1394;

	/**
	 * Verbose status indicating if debug messages should be displayed
	 * on the console or only sent to a log file
	 */
	protected boolean verbose;

	/**
	 * Logger used to display debug or info messages
	 */
	protected Logger logger;

	/**
	 * Protected Client or Server constructor parsing common arguments,
	 * such as {@link #verbose} and {@link #port}
	 * @param args arguments strings to parse for {@link #verbose} and
	 * {@link #port} specifications
	 */
	protected AbstractRunChat(String[] args)
	{
		setAttributes(args);
	}

	/**
	 * Sets attributes values based on argument parsing.
	 * @param args arguments to parse for setting attributes values
	 * for {@link #verbose} and {@link #port} specifications
	 */
	protected void setAttributes(String[] args)
	{
		/*
		 * Set attributes to default values
		 */
		port = DEFAULTPORT;
		verbose = false;

		/*
		 * Arguments parsing
		 * 	-v | --verbose : for verbose setting
		 * 	-p | --port : for port setting used in the serverSocket
		 */
		for (int i=0; i < args.length; i++)
		{
			if (args[i].startsWith("-")) // option argument
			{
				if (args[i].equals("--verbose") || args[i].equals("-v"))
				{
					System.out.println("Setting verbose on");
					verbose = true;
				}
				if (args[i].equals("--port") || args[i].equals("-p"))
				{
					System.out.print("Setting port to: ");
					if (i < (args.length - 1))
					{
						// search for port number in next argument
						Integer portInteger = readInt(args[++i]);
						if (portInteger != null)
						{
							int readPort = portInteger.intValue();
							if (readPort >= 1024)
							{
								port = readPort;
							}
							else
							{
								System.err.println(Failure.INVALID_PORT);
								System.exit(Failure.INVALID_PORT.toInteger());
							}
						}
						System.out.println(port);
					}
					else
					{
						System.out.println("nothing, invalid value");
					}
				}
			}
		}

		/*
		 * logger instantiation
		 */
		logger = null;
		Class<?> runningClass = getClass();
		String logFilename =
		    (verbose ? null : runningClass.getSimpleName() + ".log");
		Logger parent = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		Level level = (verbose ? Level.ALL : Level.INFO);
		try
		{
			logger = LoggerFactory.getLogger(runningClass,
			                                 verbose,
			                                 logFilename,
			                                 false,
			                                 parent,
			                                 level);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.exit(Failure.OTHER.toInteger());
		}
	}

	/**
	 * Once created, launch main loopof client or server
	 */
	protected abstract void launch();

	/**
	 * Utility method to read number from string with exception handling
	 * @param s the string to parse for number
	 * @return the parsed Integer or null if number could not be parsed
	 * from string
	 */
	protected Integer readInt(String s)
	{
		try
		{
			Integer value = new Integer(Integer.parseInt(s));
			return value;
		}
		catch (NumberFormatException e)
		{
			// System.err.println("readInt: " + s + " is not a number");
			logger.warning("readInt: " + s + " is not a number");
			return null;
		}
	}
}
