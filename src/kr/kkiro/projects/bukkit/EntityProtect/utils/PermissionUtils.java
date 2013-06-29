package kr.kkiro.projects.bukkit.EntityProtect.utils;

import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.PlayerSet;

import org.bukkit.entity.Player;

public class PermissionUtils {
	
	public static boolean canBypass(String activity, Player player, Boolean hasOwner) {
		if(player.hasPermission("entityprotect.bypass-protect."+activity)) return true;
		if(hasOwner && Config.getString("protect-entities."+activity).equals("false")) return true;
		if(Config.getString("protect-entities."+activity).equals("nonowner")) return false;
		return false;
	}
	public static boolean canBypass(String activity, Boolean hasOwner) {
		return !Config.getBoolean("protect-entities."+activity);
	}
	public static boolean canBreed(Player player, Boolean hasOwner) {
		PlayerSet playerset = DatabaseUtils.searchPlayer(player.getName());
		if (playerset == null) {
			playerset = new PlayerSet();
			playerset.setPlayer(player.getName());
			playerset.setBreedCount(0);
			DatabaseUtils.save(playerset);
		}
		if(playerset.getBreedCount() < Config.getInt("general.max-entities-per-player")) {
			return true;
		} else {
			if(canBypass(EntityActivity.BREED, player, hasOwner)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
