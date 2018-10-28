package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger Factory
 * @author davidroussel
 */
public class LoggerFactory
{
	/**
	 * Factory method for a console logger
	 * @param client the logger's client class, used to provide name to logger
	 * @param level min log level (e.g. FINE, INFO, WARNING, SEVERE)
	 * @return a simple console logger
	 * @throws IOException
	 */
	public static <E> Logger getConsoleLogger(Class<E> client, Level level)
	{
		Logger logger = null;
		try
		{
			logger = getLogger(client, true, null, false, null, level);

		}
		catch (IOException e)
		{
			System.err.println("getConsoleLogger: impossible file IO error");
			e.printStackTrace();
			System.exit(e.hashCode());
		}

		return logger;
	}

	/**
	 * Factory method for a child logger
	 * @param client the logger's client class, used to provide name to logger
	 * @param parentLogger the parent logger (if any)
	 * @param level min log level (e.g. FINE, INFO, WARNING, SEVERE)
	 * @return a child logger to the parent logger
	 * @throws IOException
	 */
	public static <E> Logger getParentLogger(Class<E> client,
	                                         Logger parentLogger,
	                                         Level level)
	{
		Logger logger = null;
		try
		{
			logger = getLogger(client, true, null, false, parentLogger, level);
		}
		catch (IOException e)
		{
			System.err.println("getParentLogger: impossible file IO error");
			e.printStackTrace();
			System.exit(e.hashCode());
		}

		return logger;
	}

	/**
	 * Factory method for a file logger
	 * @param client the logger's client class, used to provide name to logger
	 * @param fileName file name to log in
	 * @param xmlFormat flag to format output with XML
	 * @param level min log level (e.g. FINE, INFO, WARNING, SEVERE)
	 * @return a file logger
	 * @throws IOException if the file could not be opened
	 */
	public static <E> Logger getFileLogger(Class<E> client,
	                                       String fileName,
	                                       boolean xmlFormat,
	                                       Level level)
	    throws IOException
	{
		return getLogger(client, false, fileName, xmlFormat, null, level);
	}

	/**
	 * Factory method for a general logger
	 * @param client the logger's client class, used to provide name to logger
	 * @param verbose true to display messages in console
	 * @param logFileName file name to log in (or null)
	 * @param xmlFormat flag to format output with XML
	 * @param parentLogger the parent logger (if any)
	 * @param level min log level (e.g. FINE, INFO, WARNING, SEVERE)
	 * @return a general logger
	 * @throws IOException if the file could not be opened
	 */
	public static <E> Logger getLogger(Class<E> client,
	                                   boolean verbose,
	                                   String logFileName,
	                                   boolean xmlFormat,
	                                   Logger parentLogger,
	                                   Level level)
	    throws IOException
	{
		Logger logger = null;

		if (verbose || (logFileName != null) || (parentLogger != null))
		{
			if (client != null)
			{
				String canonicalName = client.getCanonicalName();
				logger = Logger.getLogger(canonicalName);

				if (parentLogger != null)
				{
					logger.setParent(parentLogger);
				}
				else
				{
					if (!verbose)
					{
						/*
						 * We don't want messages to be sent to console
						 */
						logger.setUseParentHandlers(false);
					}
				}

				if (logFileName != null)
				{
					String filename = logFileName;
					if (xmlFormat)
					{
						if (!logFileName.contains(new String("xml")))
						{
							filename = logFileName + ".xml";
						}
					}

					// Add file handler to logger
					try
					{
						Handler handler = new FileHandler(filename);
						if (!xmlFormat)
						{
							/*
							 * Default file formatting will be XML,
							 * so we need to setup a simple formatter
							 */
							handler.setFormatter(new SimpleFormatter());
						}

						// Adds filehandler to logger
						logger.addHandler(handler);
						logger.info("log file created");
					}
					catch (IllegalArgumentException e)
					{
						String message = "Empty log file name";
						logger.severe(message);
						logger.severe(e.getLocalizedMessage());
						throw e;
					}
					catch (SecurityException e)
					{
						String message =
						    "Do not have privileges to open log file "
						        + logFileName;
						logger.warning(message);
						logger.warning(e.getLocalizedMessage());
					}
					catch (IOException e)
					{
						String message = "Error opening file " + logFileName;
						logger.severe(message);
						logger.severe(e.getLocalizedMessage());
						throw e;
					}
				}
			}
			else
			{
				if (parentLogger != null)
				{
					logger = parentLogger;
				}
			}
		}

		if (logger != null)
		{
			logger.info("Logger ready");
			logger.setLevel(level);
		}

		return logger;
	}
}
