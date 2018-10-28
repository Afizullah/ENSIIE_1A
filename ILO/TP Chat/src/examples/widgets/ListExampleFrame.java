package examples.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Window example showing:
 * <ul>
 * <li>A list containing selectable colored elements with a popup menu
 * containing actions to add and remove elements along with an action to clear
 * selected elements</li>
 * <li>A text pane showing current elements selection</li>
 * </ul>
 * @author davidroussel
 */
public class ListExampleFrame extends JFrame
{
	/**
	 * Serial ID (because {@link TransferHandler} is serializable)
	 */
	private static final long serialVersionUID = -7970541114190577807L;

	/**
	 * New line separator for this OS
	 */
	private static String newline = System.getProperty("line.separator");

	/**
	 * The underlying list model to be associated with a {@link JList}.
	 * elements added or removed from this model will automatically be
	 * reflected in the {@link JList} associated to this model.
	 */
	private DefaultListModel<String> elements = new DefaultListModel<String>();

	/**
	 * The undelying list selection model to get from an existing
	 * {@link JList}.
	 * Keeps track of the indices of selected elements in the {@link JList}
	 * displaying these elements.
	 * Selections or deselections performed in this model will
	 * automatically be reflected in the associated {@link JList}.
	 */
	private ListSelectionModel selectionModel = null;

	/**
	 * Text area used to display messages (in this case messages are
	 * the current selection model data)
	 */
	private JTextArea output = null;

	/**
	 * Action to perform when selected elements are removed from the list
	 */
	private final Action removeAction = new RemoveItemAction();

	/**
	 * Action to perform for deselecting all elements from the list
	 */
	private final Action clearSelectionAction = new ClearSelectionAction();

	/**
	 * Action to perform for adding elements to the list
	 */
	private final Action addAction = new AddAction();

	/**
	 * Constructor
	 * @throws HeadlessException when code that is dependent on a keyboard,
	 * display, or mouse is called in an environment that does not support a
	 * keyboard, display, or mouse
	 */
	public ListExampleFrame() throws HeadlessException
	{
		super(); // implicit
		elements.addElement("Ténéphore");
		elements.addElement("Zébulon");
		elements.addElement("Zéphirine");
		elements.addElement("Uriel");
		elements.addElement("Philomène");

		setPreferredSize(new Dimension(600, 400));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JScrollPane textScrollPane = new JScrollPane();
		getContentPane().add(textScrollPane, BorderLayout.CENTER);

		output = new JTextArea();
		textScrollPane.setViewportView(output);

		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(200, 10));
		getContentPane().add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JButton btnClearSelection = new JButton("Clear Selection");
		btnClearSelection.setAction(clearSelectionAction);
		leftPanel.add(btnClearSelection, BorderLayout.NORTH);

		JScrollPane listScrollPane = new JScrollPane();
		leftPanel.add(listScrollPane, BorderLayout.CENTER);

		JList<String> list = new JList<String>(elements);
		listScrollPane.setViewportView(list);
		list.setName("Elements");
		list.setBorder(UIManager.getBorder("EditorPane.border"));
		list.setSelectedIndex(0);
		list.setCellRenderer(new ColorTextRenderer());

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);

		JMenuItem mntmAdd = new JMenuItem(addAction);
		mntmAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.META_MASK));
		popupMenu.add(mntmAdd);

		JMenuItem mntmRemove = new JMenuItem(removeAction);
		popupMenu.add(mntmRemove);

		JSeparator separator = new JSeparator();
		popupMenu.add(separator);

		JMenuItem mntmClearSelection = new JMenuItem(clearSelectionAction);
		popupMenu.add(mntmClearSelection);

		selectionModel = list.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				int firstIndex = e.getFirstIndex();
				int lastIndex = e.getLastIndex();
				boolean isAdjusting = e.getValueIsAdjusting();
				/*
				 * isAdjusting remains true while events like drag n drop are
				 * still processed and becomes false afterwards.
				 */
				if (!isAdjusting)
				{
					output.append("Event for indexes " + firstIndex + " - "
						+ lastIndex + "; selected indexes:");

					if (lsm.isSelectionEmpty())
					{
						removeAction.setEnabled(false);
						clearSelectionAction.setEnabled(false);
						output.append(" <none>");
					}
					else
					{
						removeAction.setEnabled(true);
						clearSelectionAction.setEnabled(true);
						// Find out which indexes are selected.
						int minIndex = lsm.getMinSelectionIndex();
						int maxIndex = lsm.getMaxSelectionIndex();
						for (int i = minIndex; i <= maxIndex; i++)
						{
							if (lsm.isSelectedIndex(i))
							{
								output.append(" " + i);
							}
						}
					}
					output.append(newline);
				}
				else
				{
					// Still adjusting ...
					output.append("Processing ..." + newline);
				}
			}
		});
	}

	/**
	 * Color Text renderer for drawing list's elements in colored text
	 * @author davidroussel
	 */
	public static class ColorTextRenderer extends JLabel
		implements ListCellRenderer<String>
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = 743494506056117662L;

		/**
		 * Text color
		 */
		private Color color = null;

		/**
		 * Customized rendering for a ListCell with a color obtained from
		 * the hashCode of the string to display
		 * @see
		 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
		 * .JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(
			JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus)
		{
			color = list.getForeground();
			if (value != null)
			{
				if (value.length() > 0)
				{
					color = new Color(value.hashCode()).darker();
				}
			}
			setText(value);
			if (isSelected)
			{
				setBackground(color);
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(color);
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}

	/**
	 * Adds a popup menu to a component
	 * @param component the parent component of the popup menu
	 * @param popup the popup menu to add
	 */
	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/**
	 * Action for removing elements from the list
	 */
	private class RemoveItemAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = -7283431690274175994L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public RemoveItemAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_MASK));
			putValue(SMALL_ICON, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/remove_user-16.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/remove_user-32.png")));
			putValue(NAME, "Remove");
			putValue(SHORT_DESCRIPTION, "Removes item from list");
		}

		/**
		 * Action performing
		 * Remove selected elements in {@link ListExampleFrame#elements}
		 * based on the selections of {@link ListExampleFrame#selectionModel}
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			output.append("Remove action triggered for indexes : ");
			int minIndex = selectionModel.getMinSelectionIndex();
			int maxIndex = selectionModel.getMaxSelectionIndex();
			Stack<Integer> toRemove = new Stack<Integer>();
			for (int i = minIndex; i <= maxIndex; i++)
			{
				if (selectionModel.isSelectedIndex(i))
				{
					output.append(" " + i);
					toRemove.push(new Integer(i));
				}
			}
			output.append(newline);
			while (!toRemove.isEmpty())
			{
				int index = toRemove.pop().intValue();
				output.append("removing element: "
					+ elements.getElementAt(index) + newline);
				elements.remove(index);
			}
		}
	}

	/**
	 * Action for clearing selection in the list
	 */
	private class ClearSelectionAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = -700602366347015759L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public ClearSelectionAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.META_MASK));
			putValue(LARGE_ICON_KEY, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/delete_sign-32.png")));
			putValue(SMALL_ICON, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/delete_sign-16.png")));
			putValue(NAME, "Clear selection");
			putValue(SHORT_DESCRIPTION, "Unselect selected items");
		}

		/**
		 * Action performing: clears selection in
		 * {@link ListExampleFrame#selectionModel}
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			output.append("Clear selection action triggered" + newline);
			selectionModel.clearSelection();
		}
	}

	/**
	 * Action to perform for adding element to the list
	 */
	private class AddAction extends AbstractAction
	{
		/**
		 * Serial ID because enclosing class is serializable ?
		 */
		private static final long serialVersionUID = 3189171161651612574L;

		/**
		 * Constructor.
		 * Sets name, description, icons and also action's shortcut
		 */
		public AddAction()
		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_MASK));
			putValue(SMALL_ICON, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/add_user-16.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(ListExampleFrame.class.getResource("/examples/icons/add_user-32.png")));
			putValue(NAME, "Add...");
			putValue(SHORT_DESCRIPTION, "Add item");
		}

		/**
		 * Action performing: add a new element to
		 * {@link ListExampleFrame#elements}
		 * @param e the event that triggered this action [not used]
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			output.append("Add action triggered" + newline);
			String inputValue = JOptionPane.showInputDialog("New item name");
			if (inputValue != null)
			{
				if (inputValue.length() > 0)
				{
					elements.addElement(inputValue);
				}
			}
		}
	}
}
