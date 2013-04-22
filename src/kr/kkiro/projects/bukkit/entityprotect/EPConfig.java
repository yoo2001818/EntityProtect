package kr.kkiro.projects.bukkit.entityprotect;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

public class EPConfig {
	
	public static final String path = "config.yml";
	
	private File yaml;
	private EntityProtect plugin;
	private YamlConfiguration config;
	private Logger logger;
	
	public EPConfig(EntityProtect plugin) throws Exception {
		if(plugin == null) throw new Exception("Plugin can't be null");
		this.plugin = plugin;
		logger = plugin.getLogger();
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		yaml = new File(plugin.getDataFolder(), path);
		if(yaml.exists()) {
			logger.info("Creating config file...");
			
		}
	}
}