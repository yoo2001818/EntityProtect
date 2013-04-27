package kr.kkiro.projects.bukkit.entityprotect;

import org.bukkit.command.CommandSender;

public class EPChat {
	
	public static String packMessage(String message, String... replaces) {
		String out = message;
		 for(int i = 0; i < replaces.length; ++i) {
			 out = out.replace(Integer.toString(i), replaces[i]);
		 }
		return out;
	}
	
	public static void speak(EntityProtect plugin, CommandSender sender, String message) {
		plugin.getLogger().info(sender.getName() + " << " + message);
		sender.sendMessage(message);
	}
	
	public static void speak(EntityProtect plugin, CommandSender sender, String message, String... replaces) {
		speak(plugin, sender, packMessage(message, replaces));
	}
}
