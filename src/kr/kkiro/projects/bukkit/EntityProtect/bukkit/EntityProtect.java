package kr.kkiro.projects.bukkit.EntityProtect.bukkit;

import java.util.List;

import javax.persistence.PersistenceException;

import kr.kkiro.projects.bukkit.EntityProtect.commands.CommandManager;
import kr.kkiro.projects.bukkit.EntityProtect.events.DispenserListener;
import kr.kkiro.projects.bukkit.EntityProtect.events.PlayerInteractListener;
import kr.kkiro.projects.bukkit.EntityProtect.utils.cache.BreedCache;
import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.Database;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.EntitySet;

import org.bukkit.plugin.java.JavaPlugin;

public class EntityProtect extends JavaPlugin {

	private static EntityProtect instance;

	@Override
	public void onEnable() {
		instance = this;

		Config.init();
		Database.init();
		CommandManager.init();
		BreedCache.init();

		getServer().getPluginManager().registerEvents(new DispenserListener(),
				this);
		getServer().getPluginManager().registerEvents(
				new PlayerInteractListener(), this);
	}

	@Override
	public void onDisable() {
		// TODO Insert logic to be performed when the plugin is disabled
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		return Database.getDatabaseClasses();
	}

	public void installDatabase() {
		try {
			this.getDatabase().find(EntitySet.class).findRowCount();
		} catch (PersistenceException ex) {
			info("Installing database for " + getDescription().getName()
					+ " due to first time usage");
			this.installDDL();
		}
	}

	public static EntityProtect getInstance() {
		return instance;
	}

	public static void info(String message) {
		getInstance().getLogger().info(message);
	}

	public static void warning(String message) {
		getInstance().getLogger().warning(message);
	}

	public static void severe(String message) {
		getInstance().getLogger().severe(message);
	}
}
