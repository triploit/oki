package io.github.triploit;

import io.github.triploit.settings.Runtime;
import io.github.triploit.settings.SettingsLoader;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main
{
	public static void main(String[] args)
	{
		SettingsLoader s = new SettingsLoader(args[0]);
		s.load();

		try
		{
			JDA jda = new JDABuilder(AccountType.BOT)
					.setToken(Runtime.config.bot_token)           //The token of the account that is logging in.
					.addEventListener(new MessageListener())  //An instance of a class that will handle events.
					.buildBlocking();  //There are 2 ways to login, blocking vs async. Blocking guarantees that JDA will be completely loaded.
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
