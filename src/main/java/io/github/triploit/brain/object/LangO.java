package io.github.triploit.brain.object;

import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class LangO
{
	private List<String> users = new ArrayList<String>();
	private List<String> answers = new ArrayList<String>();

	public LangO() {}

	public LangO(List<String> users, List<String> answers)
	{
		this.users.clear();
		this.answers.clear();

		this.users.addAll(users);
		this.answers.addAll(answers);
	}

	public List<String> getAnswers()
	{
		return answers;
	}

	public List<String> getUsers()
	{
		return users;
	}

	public void addAnswer(String a)
	{
		answers.add(a);
	}

	public void addUser(String u)
	{
		answers.add(u);
	}

	public void setAnswers(List<String> answers)
	{
		this.answers = answers;
	}

	public void setUsers(List<String> users)
	{
		this.users = users;
	}
}
