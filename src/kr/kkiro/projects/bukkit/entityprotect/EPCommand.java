package kr.kkiro.projects.bukkit.entityprotect;

import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EPCommand implements CommandExecutor {
	public EntityProtect plugin;

	public EPCommand(EntityProtect plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("entityprotect")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					EPChat.speak(
							plugin,
							sender,
							plugin.getCfg().readString("translation.info"),
							Integer.toString(plugin.getDB().getCount(
									sender.getName())));
				}
				EPChat.speak(plugin, sender, "&e-- EntityProtect --");
				if (sender.hasPermission("entityprotect.get")) {
					EPChat.speak(plugin, sender, "/ep get [player]");
				}
				if (sender.hasPermission("entityprotect.set")) {
					EPChat.speak(plugin, sender, "/ep set [player] [count]");
				}
				if (sender.hasPermission("entityprotect.killall")) {
					EPChat.speak(plugin, sender, "/ep killall [player]");
				}
				if (sender instanceof Player) {
					if (sender.hasPermission("entityprotect.setowner")) {
						EPChat.speak(plugin, sender, "/ep setowner [player]");
					}
					if (sender.hasPermission("entityprotect.delowner")) {
						EPChat.speak(plugin, sender, "/ep delowner");
					}
					if (sender.hasPermission("entityprotect.finish")) {
						EPChat.speak(plugin, sender, "/ep finish");
					}
					if (sender.hasPermission("entityprotect.give")) {
						EPChat.speak(plugin, sender, "/ep give [player]");
					}
					if (sender.hasPermission("entityprotect.yes")) {
						EPChat.speak(plugin, sender, "/ep yes");
					}
					if (sender.hasPermission("entityprotect.no")) {
						EPChat.speak(plugin, sender, "/ep no");
					}
				}
				return true;
			}
			if (args[0].equals("get")) {
				if (!sender.hasPermission("entityprotect.get"))
					return false;
				if (args.length == 2) {
					EPChat.speak(plugin, sender, "$1 breeded $2 times.",
							args[1],
							Integer.toString(plugin.getDB().getCount(args[1])));
					return true;
				}
			}
			if (args[0].equals("set")) {
				if (!sender.hasPermission("entityprotect.set"))
					return false;
				if (args.length == 3) {
					EPChat.speak(plugin, sender, "$1 set $2 to $3", args[1],
							Integer.toString(plugin.getDB().getCount(args[1])),
							args[2]);
					plugin.getDB().setCount(args[1], Integer.parseInt(args[2]));
					return true;
				}
			}
			if (args[0].equals("killall")) {
				if (!sender.hasPermission("entityprotect.killall"))
					return false;
				if (args.length == 2) {
					EPChat.speak(plugin, sender,
							"&4Preparing deletion of $1's all entities...",
							args[1]);
					EPChat.speak(plugin, sender,
							"&aThis can take many time....");
					plugin.getServer().broadcastMessage(
							"EntityProtect is scanning all over the world.");
					plugin.getServer().broadcastMessage(
							"This could take a while...");
					List<UUID> entities = plugin.getDB().getEntities(args[1]);
					List<World> worlds = plugin.getServer().getWorlds();
					for (World world : worlds) {
						EPChat.speak(plugin, sender, "&4Scanning world $1...",
								world.getName());
						List<Entity> worldEntities = world.getEntities();
						for (Entity worldEntity : worldEntities) {
							if (entities.indexOf(worldEntity.getUniqueId()) != -1) {
								if (worldEntity instanceof Creature) {
									((Creature) worldEntity)
											.damage(((Creature) worldEntity)
													.getHealth());
								}
							}
						}
					}
					plugin.getServer().broadcastMessage("Done!");
					return true;
				}
			}
			if (sender instanceof Player) {
				if (args[0].equals("setowner")) {
					if (!sender.hasPermission("entityprotect.setowner"))
						return false;
					if (args.length == 2) {
						EPChat.speak(
								plugin,
								sender,
								"&eRight click any animal, and migration to $1 will started.\n&eType &f/ep finish&e to finish.",
								args[1]);
						plugin.getCache().setmode.put((Player) sender, args[1]);
						return true;
					}
				}
				if (args[0].equals("delowner")) {
					if (!sender.hasPermission("entityprotect.delowner"))
						return false;
					if (args.length == 1) {
						EPChat.speak(
								plugin,
								sender,
								"&eRight click any animal, and migration to Nothing will started.\n&eType &f/ep finish&e to finish.");
						plugin.getCache().setmode.put((Player) sender, "");
						return true;
					}
				}
				if (args[0].equals("finish")) {
					if (!sender.hasPermission("entityprotect.finish"))
						return false;
					if (args.length == 1) {
						EPChat.speak(plugin, sender, "&eFinished!");
						plugin.getCache().setmode.remove((Player) sender);
						return true;
					}
				}
			}
		}
		return false;
	}

}
