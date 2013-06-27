package kr.kkiro.projects.bukkit.EntityProtect.commands;

import org.bukkit.command.CommandSender;

public abstract class CommandRunnable {
	public abstract String getCommand();

	public abstract boolean runCommand(CommandSender sender, String[] args);
}
