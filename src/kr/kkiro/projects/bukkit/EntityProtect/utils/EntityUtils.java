package kr.kkiro.projects.bukkit.EntityProtect.utils;

import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntityUtils {
	public static boolean isEnabled(EntityType type) {
		return Config.getBoolean("enabled-entities." + type.getName());
	}
	public static void playEffect(Player player, Entity entity) {
		player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
		player.playEffect(entity.getLocation(), Effect.SMOKE, 0);
	}
}
