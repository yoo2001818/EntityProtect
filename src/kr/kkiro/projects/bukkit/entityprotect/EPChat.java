package kr.kkiro.projects.bukkit.entityprotect;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EPChat {

	public static String packMessage(String message, String... replaces) {
		String out = message;
		for (int i = 0; i < replaces.length; ++i) {
			out = out.replace("$" + Integer.toString(i + 1), replaces[i]);
		}
		return out;
	}

	public static void speak(EntityProtect plugin, CommandSender sender,
			String message) {
		plugin.getLogger().info(
				sender.getName() + " << "
						+ ChatColor.translateAlternateColorCodes('&', message));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void speak(EntityProtect plugin, CommandSender sender,
			String message, String... replaces) {
		speak(plugin, sender, packMessage(message, replaces));
	}
}
