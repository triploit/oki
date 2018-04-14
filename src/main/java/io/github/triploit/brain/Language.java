package io.github.triploit.brain;

import io.github.triploit.brain.object.LangO;
import io.github.triploit.brain.object.Token;
import io.github.triploit.brain.object.TokenType;
import io.github.triploit.settings.Runtime;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language
{
	public static String makeItAble(String text, Message c)
	{
		text = Runtime.brain.replaceVariables(text, c);

		text = text.replace("\\\\", "<*T2A4_TMP_TBS*>");
		text = text.replace("\\\"", "<*T1A3_TMP_AFZ*>");
		text = text.replace("<*br*>", "\n");
		text = text.trim();

		return text.trim();
	}

	public static List<Token> tokenize(String text, Message c)
	{
		List<Token> l = new ArrayList<Token>();

		String type = "";
		String tmp = "";

		text = makeItAble(text, c);

		if (text.charAt(0) == '(')
		{
			for (int i = 1; i < text.length(); i++)
			{
				if (text.charAt(i) == '"')
				{
					if (type.equals(""))
					{
						tmp = "";
						type = "str";
					}
					else if (type.equals("str"))
					{
						type = "";
						l.add(new Token(TokenType.TT_STRING, tmp
								.replace("<*T1A3_TMP_AFZ*>", "\"")
								.replace("<*T2A4_TMP_TBS*>", "\\")));

						tmp = "";
					}

					continue;
				}

				if (type.equals("str"))
				{
					tmp += text.charAt(i);
					continue;
				}

				if (text.charAt(i) == ';')
				{
					l.add(new Token(TokenType.TT_SEMICOLON, ";"));
					tmp = "";
					continue;
				}

				if (text.charAt(i) == ':')
				{
					l.add(new Token(TokenType.TT_DOUBLE_POINT, ":"));
					tmp = "";
					continue;
				}

				if (text.charAt(i) == ')')
				{
					return l;
				}

				tmp += text.charAt(i);
			}
		}

		return null;
	}

	public static List<LangO> parse(List<Token> tokens)
	{
		List<LangO> lr = new ArrayList<LangO>();
		List<String> usr = new ArrayList<String>();
		List<String> ans = new ArrayList<String>();
		String type = "users";

		for (Token t : tokens)
		{
			if (t.getType() == TokenType.TT_DOUBLE_POINT)
			{
				type = "answer";
			}

			if (t.getType() == TokenType.TT_STRING)
			{
				if (type.equals("users"))
				{
					usr.add(t.getValue());
				}
				else
				{
					ans.add(t.getValue());
				}
			}

			if (t.getType() == TokenType.TT_SEMICOLON)
			{
				type = "users";
				lr.add(new LangO(usr, ans));

				ans.clear();
				usr.clear();
			}
		}

		return lr;
	}
}
