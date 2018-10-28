package examples;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Exemple de classe implémentant un Runnable et lancé dans un Thread
 *
 * @author davidroussel
 */
public class RunnableExample
{
	/**
	 * Simple Runnable class counting to be executed in a thread.
	 * Counter counts from 0 to max. When count reaches max the run loop stops
	 * @author davidroussel
	 */
	protected static class Counter implements Runnable
	{
		/**
		 * Number of existing Counter
		 */
		private static int CounterNumber = 0;

		/**
		 * Index of current counter
		 */
		private int number;

		/**
		 * Count of this counter
		 */
		private int count;

		/**
		 * Max value to count to
		 */
		private int max;

		/**
		 * Constructor
		 * @param max max count value to reach
		 */
		public Counter(int max)
		{
			number = ++CounterNumber;
			count = 0;
			this.max = max;
		}


		/* (non-Javadoc)
		 * @see java.lang.Object#finalize()
		 */
		@Override
		protected void finalize() throws Throwable
		{
			CounterNumber--;
		}

		/**
		 * Main run loop: while {@link #max} is not reached by {@link #count},
		 * it is incremented, current object is printed on the
		 * console, then prioriy is given to other threads (if any)
		 */
		@Override
		public void run()
		{
			while (count < max)
			{
				count++;

				System.out.println(this);

				// give priority to other threads
				Thread.yield();
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new String("Counter #" + number + " = " + count);
		}
	}

	/**
	 * Counters collection to launch in threads
	 */
	protected Collection<Counter> counters;

	/**
	 * Threads collection to run Counters
	 */
	protected Collection<Thread> threads;

	/**
	 * Constructor.
	 * Creates a collection of Counters, then place these counters in a
	 * collection of threads
	 */
	public RunnableExample(int nbCounters)
	{
		counters = new ArrayList<Counter>(nbCounters);
		threads = new ArrayList<Thread>(nbCounters);

		for (int i = 0; i < nbCounters; i++)
		{
			Counter c = new Counter(10);
			counters.add(c);

			Thread t = new Thread(c);
			threads.add(t);
		}
	}

	/**
	 * Launch all threads in the collection
	 */
	public void launch()
	{
		for (Thread t : threads)
		{
			t.start();
		}
	}

	/**
	 * Wait for all threads in the collection to finish
	 */
	public void terminate()
	{
		for (Thread t : threads)
		{
			try
			{
				t.join();
			}
			catch (InterruptedException e)
			{
				System.err.println("Thread" + t + " join interrupted");
				e.printStackTrace();
			}
		}

		System.out.println("All threads terminated");
	}

	/**
	 * Main program.
	 * @param args program arguments to read number of counters to
	 * launch
	 */
	public static void main(String[] args)
	{
		int nbCounters = 3;
		// First argument should be the number of threads to launch
		if (args.length > 0)
		{
			int value;
			try
			{
				value = Integer.parseInt(args[0]);
				if (value > 0)
				{
					nbCounters = value;
				}
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("Error reading number of counters");
			}
		}

		RunnableExample runner = new RunnableExample(nbCounters);

		runner.launch();

		System.out.println("All threads launched");

		runner.terminate();
	}
}
