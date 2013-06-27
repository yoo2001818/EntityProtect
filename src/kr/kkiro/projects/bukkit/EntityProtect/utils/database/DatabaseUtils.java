package kr.kkiro.projects.bukkit.EntityProtect.utils.database;

import java.util.UUID;

import com.avaje.ebean.Ebean;

public class DatabaseUtils {
	public static EntitySet searchEntity(UUID uuid) {
		return Ebean.find(EntitySet.class).where().eq("entity", uuid)
				.findUnique();
	}
}
