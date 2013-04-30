package kr.kkiro.projects.bukkit.entityprotect;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import kr.kkiro.projects.bukkit.entityprotect.utils.FileInteract;

import org.bukkit.configuration.file.YamlConfiguration;

public class EPConfig {

	public static final String path = "config.yml";

	private File yaml;
	private YamlConfiguration config;
	private YamlConfiguration defaultConfig;
	private Logger logger;
	private static InputStream defaultYaml;

	public EPConfig(EntityProtect plugin) throws Exception {
		if (defaultYaml == null)
			defaultYaml = getClass().getResourceAsStream("config.yml");
		if (plugin == null)
			throw new Exception("Plugin can't be null");
		logger = plugin.getLogger();
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		yaml = new File(plugin.getDataFolder(), path);
		if (!yaml.exists()) {
			logger.info("Creating config file...");
			if (!FileInteract.copy(defaultYaml, yaml)) {
				logger.severe("Error occurred while copying files");
				throw new Exception("Error occurred while copying files");
			} else {
				logger.info("A new config copied");
			}
		}
		config = new YamlConfiguration();
		config.load(yaml);
		if (!config.getBoolean("general.enabled", false)) {
			logger.info("EntityProtect is not enabled!");
			throw new Exception("ConfigRequired");
		}
		defaultConfig = new YamlConfiguration();
		defaultConfig.load(defaultYaml);
	}

	public void close() {
		try {
			defaultYaml.close();
			defaultYaml = null;
			config.save(yaml);
		} catch (Exception e) {
		}
	}

	public String readString(String key, String value) {
		if (!contains(key))
			return value;
		else
			return readString(key);
	}

	public boolean readBoolean(String key, boolean value) {
		if (!contains(key))
			return value;
		else
			return readBoolean(key);
	}

	public int readInt(String key, int value) {
		if (!contains(key))
			return value;
		else
			return readInt(key);
	}

	public boolean contains(String key) {
		return config.contains(key);
	}

	public String readString(String key) {
		if (!config.contains(key))
			config.set(key, defaultConfig.getString(key));
		return config.getString(key);
	}

	public boolean readBoolean(String key) {
		if (!config.contains(key))
			config.set(key, defaultConfig.getBoolean(key));
		return config.getBoolean(key);
	}

	public int readInt(String key) {
		if (!config.contains(key))
			config.set(key, defaultConfig.getInt(key));
		return config.getInt(key);
	}
}