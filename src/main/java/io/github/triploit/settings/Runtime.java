package io.github.triploit.settings;

import io.github.triploit.settings.files.ConfigFile;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Runtime
{
	public static ConfigFile config = new ConfigFile();
	public static List<String> Blacklist = new ArrayList<String>();
	public static List<String> bannedUsers = new ArrayList<String>();
	public static io.github.triploit.brain.Brain brain = new io.github.triploit.brain.Brain();
	public static boolean ignore = false;

	public static boolean input = false;
	public static String inputAuthor = "";
	public static Message inputMessage;
	public static int inputSeconds;
	public static int inputID = 0;

	public static void loadBlackList()
	{
		Blacklist = FileReader.readFile(config.blacklist_file);
	}

	public static boolean stringContainsBlacklist(String msg)
	{
		loadBlackList();

		for (String w : Blacklist)
		{
			if (brain.naked(msg).contains(brain.naked(w)))
				return true;
		}

		return false;
	}

	public static String getContainedBlackListString(String msg)
	{
		for (String w : Blacklist)
		{
			if (brain.naked(msg).contains(brain.naked(w)))
				return w;
		}

		return "<NULL>";
	}

	public static void addBlacklist(String word)
	{
		(new FileWriter()).append(config.blacklist_file, word+"\n");
	}
}
