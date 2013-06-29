package kr.kkiro.projects.bukkit.EntityProtect.utils;

import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {
	public static String packMessage(String message, String... replaces) {
		String out = message;
		for (int i = 0; i < replaces.length; ++i) {
			String replacement = replaces[i];
			if(replacement.startsWith("#", 0)) {
				replacement = Config.getString("language."+replacement.substring(1));
			}
			out = out.replace("{" + Integer.toString(i + 1) + "}", replaces[i]);
		}
		return out;
	}

	public static void send(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void send(CommandSender sender, String message, String... replaces) {
		send(sender, packMessage(message, replaces));
	}
	
	public static void sendLang(CommandSender sender, String message) {
		send(sender, Config.getString("language."+message));
	}
	
	public static void sendLang(CommandSender sender, String message, String... replaces) {
		send(sender, Config.getString("language."+message), replaces);
	}
}
