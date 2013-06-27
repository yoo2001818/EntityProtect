package kr.kkiro.projects.bukkit.EntityProtect.utils;

import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;

import org.bukkit.entity.Player;

public class PermissionUtils {
	public boolean canBypass(String activity, Player player, Boolean hasOwner) {
		if(player.hasPermission("entityprotect.bypass-protect."+activity)) return true;
		if(hasOwner && Config.getString("protect-entities."+activity).equals("false")) return true;
		if(Config.getString("protect-entities."+activity).equals("nonowner")) return false;
		return false;
	}
	public boolean canBypass(String activity, Boolean hasOwner) {
		return !Config.getBoolean("protect-entities."+activity);
	}
}
