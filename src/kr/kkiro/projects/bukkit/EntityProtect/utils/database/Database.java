package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.util.ArrayList;
import java.util.List;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;

public class Database {
	private static Database instance;
	private static List<Class<?>> databaseClasses;
	private EntityProtect plugin;

	public Database() {
		plugin = EntityProtect.getInstance();
		plugin.installDatabase();
	}

	public static void init() {
		instance = new Database();
	}

	public static Database getInstance() {
		return instance;
	}

	public static List<Class<?>> getDatabaseClasses() {
		if (databaseClasses == null) {
			databaseClasses = new ArrayList<Class<?>>();
			databaseClasses.add(EntitySet.class);
			databaseClasses.add(PlayerSet.class);
		}
		return databaseClasses;
	}
}
