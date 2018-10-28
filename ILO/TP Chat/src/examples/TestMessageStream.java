package examples;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;

import models.Message;
import models.Message.MessageOrder;

/**
 * {@link Message} stream test
 * @author davidroussel
 */
public class TestMessageStream
{
	private static void randomWait(int max)
	{
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		try
		{
			Thread.sleep(rand.nextInt(max));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Main program
	 * @param args arguments [non utilisé]
	 */
	public static void main(String[] args)
	{
		Vector<Message> messages = new Vector<Message>();
		int delay = 500;

		Date date = Calendar.getInstance().getTime();
		messages.add(new Message("Message de T", "Ténéphore"));
		randomWait(delay);
		messages.add(new Message("Hello", "Zébulon"));
		randomWait(delay);
		messages.add(new Message("ZBulon's in the place", "Zébulon"));
		randomWait(delay);
		messages.add(new Message(date, "ZBulon antidaté", "Zébulon"));
		randomWait(delay);
		messages.add(new Message("Message de contrôle")); // sans auteur

		Consumer<Message> messagePrinter = (Message m) -> System.out.println(m);

		// Regular Message stream
		System.out.println("Whole message stream (unsorted): ");
		messages.stream().forEach(messagePrinter);

		// Whole message stream (sorted by default order: date)
		System.out.println("Whole message stream sorted by date: ");
		messages.stream().sorted().forEach(messagePrinter);

		// Change message ordering
		Message.removeOrder(MessageOrder.DATE);
		Message.addOrder(MessageOrder.AUTHOR);

		System.out.println("Whole message stream sorted by author: ");
		messages.stream().sorted().forEach(messagePrinter);

		// Add message content to sort criteria
		Message.addOrder(MessageOrder.CONTENT);
		System.out.println("Whole message stream sorted by author and content: ");
		messages.stream().sorted().forEach(messagePrinter);

		// Add message date to sort criteria
		Message.addOrder(MessageOrder.DATE);
		System.out.println("Whole message stream sorted by author, content and date: ");
		messages.stream().sorted().forEach(messagePrinter);

		// Predicate for messages author Zébulon
		Predicate<Message> zebulonFilter = (Message m) ->
		{
			if (m != null)
			{
				if (m.hasAuthor())
				{
					if (m.getAuthor().equals("Zébulon"))
					{
						return true;
					}
				}
			}
			return false;
		};

		System.out.println("Filtered(Zébulon) stream sorted by author and content: ");
		messages.stream().sorted().filter(zebulonFilter).forEach(messagePrinter);
		Message.removeOrder(MessageOrder.CONTENT);
		Message.removeOrder(MessageOrder.AUTHOR);
		Message.clearOrders();
		Message.addOrder(MessageOrder.DATE);

		System.out.println("Filtered(Zébulon) stream sorted by date: ");
		messages.stream().filter(zebulonFilter).sorted().forEach(messagePrinter);
	}

}
