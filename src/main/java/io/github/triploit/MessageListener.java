package io.github.triploit;

import io.github.triploit.settings.Runtime;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

public class MessageListener extends ListenerAdapter
{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		//These are provided with every event in JDA
		JDA jda = event.getJDA();                       //JDA, the core of the api.
		long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

		//Event specific information
		User author = event.getAuthor();                //The user that sent the message
		Message message = event.getMessage();           //The message that was received.
		MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.

		String msg = message.getContentDisplay();
		boolean bot = author.isBot();

		if (!message.getChannel().getName().contains("bot"))
			return;

		System.out.println("MESSAGE: "+channel.getName()+": "+author.getId()+": "+author.getName()+": "+msg);

		if (author.isBot())
		{
			return;
		}

		if (Runtime.input)
		{
			if (author.getId().equals(Runtime.inputAuthor))
			{
				if (Runtime.stringContainsBlacklist(msg))
				{
					Runtime.input = false;
					channel.sendMessage("<@"+author.getIdLong()+">, du hast ein Wort verwendet, das auf der Blacklist steht: \""+Runtime.getContainedBlackListString(msg)+"\"").queue();
					return;
				}

				if (author.getId().equals(Runtime.config.master_identity))
				{
					if (msg.trim().equals("!") || msg.trim().equals("!cancel"))
					{
						Runtime.input = false;
						channel.sendMessage("Abbruch durch Master-Identität.").queue();
						return;
					}
					else if (msg.trim().equals("!i"))
					{
						Runtime.input = false;
						Runtime.ignore = true;

						channel.sendMessage("Abbruch durch Master-Identität.\nOKI wurde deaktiviert.").queue();
						return;
					}
				}

				if (msg.trim().equals("!") || msg.trim().equals("!cancel"))
				{
					Runtime.input = false;
					channel.sendMessage("Abbruch.").queue();
					return;
				}
				else
				{
					System.out.println("=> Input: "+msg);
					List<String> ans = new ArrayList<String>();

					ans = Arrays.asList(msg.split("~"));

					if (Runtime.inputMessage.getContentDisplay().trim().startsWith("oki"))
						Runtime.brain.addToBrain(Runtime.inputMessage.getContentDisplay().trim().substring(3), ans.toArray());
					else
						Runtime.brain.addToBrain(Runtime.inputMessage.getContentDisplay(), ans.toArray());

					Runtime.input = false;
				}
			}
			else if (msg.toLowerCase().contains("oki") && !msg.toLowerCase().contains("#oki"))
			{
				channel.sendMessage("OKI ist noch für maximal "+Runtime.inputSeconds+" blockiert.");
			}

			return;
		}

		if (msg.toLowerCase().contains("#oki"))
			return;

		if (author.getId().equals(Runtime.config.master_identity))
		{
			System.out.println("=> Master Identity");
			msg = msg.trim();

			if (msg.equals("!i"))
			{
				if (Runtime.ignore)
				{
					channel.sendMessage("Master-Identität schaltete OKI an.").queue();
					Runtime.ignore = false;
				}
				else
				{
					channel.sendMessage("Master-Identität schaltete OKI aus.").queue();
					Runtime.ignore = true;
				}

				return;
			}
			else if (msg.startsWith("!bl:"))
			{
				Runtime.addBlacklist(Runtime.brain.naked(msg.substring(3)));
				return;
			}
		}

		if (msg.toLowerCase().contains("oki"))
		{
			if (Runtime.ignore)
			{
				return;
			}

			if (Runtime.stringContainsBlacklist(msg))
			{
				channel.sendMessage("<@"+author.getIdLong()+">, du hast ein Wort verwendet, das auf der Blacklist steht: \""+Runtime.getContainedBlackListString(msg)+"\"").queue();
				return;
			}

			if (message.getMentionedUsers().size() != 0 &&
					message.getMentionedMembers().size() != 0 &&
					message.getMentionedRoles().size() != 0)
			{
				channel.sendMessage("<@"+author.getIdLong()+">, es ist dir nicht erlaubt zu taggen!").queue();
				return;
			}

			String ans = Runtime.brain.responseOn(msg, message);

			if (ans != null)
				channel.sendMessage(ans).queue();
			else
			{
				channel.sendMessage(msg).queue();

				Runtime.inputMessage = message;
				Runtime.inputAuthor = author.getId();
				Runtime.input = true;
				Runtime.inputID++;

				if (Runtime.inputID > 100)
					Runtime.inputID = 0;

				(new InputTask()).start();
			}
		}
	}
}