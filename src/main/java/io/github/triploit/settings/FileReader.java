package io.github.triploit.settings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader
{
	public static List<String> readFile(String path)
	{
		String line;
		List<String> file = new ArrayList<String>();
		BufferedReader br;

		try
		{
			br = new BufferedReader(new java.io.FileReader(path));

			while ((line = br.readLine()) != null)
			{
				file.add(line);
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("error: file ("+path+") not found.");
		}
		catch (IOException ex)
		{
			System.out.println("error: couldn't read file ("+path+").");
		}

		return file;
	}

	public static String readFileComplete(String file)
	{
		String s = "";

		for (String d : readFile(file))
		{
			s += d + "\n";
		}

		return s;
	}
}
