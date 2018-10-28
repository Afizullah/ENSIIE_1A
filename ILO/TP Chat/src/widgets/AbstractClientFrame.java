package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import logger.LoggerFactory;

/**
 * Abstract frame containing all common elements to all GUI Chat Clients.
 * <ul>
 * <li>{@link #commonRun} to ensure common run/stop of multiple threads</li>
 * <li>{@link #inPipe} input stream to read messages from server (text or object
 * messages) and displya messages in the client.</li>
 * <li>{@link #outPipe} output stream to write messages to the server
 * (text)</li>
 * <li>{@link #outPW} output print writer to write user messages to the
 * {@link #outPipe} to the server</li>
 * <li>{@link #logger} for logging</li>
 * <li>{@link #document} The document where to write (possibly in rich text)
 * messages from server</li>
 * <li>{@link #documentStyle} to locally modify the document style in order to
 * display messages with a specific color for instance</li>
 * <li>{@link #defaultColor} to use in the {@link #documentStyle} of the
 * {@link #document}</li>
 * <li>{@link #colorMap} map associating a word with a color in order to
 * associate a unique color to each users logged on the server whe displaying
 * messages</li>
 * </ul>
 * @author davidroussel
 */
public abstract class AbstractClientFrame extends JFrame implements Runnable
{
	/**
	 * Serial ID (because {@link TransferHandler} is serializable)
	 */
	private static final long serialVersionUID = 7475861952441319100L;

	/**
	 * Common run when mutiple threads are used for listening to server's
	 * messages
	 */
	protected Boolean commonRun;

	/**
	 * Piped intput stream to read messages from server
	 */
	protected final PipedInputStream inPipe;

	/**
	 * Piped output stream to write messages to server
	 */
	protected final PipedOutputStream outPipe;

	/**
	 * Print writer to write the content of the {@link #txtFieldSend} containing
	 * the message to the {@link #outPipe} to the server
	 */
	protected final PrintWriter outPW;

	/**
	 * Logger to show debug message or only log them in a file
	 */
	protected Logger logger;

	/**
	 * The underlying document of a {@link JTextPane} in wich messages should be
	 * displayed
	 */
	protected StyledDocument document;

	/**
	 * The sytle applied to {@link #document}
	 */
	protected Style documentStyle;

	/**
	 * Default text color in {@link #documentStyle}
	 */
	protected Color defaultColor;

	/**
	 * Map associating names to colors so that each message from specific users
	 * can be displayed with a specific color
	 * This map is updated with calls to {@link #getColorFromName(String)}
	 * @see #getColorFromName(String)
	 */
	protected Map<String, Color> colorMap;

	/**
	 * [protected] constructor (used in subclasses)
	 * @param name user name
	 * @param host chat server's names or IP address
	 * @param commonRun common run status with other threads in the client
	 * @param parentLogger parent logger
	 * @throws HeadlessException when code that is dependent on a keyboard,
	 * display, or mouse is called in an environment that does not support a
	 * keyboard, display, or mouse
	 */
	protected AbstractClientFrame(String name,
	                              String host,
	                              Boolean commonRun,
	                              Logger parentLogger)
		throws HeadlessException
	{
		// --------------------------------------------------------------------
		// Logger
		//---------------------------------------------------------------------
		logger = LoggerFactory.getParentLogger(getClass(),
		                                       parentLogger,
		                                       (parentLogger == null ?
		                                    	Level.INFO :
		                                    	parentLogger.getLevel()));

		// --------------------------------------------------------------------
		// Common run with other threads
		//---------------------------------------------------------------------
		if (commonRun != null)
		{
			this.commonRun = commonRun;
		}
		else
		{
			this.commonRun = Boolean.TRUE;
		}

		// --------------------------------------------------------------------
		// IO streams
		//---------------------------------------------------------------------
		inPipe = new PipedInputStream();
		logger.info("AbstractClientFrame : PipedInputStream Created");

		outPipe = new PipedOutputStream();
		logger.info("AbstractClientFrame : PipedOutputStream Created");
		outPW = new PrintWriter(outPipe, true);
		if (outPW.checkError())
		{
			logger.warning("ClientFrame: Output PrintWriter has errors");
		}
		else
		{
			logger.info("AbstractClientFrame : Printwriter to PipedOutputStream Created");
		}

		// --------------------------------------------------------------------
		// Window setup
		//---------------------------------------------------------------------
		if (name != null)
		{
			setTitle(name);
		}

		setPreferredSize(new Dimension(400, 200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		document = null;
		documentStyle = null;
		defaultColor = Color.BLACK;
		colorMap = new TreeMap<String, Color>();
	}

	/**
	 * Send message into {@link #outPipe} (iff non null) using the
	 * {@link #outPW}
	 * @param the message to send
	 */
	protected void sendMessage(String message)
	{
		logger.info("ClientFrame::sendMessage writing out: "
		    + (message == null ? "NULL" : message));
		/*
		 * DONE send message with #outPW and check for errors. If an error
		 * occurs log a warning
		 */
		if (message != null)
		{
			outPW.println(message);
			if (outPW.checkError())
			{
				logger.warning("ClientFrame::sendMessage: error writing");
			}
		}
	}

	/**
	 * Compute Color from name: retrieve color from {@link #colorMap} and if
	 * this name is not already in the map add a new <name, color> to the map
	 * before retriving
	 * @param name the name to generate color from
	 * @return a {@link Color} associated to the name or null if name is null or
	 * empty
	 * @warning Ensure similar names don't get similar colors
	 */
	protected Color getColorFromName(String name)
	{
		/*
		 * DONE return a color (not too bright, using Color#darker()) from the
		 * provided name.
		 * You can use the name's hashcode and a Random#nextInt to generate the
		 * color
		 */
		if (name != null)
		{
			if (name.length() > 0)
			{
				if (!colorMap.containsKey(name))
				{
					Random rand = new Random(name.hashCode());
					colorMap.put(name, new Color(rand.nextInt()).darker());
					// colorMap.put(name, name.hashCode()).darker();
				}

				return colorMap.get(name);
			}
		}

		return null;
	}

	/**
	 * {@link #inPipe} accessor to connect to a {@link PipedOutputStream}
	 * @return The {@link #inPipe}
	 */
	public PipedInputStream getInPipe()
	{
		return inPipe;
	}

	/**
	 * {@link #outPipe} accessor to connecto to a {@link PipedInputStream}
	 * @return The {@link #outPipe}
	 */
	public PipedOutputStream getOutPipe()
	{
		return outPipe;
	}

	/**
	 * Cleanup: close window and streams
	 */
	public void cleanup()
	{
		logger.info("ClientFrame::cleanup: closing window ... ");
		dispose();

		logger.info("ClientFrame::cleanup: closing output print writer ... ");
		outPW.close();

		logger.info("ClientFrame::cleanup: closing output stream ... ");
		try
		{
			outPipe.close();
		}
		catch (IOException e)
		{
			logger.warning("ClientFrame::cleanup: failed to close output stream"
				+ e.getLocalizedMessage());
		}

		logger.info("ClientFrame::cleanup: closing input stream ... ");
		try
		{
			inPipe.close();
		}
		catch (IOException e)
		{
			logger.warning("ClientFrame::cleanup: failed to close input stream"
				+ e.getLocalizedMessage());
		}
	}
}
