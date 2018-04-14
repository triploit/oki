package io.github.triploit.settings;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter
{
	public void append(String file, String line)
	{
		try
		{
			BufferedWriter output = new BufferedWriter(new java.io.FileWriter(file, true));
			output.write(line);
			output.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
