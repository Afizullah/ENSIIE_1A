package examples;
import java.awt.EventQueue;

import examples.widgets.ExampleFrame;


/**
 * Main program launching a {@link ExampleFrame}
 * @author davidroussel
 *
 */
public class RunExampleFrame
{
	/**
	 * Main program
	 * @param args arguments [not used]
	 */
	public static void main(String[] args)
	{
		if (System.getProperty("os.name").startsWith("Mac OS"))
		{
			// Use top screen menu instead of application window menu
			System.setProperty("apple.laf.useScreenMenuBar", "true");
	        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name");
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
					ExampleFrame frame = new ExampleFrame();
					frame.pack();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
