package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.util.UUID;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;

import com.avaje.ebean.Ebean;

public class DatabaseUtils {
	public static EntitySet searchEntity(UUID uuid) {
		return Ebean.find(EntitySet.class).where().eq("entity", uuid)
				.findUnique();
	}
	public static PlayerSet searchPlayer(String player) {
		return Ebean.find(PlayerSet.class).where().eq("player", player)
				.findUnique();
	}
	public static void save(Object object) {
		EntityProtect.getInstance().getDatabase().save(object);
	}
}
