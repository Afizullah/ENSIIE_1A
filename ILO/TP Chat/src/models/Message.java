package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * Class containing a message sent by the server.
 * A user's message contains:
 * <ul>
 * <li>message's date</li>
 * <li>message's content</li>
 * <li>and eventual message's author</li>
 * </ul>
 * Messages are comparable so they can be compared with
 * {@link #compareTo(Message)}
 * method and also sorted in a collection.
 * Sorting criteria can be changed dynamically with
 * {@link #addOrder(MessageOrder)} and {@link #removeOrder(MessageOrder)}
 * methods
 * @author davidroussel
 */
public class Message implements Serializable, Comparable<Message>
{
	/**
	 * Serial version ID for serialization
	 */
	private static final long serialVersionUID = 2253762081610943097L;

	/**
	 * Les différents ordres de comparaison possibles pour un message
	 * Comparison criteria enumeration
	 */
	public enum MessageOrder
	{
		/**
		 * Author's name for comparison
		 */
		AUTHOR,
		/**
		 * Message date comparison
		 */
		DATE,
		/**
		 * Message content comparison
		 */
		CONTENT;

		/**
		 * Comparison criterium string representation
		 * @return a string showing the kind of comparison
		 */
		@Override
		public String toString()
		{
			switch (this)
			{
				case AUTHOR:
					return new String("Author");
				case DATE:
					return new String("Date");
				case CONTENT:
					return new String("Content");
			}
			throw new AssertionError("MessageOrder: unknown order: " + this);
		}
	}

	/**
	 * Comparison critera set (initialized to empty).
	 * This collection should contain only one and only one instnce of each
	 * possible {@link MessageOrder} in any possible order.
	 */
	protected static Vector<MessageOrder> orders = new Vector<MessageOrder>();

	/**
	 * Message receiving date
	 */
	private Date date;

	/**
	 * Message content
	 */
	private String content;

	/**
	 * Message author (optional)
	 * A Server message does not need to have an author, but all users messages
	 * should have an author
	 */
	private String author;

	/**
	 * Date format to use to print message date
	 */
	protected static SimpleDateFormat dateFormat =
	    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * Constructor
	 * @param date message receiving date
	 * @param content message content
	 * @param author message authoe (may be null on server's messages)
	 */
	public Message(Date date, String content, String author)
	{
		/*
		 * 	- initialize this.date with date (iff it is not null,
		 * 	otherwise use current time from Calendar)
		 * 	- Initialize this.content with content (iff it is not null,
		 * 	otherwise use empty String
		 * 	- initialize this.author with author (which can be null)
		 * DONE
		 */
		// date should never be null
		if (date != null) {
			this.date = date;
		}
		else {
			this.date = Calendar.getInstance().getTime();
		}
		// content should never be null
		if (content != null) {
			this.content = content;
		}
		else {
			content="";
		}
		this.author = author;
	}

	/**
	 * Constructor with no author (for server's messages)
	 * @param date message receiving date
	 * @param content message content
	 */
	public Message(Date date, String content)
	{
		this(date, content, null);
	}

	/**
	 * Constructor with no date (current date is then used as message's date)
	 * @param content message content
	 * @param author message authoe (may be null on server's messages)
	 * @see Calendar#getInstance()
	 * @see Calendar#getTime()
	 */
	public Message(String content, String author)
	{
		this(null, content, author);
	}

	/**
	 * Constructor with only content. (author is null and date will be set to current date)
	 * @param content message content
	 * @see Calendar#getInstance()
	 * @see Calendar#getTime()
	 */
	public Message(String content)
	{
		this(content, null);
	}

	/**
	 * Message date accessor
	 * @return the receiving date of the message
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Formatted date string accessor
	 * @return formatted string of the message's date
	 */
	public String getFormattedDate()
	{
		return dateFormat.format(date);
	}

	/**
	 * Message content accessor
	 * @return the message content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * Message author accessor
	 * @return the author's name or null if there is no author
	 */
	public String getAuthor()
	{
		return author;
	}

	/**
	 * Indicates if a message has an author
	 * @return true if the messag has an author, false otherwise
	 */
	public boolean hasAuthor()
	{
		return author != null;
	}

	/**
	 * Date formatter accessor
	 * @return the formatted used to format date
	 */
	public static SimpleDateFormat getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * Message hashcode.
	 * Can be used in {@link HashSet} for instance.
	 * @return a hash value based on date, author and content hashcodes.
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int hash = date.hashCode();
		hash = (prime * hash) + content.hashCode();
		if (author != null)
		{
			hash = (prime * hash) + author.hashCode();
		}
		return hash;
	}

	/**
	 * Message comparison with another object
	 * @param the other object to compare
	 * @return true if the other object is also a Message and has the same date,
	 * content and author
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj == this)
		{
			return true;
		}

		if (obj instanceof Message)
		{
			Message m = (Message) obj;

			if (date.equals(m.date))
			{
				if (content.equals(m.content))
				{
					if (author != null)
					{
						return author.equals(m.author);
					}
					else
					{
						return m.author == null;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Message string representation
	 * @return a new string formatted as:
	 * "[yyyy/mm/dd HH:MM:SS] author > message content"
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");

		sb.append(dateFormat.format(date));
		sb.append("] ");
		if (author != null)
		{
			sb.append(author);
			sb.append(" > ");
		}
		sb.append(content);

		return sb.toString();
	}

	/**
	 * String representation of current order criteria
	 * @return a new string representing current sorting criteria
	 */
	public static String toStringOrder()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (Iterator<MessageOrder> it = orders.iterator(); it.hasNext(); )
		{
			sb.append(it.next().toString());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 3 Way comparison with other message using compare criteria stored in
	 * {@link #orders}
	 * @param m the message to compare to
	 * @return -1 if the current message is considered smaller as message m
	 * according to current order criteria stored in {@link #orders}, 0 if
	 * current message is considered equal and +1 if current message is
	 * considered bigger
	 */
	@Override
	public int compareTo(Message m)
	{
		// Default order is no order : all messages are equal
		int compare = 0;

		if (!orders.isEmpty())
		{
			for (Iterator<MessageOrder> it = orders.iterator(); it.hasNext();)
			{
				MessageOrder criterium = it.next();
				switch (criterium)
				{
					case AUTHOR:
						/*
						 * Compare authors (which are already Comparable)
						 * but we need to consider the null author case
						 * A message with an non null author
						 * will be considered as bigger than
						 * a message without author
						 * e.g. this.author > m.author then compare = 1
						 * DONE
						 */
						if (this.author == null) {
							compare = 0;
						}
						else if (m.author == null){
							compare = 1;
						}
						else {
							compare = this.author.compareTo(m.author);
						}
						break;
					case DATE:
						/*
						 * Compare Dates (which are already Comparable)
						 */
						compare = this.date.compareTo(m.date);
						break;
					case CONTENT:
						/*
						 * Content
						 */
						compare = this.content.compareTo(m.content);
						break;
					default:
						break;
				}
				/*
				 * If current criteria is enough to differentiate messages then
				 * break
				 */
				if (compare != 0)
				{
					break;
				}
			}
			/*
			 * Compare loop ended without returning, all criteria
			 * compared to 0 which means messages are equal
			 */
		}

		return compare;
	}

	/**
	 * Add order criterium to criteria in {@link #orders} iff not already
	 * present
	 * @param o the criterium to add
	 * @return true if such criterium was not already present and has been added
	 * to {@link #orders}, false otherwise
	 */
	public static boolean addOrder(MessageOrder o)
	{
		/*
		 * Add o the orders if it was not already in and return true
		 * DONE
		 */
		if (!(orders.contains(o))) {
			orders.addElement(o);;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Remove order criterium from criteria in {@link #orders}
	 * @param o the criterium to remove
	 * @return true if the criterium has been removed from criteria,
	 * false otherwise
	 */
	public static boolean removeOrder(MessageOrder o)
	{
		/*
		 * Remove o from the orders if it was in and return true
		 * DONE
		 */
		if (orders.contains(o)) {
			orders.removeElement(o);
			return true;
		}
		else {
		return false;
		}
	}

	/**
	 * Current number of criteria in {@link #orders}
	 * @return the number of criteria in {@link #orders}
	 */
	public static int orderSize()
	{
		return orders.size();
	}

	/**
	 * Clear all criteria in {@link #orders}
	 */
	public static void clearOrders()
	{
		/*
		 * Clear orders
		 * DONE
		 */
		orders.clear();
	}
}
