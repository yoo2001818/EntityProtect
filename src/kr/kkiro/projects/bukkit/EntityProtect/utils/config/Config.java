package kr.kkiro.projects.bukkit.EntityProtect.utils.config;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private static Config instance;
	private EntityProtect plugin;

	public Config() {
		plugin = EntityProtect.getInstance();
		plugin.saveDefaultConfig();
		plugin.getConfig().options().copyDefaults(true);
	}

	public static void init() {
		instance = new Config();
	}

	public static Config getInstance() {
		return instance;
	}

	public FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	public static boolean getBoolean(String path) {
		return getInstance().getConfig().getBoolean(path);
	}

	public static String getString(String path) {
		return getInstance().getConfig().getString(path);
	}

	public static int getInt(String path) {
		return getInstance().getConfig().getInt(path);
	}
}
