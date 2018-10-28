import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import chat.Failure;
import chat.UserOutputType;
import chat.client.ChatClient;
import models.OSCheck;
import widgets.AbstractClientFrame;
import widgets.ClientFrame;
import widgets.ClientFrame2;

/**
 * Chat client launcher
 * @author davidroussel
 */
public class RunChatClient extends AbstractRunChat
{
	/**
	 * Chat server host string
	 */
	private String host;

	/**
	 * User name for server connection
	 */
	private String name;

	/**
	 * Input stream to read user messages from
	 */
	private InputStream userIn;

	/**
	 * Output stream to write messages to the user
	 */
	private OutputStream userOut;

	/**
	 * Flag indicating if the client as a graphical interface or not
	 */
	private boolean gui;

	/**
	 * GUI version to use in the client:
	 * <ul>
	 * 	<li>version 1 uses ClientFrame</li>
	 * 	<li>version 2 uses ClientFrame2</li>
	 * 	<li>version 3 uses ClientFrame3</li>
	 * </ul>
	 */
	private int guiVersion;

	/**
	 * Clients threads pool containg all threads used in the client.
	 * Typically there will be a thread for the {@link ChatClient} and
	 * another thread for an eventual GUI such as {@link ClientFrame},
	 * {@link ClientFrame2} or {@link ClientFrame3}
	 */
	private Vector<Thread> threadPool;

	/**
	 * Constructeur d'un lanceur de client d'après les arguments du programme
	 * principal
	 *
	 * @param args les arguments du programme principal
	 */
	/**
	 * Chat client launcher constructor. Parses arguments specific to
	 * the client
	 * @param args the arguments to parse
	 */
	protected RunChatClient(String[] args)
	{
		super(args);

		/*
		 * I/O streams are initialized to null since they will depend
		 * on the type of client (GUI or not)
		 */
		userIn = null;
		userOut = null;

		/*
		 * Client threads pool initialization
		 */
		threadPool = new Vector<Thread>();
	}

	/**
	 * Sets attributes values based on argument parsing
	 * @param args arguments to parse for setting attributes values
	 * for {@link #host}, {@link #name} and {@link #gui}
	 */
	@Override
	protected void setAttributes(String[] args)
	{
		/*
		 * Clients and Server common arguments parsing
		 * -v | --verbose
		 * -p | --port
		 */
		super.setAttributes(args);

		/*
		 * Attributes are initialized to their default value
		 */
		host = null;
		name = null;
		gui = false;

		/*
		 * Client specific arguments parsing
		 * -h | --host : server name or IP address
		 * -n | --name : user name on server
		 * -g | --gui : use GUI(s) or console interface
		 */
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("--host") || args[i].equals("-h"))
			{
				if (i < (args.length - 1))
				{
					// parse next arg for in port value
					host = args[++i];
					logger.fine("Setting host to " + host);
				}
				else
				{
					logger.warning("Setting host to: nothing, invalid value");
				}
			}
			else if (args[i].equals("--name") || args[i].equals("-n"))
			{
				if (i < (args.length - 1))
				{
					// parse next arg for in port value
					name = args[++i];
					logger.fine("Setting user name to: " + name);
				}
				else
				{
					logger.warning("Setting user name to: nothing, invalid value");
				}
			}
			if (args[i].equals("--gui") || args[i].equals("-g"))
			{
				gui = true;
				if (i < (args.length - 1))
				{
					// parse next arg for gui version
					try
					{
						guiVersion = Integer.parseInt(args[++i]);
						if (guiVersion < 1)
						{
							guiVersion = 1;
						}
						else if (guiVersion > 2)
						{
							guiVersion = 2;
						}
					}
					catch (NumberFormatException nfe)
					{
						logger.warning("Invalid gui number, revert to 1");
						guiVersion = 1;
					}
					logger.fine("Setting gui to " + guiVersion);
				}
				else
				{
					logger.warning("ReSetting gui version to 1, invalid value");
					guiVersion = 1;
				}
			}
		}

		if (host == null) // use localhost if there is no specified host
		{
			try
			{
				host = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException e)
			{
				logger.severe(Failure.NO_LOCAL_HOST.toString());
				logger.severe(e.getLocalizedMessage());
				System.exit(Failure.NO_LOCAL_HOST.toInteger());
			}
		}

		if (name == null) // use current user name if there is no specified user name
		{
			try
			{
				// Try LOGNAME on unix type systems
				name = System.getenv("LOGNAME");
			}
			catch (NullPointerException npe)
			{
				logger.warning("no LOGNAME found, trying USERNAME");
				try
				{
					// Try USERNAME on other systems
					name = System.getenv("USERNAME");
				}
				catch (NullPointerException npe2)
				{
					logger.severe(Failure.NO_USER_NAME + " abort");
					System.exit(Failure.NO_USER_NAME.toInteger());
				}
			}
			catch (SecurityException se)
			{
				logger.severe(Failure.NO_ENV_ACCESS + " !");
				System.exit(Failure.NO_ENV_ACCESS.toInteger());
			}
		}
	}

	/**
	 * Chat client launch
	 */
	@Override
	protected void launch()
	{
		/*
		 * Create and Launch client
		 */
		logger.info("Creating client to " + host + " at port " + port
				+ " with verbose " + (verbose ? "on" : "off ... "));

		Boolean commonRun;

		if (gui) // GUI client
		{
			if (OSCheck.getOperatingSystemType() == OSCheck.OSType.MacOS)
			{
				// Use top screen menu instead of application window menu
				System.setProperty("apple.laf.useScreenMenuBar", "true");
		        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name");
			}

			/*
			 * We need a common run between the frame, the ServerHandler and the
			 * UserHandler created below
			 */
			commonRun = Boolean.TRUE;

			/*
			 * Chat client window creation
			 */
			final AbstractClientFrame frame =
			    (guiVersion > 1 ? new ClientFrame2(name,
			                                       host,
			                                       commonRun,
			                                       logger) :
			                      new ClientFrame(name,
			                                      host,
			                                      commonRun,
			                                      logger));

			/*
			 * TODO GUI Output stream instantiation: userOut from the
			 * ClientFrame#getInPipe() :
			 * 	- PipedOutputStream created on the frame's PipedInputStream
			 */
			try
			{
				userOut = new PipedOutputStream(frame.getInPipe());
			}
			catch (IOException e)
			{
				logger.severe(Failure.USER_OUTPUT_STREAM
						+ " unable to get piped out stream");
				logger.severe(e.getLocalizedMessage());
				System.exit(Failure.USER_OUTPUT_STREAM.toInteger());
			}

			/*
			 * TODO GUI Input stream instantiation: userIn from the
			 * ClientFrame#getOutPipe() :
			 * 	- PipedInputStream created on the frame's PipedOutputStream
			 */
			try
			{
				userIn = new PipedInputStream(frame.getOutPipe());
			}
			catch (IOException e)
			{
				logger.severe(Failure.USER_INPUT_STREAM
						+ " unable to get user piped in stream");
				logger.severe(e.getLocalizedMessage());
				System.exit(Failure.USER_INPUT_STREAM.toInteger());
			}

			/*
			 * Insert frame into main GUI Event processing loop with
			 * anonymous Runnable
			 */
			EventQueue.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						frame.pack();
						frame.setVisible(true);
					}
					catch (Exception e)
					{
						logger.severe("GUI Runnable::pack & setVisible" + e.getLocalizedMessage());
					}
				}
			});

			/*
			 * Frame's thread instantiation and launch
			 */
			Thread guiThread = new Thread(frame);
			threadPool.add(guiThread);
			guiThread.start();

		}
		else // console client
		{
			// userIn is simply System input stream
			userIn = System.in;
			// userOut is simply the System output stream
			userOut = System.out;
			// We don't need a common run with console client
			commonRun = null;
		}

		/*
		 * ChatClient launch
		 */
		UserOutputType outType = UserOutputType.fromInteger(guiVersion);
		ChatClient client = new ChatClient(host,		// server's name or IP
		                                   port,		// tcp port
		                                   name,		// user's name
		                                   userIn,		// user input
		                                   userOut,		// user output
		                                   outType,		// user output type (text or object)
		                                   commonRun,	// GUI commonRun
		                                   logger);		// parent logger
		if (client.isReady())
		{
			Thread clientThread = new Thread(client);
			threadPool.add(clientThread);

			clientThread.start();

			logger.fine("client launched");

			// Wait for all threads in the pool to terminate
			for (Thread t : threadPool)
			{
				try
				{
					t.join();
					logger.fine("client thread end");
				}
				catch (InterruptedException e)
				{
					logger.severe("join interrupted" + e.getLocalizedMessage());
				}
			}
		}
		else
		{
			logger.severe(Failure.CLIENT_NOT_READY + " abort ...");
			System.exit(Failure.CLIENT_NOT_READY.toInteger());
		}
	}

	/**
	 * Chat client launch main program
	 * @param args program's arguments :
	 * <ul>
	 * <li>--host <host address> : set host to connect to</li>
	 * <li>--port <port number> : set host connection port</li>
	 * <li>--name <user name> : user name to use to connect</li>
	 * <li>--verbose : set verbose on</li>
	 * <li>--gui <1, 2 or 3>: use graphical interface rather than console interface
	 * </li>
	 * </ul>
	 */
	public static void main(String[] args)
	{

		RunChatClient client = new RunChatClient(args);

		client.launch();
	}
}
