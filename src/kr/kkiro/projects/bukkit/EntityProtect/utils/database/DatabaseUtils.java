package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.util.UUID;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;

public class DatabaseUtils {
	public static EntitySet searchEntity(UUID uuid) {
		return EntityProtect.getInstance().getDatabase().find(EntitySet.class).where().eq("entity", uuid)
				.findUnique();
	}
	public static PlayerSet searchPlayer(String player) {
		return EntityProtect.getInstance().getDatabase().find(PlayerSet.class).where().eq("player", player)
				.findUnique();
	}
	public static void save(Object object) {
		EntityProtect.getInstance().getDatabase().save(object);
	}
	public static void delete(Object object) {
		EntityProtect.getInstance().getDatabase().delete(object);
	}
}
