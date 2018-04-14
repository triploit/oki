package io.github.triploit;

import io.github.triploit.settings.Runtime;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;


public class InputTask extends Thread
{
	@Override
	public void run()
	{
		String author = Runtime.inputAuthor;
		Message message = Runtime.inputMessage;
		int ID = Runtime.inputID;

		try
		{
			System.out.println("TimerTask started.");

			for (int i = 0; i <= 15; i++)
			{
				Runtime.inputSeconds = i;
				Thread.sleep(1000);
			}
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}

		if (Runtime.input && Runtime.inputAuthor.equals(author) && ID == Runtime.inputID)
		{
			message.getChannel().sendMessage("<@"+author+">, du hast zu lange gebraucht, um eine Nachricht einzugeben.").queue();
			Runtime.input = false;
			Runtime.inputSeconds = 0;
		}

		System.out.println("TimerTask ended.");
	}
}
