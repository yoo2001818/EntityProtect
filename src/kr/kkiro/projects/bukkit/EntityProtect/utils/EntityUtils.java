package kr.kkiro.projects.bukkit.EntityProtect.utils;

import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;

import org.bukkit.entity.EntityType;

public class EntityUtils {
	public static boolean isEnabled(EntityType type) {
		return Config.getBoolean("enabled-entities." + type.getName());
	}
}
