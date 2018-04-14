package io.github.triploit.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.triploit.settings.files.ConfigFile;

public class SettingsLoader
{
	private String config;

	public SettingsLoader(String settings_config)
	{
		config = settings_config;
	}

	public void load()
	{
		String settings = FileReader.readFileComplete(config);
		Gson gb = new GsonBuilder().create();
		Runtime.config = gb.fromJson(settings, ConfigFile.class);
	}
}
