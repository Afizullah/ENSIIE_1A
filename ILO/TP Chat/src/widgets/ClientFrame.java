package widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyleConstants;

import chat.Vocabulary;
import models.Message;

/**
 * Chat GUI v1.0 with
 * <ul>
 * 	<li>server messages display with a different color for each user</li>
 * 	<li>a text field to send new messages to the server</li>
 * </ul>
 * @author davidroussel
 */
public class ClientFrame extends AbstractClientFrame
{
	/**
	 * Serial ID (because {@link TransferHandler} is serializable)
	 */
	private static final long serialVersionUID = -4866607643089870507L;

	/**
	 * Input stream reader to read messages from the {@link #inPipe} and display
	 * them in the {@link #document}
	 */
	private BufferedReader inBR;

	/**
	 * Label indicating the name of the server we're connected to
	 */
	protected final JLabel serverLabel;

	/**
	 * Text field containing the messages to send to server
	 */
	protected final JTextField sendTextField;

	/**
	 * Action for clearing the {@link AbstractClientFrame#document} containing
	 * users messages
	 */
	private final ClearAction clearAction;

	/**
	 * Action for sending the content of the {@link #sendTextField} to the
	 * server
	 */
	private final SendAction sendAction;

	/**
	 * Action for quitting the application
	 */
	protected final QuitAction quitAction;

	/**
	 * Reference to the current window (useful in internal classes)
	 */
	protected final JFrame thisRef;

	/**
	 * Window constructor
	 * @param name user's name
	 * @param host server's name or IP address
	 * @param commonRun common run with other threads
	 * @param parentLogger parent logger
	 * @throws HeadlessException when code that is dependent on a keyboard,
	 * display, or mouse is called in an environment that does not support a
	 * keyboard, display, or mouse
	 */
	public ClientFrame(String name,
	                   String host,
	                   Boolean commonRun,
	                   Logger parentLogger)
	    throws HeadlessException
	{
		super(name, host, commonRun, parentLogger);
		thisRef = this;

		// -------------------------------------------------------------
		// IO streams
		//--------------------------------------------------------------
		/*
		 * Caution: input stream creation should (eventualluu) be delayed
		 * until #run since the #inPipe can't be connected to a
		 * PipedOutputStream yet
		 */

		// -------------------------------------------------------------
		// Actions initialization
		// -------------------------------------------------------------
		sendAction = new SendAction();
		clearAction = new ClearAction();
		quitAction = new QuitAction();

		/*
		 * Adds a listener to the window so teh application can quit when the
		 * window is closed
		 */
		addWindowListener(new FrameWindowListener());

		// --------------------------------------------------------------------
		// Widgets setup (handled by Window builder)
		// --------------------------------------------------------------------

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton quitButton = new JButton(quitAction);
		toolBar.add(quitButton);

		JButton clearButton = new JButton(clearAction);
		toolBar.add(clearButton);

		Component toolBarSep = Box.createHorizontalGlue();
		toolBar.add(toolBarSep);

		serverLabel = new JLabel(host == null ? "" : host);
		toolBar.add(serverLabel);

		JPanel sendPanel = new JPanel();
		getContentPane().add(sendPanel, BorderLayout.SOUTH);
		sendPanel.setLayout(new BorderLayout(0, 0));
		sendTextField = new JTextField();
		sendTextField.setAction(sendAction);
		sendPanel.add(sendTextField);
		sendTextField.setColumns(10);

		JButton sendButton = new JButton(sendAction);
		sendPanel.add(sendButton, BorderLayout.EAST);

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		// autoscroll textPane to bottom
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane.setViewportView(textPane);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu actionsMenu = new JMenu("Actions");
		menuBar.add(actionsMenu);

		JMenuItem sendMenuItem = new JMenuItem(sendAction);
		actionsMenu.add(sendMenuItem);

		JMenuItem clearMenuItem = new JMenuItem(clearAction);
		actionsMenu.add(clearMenuItem);

		JSeparator separator = new JSeparator();
		actionsMenu.add(separator);

		JMenuItem quitMenuItem = new JMenuItem(quitAction);
		actionsMenu.add(quitMenuItem);

		// -------------------------------------------------------------
		// Document related
		// Gets #document and #documentStyle from the #textPane
		// Initialize default color from document style
		//--------------------------------------------------------------
		document = textPane.getStyledDocument();
		documentStyle = textPane.addStyle("New Style", null);
		defaultColor = StyleConstants.getForeground(documentStyle);
	}

	/**
	 * Display a {@link Message} in the {@link AbstractClientFrame#document}
	 * then insert a {@link Vocabulary#newLine} with user's specific color
	 * ({@link #getColorFromName(String)})
	 * The date part of the message "[yyyy/MM/dd HH:mm:ss]" should be displayed
	 * with default color whereas the "user > message" part should be displayed
	 * with user's specific color ({@link #getColorFromName(String)})
	 * @param message the message to display int the
	 * {@link AbstractClientFrame#document}
	 * @throws BadLocationException if writing in the document fails
	 * @see {@link examples.widgets.ExampleFrame#appendToDocument(String, Color)}
	 * @see java.text.SimpleDateFormat#SimpleDateFormat(String)
	 * @see java.util.Calendar#getInstance()
	 * @see java.util.Calendar#getTime()
	 * @see javax.swing.text.StyleConstants
	 * @see javax.swing.text.StyledDocument#insertString(int, String,
	 * javax.swing.text.AttributeSet)
	 */
	protected void writeMessage(String message) throws BadLocationException
	{
		/*
		 * adds "[yyyy/MM/dd HH:mm:ss] user > message" at the end of the document
		 */
		StringBuffer sb = new StringBuffer();

		sb.append(message);
		sb.append(Vocabulary.newLine);

		// parse TEXT message for name
		String source = parseName(message);
		if ((source != null) && (source.length() > 0))
		{
			/*
			 * Set color for user in document style
			 */
			StyleConstants.setForeground(documentStyle,
			                             getColorFromName(source));
		}

		document.insertString(document.getLength(),
		                      sb.toString(),
		                      documentStyle);

		// Return to default color
		StyleConstants.setForeground(documentStyle, defaultColor);

	}

	/**
	 * Search for user's name in a string formatted as "user > message".
	 * This method is used to extract user's name from text messages
	 * @param message the message to parse
	 * @return user's name or null if there is no user's name (server's
	 * messages)
	 */
	protected String parseName(String message)
	{
		if (message.contains(">") && message.contains("]"))
		{
			int pos1 = message.indexOf(']');
			int pos2 = message.indexOf('>');
			try
			{
				return new String(message.substring(pos1 + 2, pos2 - 1));
			}
			catch (IndexOutOfBoundsException iobe)
			{
				logger.warning("ClientFrame::parseName: index out of bounds");
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Search for message content in a string formatted as "user > content".
	 * This method is used to extract message content from text messages
	 * @param message the message to parse
	 * @return message conetnt or null if there is no message content
	 */
	protected String parseContent(String message)
	{
		if (message.contains(">"))
		{
			int pos = message.indexOf('>');
			try
			{
				return new String(message.substring(pos + 1, message.length()));
			}
			catch (IndexOutOfBoundsException iobe)
			{
				logger
				    .warning("ClientFrame::parseContent: index out of bounds");
				return null;
			}
		}
		else
		{
			return message;
		}
	}

	/**
	 * Action to clear {@link AbstractClientFrame#document} content
	 */
	protected class ClearAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = -8128035390050790728L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public ClearAction()
		{
			putValue(SMALL_ICON,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/erase-16.png")));
			putValue(LARGE_ICON_KEY,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/erase-32.png")));
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_L,
			                                InputEvent.META_MASK));
			putValue(NAME, "Clear");
			putValue(SHORT_DESCRIPTION, "Clear document content");
		}

		/**
		 * Action performing: clears {@link AbstractClientFrame#document}
		 * content
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				document.remove(0, document.getLength());
			}
			catch (BadLocationException ex)
			{
				logger.warning("ClientFrame: clear doc: bad location");
				logger.warning(ex.getLocalizedMessage());
			}
		}
	}

	/**
	 * Action to send message content to server
	 */
	protected class SendAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = -149768461206764688L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public SendAction()
		{
			putValue(SMALL_ICON,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/logout-16.png")));
			putValue(LARGE_ICON_KEY,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/logout-32.png")));
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_S,
			                                InputEvent.META_MASK));
			putValue(NAME, "Send");
			putValue(SHORT_DESCRIPTION, "Send text to server");
		}

		/**
		 * Action performing: retreive {@link ClientFrame#sendTextField} content
		 * if non empty and send it to server
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Obtention du contenu du sendTextField
			String content = sendTextField.getText();

			// logger.fine("Le contenu du textField etait = " + content);

			// envoi du message
			if (content != null)
			{
				if (content.length() > 0)
				{
					sendMessage(content);

					// Effacement du contenu du textfield
					sendTextField.setText("");
				}
			}
		}
	}

	/**
	 * Action to logout from server an quit application
	 */
	private class QuitAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = 704198781702061141L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public QuitAction()
		{
			putValue(SMALL_ICON,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/cancel-16.png")));
			putValue(LARGE_ICON_KEY,
			         new ImageIcon(ClientFrame.class
			             .getResource("/icons/cancel-32.png")));
			putValue(ACCELERATOR_KEY,
			         KeyStroke.getKeyStroke(KeyEvent.VK_Q,
			                                InputEvent.META_MASK));
			putValue(NAME, "Quit");
			putValue(SHORT_DESCRIPTION, "Disconnect from server and quit");
		}

		/**
		 * Action performing: Clears {@link ClientFrame#serverLabel} and send
		 * {@link Vocabulary#byeCmd} to server which should terminate this frame
		 * with the {@link AbstractClientFrame#commonRun} changing to false
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			logger.info("QuitAction: sending bye ... ");

			serverLabel.setText("");
			thisRef.validate();

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				return;
			}

			sendMessage(Vocabulary.byeCmd);
		}
	}

	/**
	 * Class redirecting the window closing event to the {@link QuitAction}
	 */
	protected class FrameWindowListener extends WindowAdapter
	{
		/**
		 * Method trigerred when window is closing
		 * @param e The Window event
		 */
		@Override
		public void windowClosing(WindowEvent e)
		{
			logger.info("FrameWindowListener::windowClosing: sending bye ... ");
			/*
			 * Calls the #quitAction if there is any
			 */
			if (quitAction != null)
			{
				quitAction.actionPerformed(null);
			}
		}
	}

	/**
	 * Client frame's thread run loop: read line from server with #inBR and if
	 * there is a message (text in this case) write it using
	 * #writeMessage(String)
	 */
	@Override
	public void run()
	{
		inBR = new BufferedReader(new InputStreamReader(inPipe));

		String messageIn;

		while (commonRun.booleanValue())
		{
			messageIn = null;
			/*
			 * - Lecture d'une ligne de texte en provenance du serveur avec inBR
			 * Si une exception survient lors de cette lecture on quitte la
			 * boucle.
			 * - Si cette ligne de texte n'est pas nulle on affiche le message
			 * dans le document avec le format voulu en utilisant
			 * #writeMessage(String)
			 * - Après la fin de la boucle on change commonRun à false de
			 * manière synchronisée afin que les autres threads utilisant ce
			 * commonRun puissent s'arrêter eux aussi :
			 * synchronized(commonRun)
			 * {
			 * commonRun = Boolean.FALSE;
			 * }
			 * Dans toutes les étapes si un problème survient (erreur,
			 * exception, ...) on quitte la boucle en ayant au préalable ajouté
			 * un "warning" ou un "severe" au logger (en fonction de l'erreur
			 * rencontrée) et mis le commonRun à false (de manière synchronisé).
			 */
			try
			{
				/*
				 * read from input (blocking call)
				 */
				messageIn = inBR.readLine();
			}
			catch (IOException e)
			{
				logger.warning("ClientFrame: I/O Error reading");
				break;
			}

			if (messageIn != null)
			{
				// write message in #document
				try
				{
					writeMessage(messageIn);
				}
				catch (BadLocationException e)
				{
					logger.warning("ClientFrame: write at bad location: "
					    + e.getLocalizedMessage());
				}
			}
			else // messageIn == null
			{
				break;
			}
		}

		if (commonRun.booleanValue())
		{
			logger
			    .info("ClientFrame::cleanup: changing run state at the end ... ");
			synchronized (commonRun)
			{
				commonRun = Boolean.FALSE;
			}
		}

		cleanup();
	}

	/**
	 * Cleanup: Close {@link #inBR} and calls super cleanup
	 * @see AbstractClientFrame#cleanup()
	 */
	@Override
	public void cleanup()
	{
		logger.info("ClientFrame::cleanup: closing input buffered reader ... ");
		try
		{
			inBR.close();
		}
		catch (IOException e)
		{
			logger.warning("ClientFrame::cleanup: failed to close input reader"
			    + e.getLocalizedMessage());
		}

		super.cleanup();
	}
}
