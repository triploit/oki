package io.github.triploit.brain;

import io.github.triploit.brain.object.LangO;
import io.github.triploit.brain.object.Token;
import io.github.triploit.settings.FileReader;
import io.github.triploit.settings.FileWriter;
import io.github.triploit.settings.Runtime;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

public class Brain
{
	public Brain()
	{}

	private String replaceFromNotifier(String outp)
	{
		outp = outp.replace("\n", "<*br*>");
		outp = outp.replace("\\~", "<*tilde*>");
		outp = outp.replace("$$ ", "<*dollar*>");
		return outp;
	}

	public String naked(String message)
	{
		String outp = message;
		outp = outp.trim();
		outp = outp.toLowerCase();

		outp = outp.replace("  ", " ");
		outp = outp.replace(",,", ",");
		outp = outp.replace(";;", ";");
		outp = outp.replace("::", ":");

		outp = outp.replace("..", ".");
		outp = outp.replace("!!", "!");
		outp = outp.replace("??", "?");

		outp = outp.replace(" ?", "?");
		outp = outp.replace("? ", "?");

		outp = outp.replace(" !", "!");
		outp = outp.replace("! ", "!");

		outp = outp.replace(" .", ".");
		outp = outp.replace(". ", ".");

		outp = outp.replace(" ;", ";");
		outp = outp.replace("; ", ";");

		outp = outp.replace(" ,", ",");
		outp = outp.replace(", ", ",");

		outp = outp.replace(" :", ":");
		outp = outp.replace(": ", ":");

		outp = outp.replace(" oki", "");
		outp = outp.replace("oki ", "");
		outp = outp.replace("oki", "");

		outp = outp.replace(",", "");
		outp = outp.replace(";", "");
		outp = outp.replace(".", "");
		outp = outp.replace(":", "");
		outp = outp.replace("  ", " ");

		outp = replaceFromNotifier(outp);
		outp = outp.trim();

		return outp;
	}

	public String responseOn(String msg, Message c)
	{
		msg = naked(msg);
		List<String> file = FileReader.readFile(Runtime.config.brain_file);

		int i = 0;

		for (i = 0; i < file.size(); i++)
		{
			file.set(i, file.get(i).trim());
		}

		for (i = 0; i < file.size(); i++)
		{
			if (file.get(i).startsWith("Q:"))
			{
				String q = file.get(i).substring(2);

				if (!msg.equals(q) && !msg.equals(q+"?"))
					continue;

				List<String> ans = new ArrayList<String>();

				i++;
				while (true)
				{
					if (i >= file.size()) break;

					if (file.get(i).startsWith("A:"))
					{
						String a = file.get(i).substring(2);

						if (a.trim().startsWith("!{c}"))
						{
							System.out.println("AHA!");
							a = a.trim().substring(4);

							List<Token> toks = Language.tokenize(a, c);

							if (toks == null)
								return ("Fehler im Skript!\n" +
										"\n```\n" +
										a +
										"\n```");

							List<LangO> tmp = Language.parse(toks);

							if (tmp == null)
								return ("Fehler im Skript!\n" +
										"\n```\n" +
										a +
										"\n```");

							boolean found = false;

							for (LangO l : tmp)
							{
								for (String u : l.getUsers())
								{
									if (u.equals(c.getAuthor().getName()))
									{
										found = true;
										ans.addAll(l.getAnswers());
										break;
									}

									if (u.equals("<other>") && !found)
									{
										ans.addAll(l.getAnswers());
									}
								}

								if (found) break;
							}
						}
						else
							ans.add(a);

						i++;
					}
					else
					{
						break;
					}
				}

				String d = replaceVariables(ans.get((int) (Math.random() * ans.size())), c);


				return d;
			}
		}

		return null;
	}

	public String replaceVariables(String s, Message c)
	{
		s = s.replace("$time", "<NICHT EINGEFÜGT>");
		s = s.replace("$master", c.getChannel().getJDA().getUserById(Runtime.config.master_identity).getName());
		s = s.replace("$sender", c.getAuthor().getName());
		s = s.replace("$user", c.getAuthor().getName());


		s = s.replace("<*br*>", "\n");
		s = s.replace("<*tilde*>", "\\~");
		s = s.replace("<*dollar*>", "$$ ");

		return s;
	}

	public void addToBrain(String inp, Object[] ans)
	{
		inp = naked(inp).trim();
		String a = "Q:"+inp+"\n";

		for (Object o : ans)
		{
			a += "A:"+o.toString()+"\n";
		}

		(new FileWriter()).append(Runtime.config.brain_file, a);
	}
}
