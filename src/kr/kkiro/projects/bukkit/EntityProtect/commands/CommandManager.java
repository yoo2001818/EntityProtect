package kr.kkiro.projects.bukkit.EntityProtect.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
	private static CommandManager instance;
	private static Map<String, CommandRunnable> commands;
	//private EntityProtect plugin;

	public CommandManager() {
		//plugin = EntityProtect.getInstance();
		//plugin.getCommand("entityprotect").setExecutor(this);
	}

	public static void init() {
		commands = new HashMap<String, CommandRunnable>();
		registerCommand(new CommandKill());
		instance = new CommandManager();
	}

	public static CommandManager getInstance() {
		return instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	private static void registerCommand(CommandRunnable command) {
		commands.put(command.getCommand(), command);
	}

}
