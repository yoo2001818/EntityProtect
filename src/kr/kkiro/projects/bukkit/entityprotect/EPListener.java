package kr.kkiro.projects.bukkit.entityprotect;

import java.util.ArrayList;
import java.util.List;

import kr.kkiro.projects.bukkit.entityprotect.utils.BreedCheck;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EPListener implements Listener {
	private EntityProtect plugin;

	public EPListener(EntityProtect plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDispenserThrow(BlockDispenseEvent event) {
		if (!event.getBlock().getType().equals(Material.DISPENSER))
			return;
		ItemStack item = event.getItem();
		if (item.getType() != Material.EGG)
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType().equals(EntityType.CHICKEN)
				|| event.getEntity().getType().equals(EntityType.PIG)
				|| event.getEntity().getType().equals(EntityType.COW)
				|| event.getEntity().getType().equals(EntityType.SHEEP)
				|| event.getEntity().getType().equals(EntityType.MUSHROOM_COW)
				|| event.getEntity().getType().equals(EntityType.OCELOT)
				|| event.getEntity().getType().equals(EntityType.WOLF)) {
			if (plugin.getDB().getPlayer(event.getEntity().getUniqueId()) == null)
				return;
			if (event.getDamager().getType().equals(EntityType.PLAYER)) {
				Player player = (Player) (event.getDamager());
				Creature entity = (Creature) (event.getEntity());
				String owner = plugin.getDB().getPlayer(entity.getUniqueId());
				if (owner != null) {
					if (player.getName().equals(owner))
						EPChat.speak(
								plugin,
								player,
								plugin.getCfg().readString(
										"translation.owneryou"),
								plugin.getCfg().readString(
										"translation.mobs."
												+ entity.getType().getName(),
										entity.getType().getName()));
					else
						EPChat.speak(
								plugin,
								player,
								plugin.getCfg().readString("translation.owner"),
								owner,
								plugin.getCfg().readString(
										"translation.mobs."
												+ entity.getType().getName(),
										entity.getType().getName()), owner);
				}
				if (player.getName().equals(
						plugin.getDB().getPlayer(
								event.getEntity().getUniqueId())))
					return;
				if (!hasPermission("damage", player)) {
					EPChat.speak(plugin, player,
							plugin.getCfg().readString("translation.sorry"));
					player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
					event.setDamage(0);
					event.setCancelled(true);
					return;
				}
				if (!hasPermission("damage1hp", player)) {
					event.setDamage(1);
				}
				if (!hasPermission("slay", player)
						&& event.getDamage() > entity.getHealth()) {
					entity.setHealth(2);
					event.setDamage(1);
					return;
				}
			} else {
				if (!plugin.getCfg().readBoolean("protect.environment")) {
					event.setDamage(0);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent event) {
		// Check this is ageable entity. otherwise, skip.
		if (event.getEntity() instanceof Ageable) {
			Ageable entity = (Ageable) (event.getEntity());
			// Check it has owner.
			String owner = plugin.getDB().getPlayer(entity.getUniqueId());
			plugin.getDB().setCount(owner, plugin.getDB().getCount(owner) - 1);
			plugin.getDB().delete(entity.getUniqueId());
			if (owner != null) {
				// Check its killer.

				Player killer = entity.getKiller();

				if (killer.getName().equals(owner)) {
					EPChat.speak(
							plugin,
							killer,
							plugin.getCfg().readString("translation.killyou"),
							plugin.getCfg().readString(
									"translation.mobs."
											+ entity.getType().getName(),
									entity.getType().getName()),
							Integer.toString(plugin.getCfg().readInt(
									"general.maxentity")
									- plugin.getDB().getCount(owner)));
					plugin.getLogger().info(
							owner
									+ " killed his "
									+ plugin.getCfg().readString(
											"translation.mobs."
													+ entity.getType()
															.getName(),
											entity.getType().getName())
									+ ", he can breed"
									+ Integer.toString(plugin.getCfg().readInt(
											"general.maxentity")
											- plugin.getDB().getCount(owner))
									+ " more animals.");
				} else {
					if (plugin.getServer().getPlayer(owner) != null) {
						Player player = plugin.getServer().getPlayer(owner);
						EPChat.speak(
								plugin,
								player,
								plugin.getCfg().readString("translation.kill"),
								killer.getName(),
								plugin.getCfg().readString(
										"translation.mobs."
												+ entity.getType().getName(),
										entity.getType().getName()), Integer
										.toString(plugin.getCfg().readInt(
												"general.maxentity")
												- plugin.getDB()
														.getCount(owner)));
					}
					plugin.getLogger().info(
							killer.getName()
									+ " killed "
									+ owner
									+ "'s"
									+ plugin.getCfg().readString(
											"translation.mobs."
													+ entity.getType()
															.getName(),
											entity.getType().getName())
									+ ", he can breed"
									+ Integer.toString(plugin.getCfg().readInt(
											"general.maxentity")
											- plugin.getDB().getCount(owner))
									+ " more animals.");
					event.setDroppedExp(0);
					event.getDrops().clear();
				}
			} else {
				// Must be natural mob; check respawn config.
				if (plugin.getCfg().readBoolean("environment.extinction.kill")) {
					respawnAnimal(entity.getType(), entity.getLocation(),
							entity.getWorld());
				}
			}
		}
	}

	@EventHandler
	public void onMobTamed(EntityTameEvent event) {
		if (!processBreed(event.getOwner().getName(), event.getEntity())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent event) {
		plugin.getCache().breedCacheAdd(event.getPlayer().getName(),
				event.getEgg());
	}

	@EventHandler
	public void onMobInteract(PlayerInteractEntityEvent event) {

		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();

		if (entity == null)
			return;

		if (plugin.getCfg().readBoolean(
				"enabled-mobs." + entity.getType().getName(), false) == false)
			return;

		String owner = plugin.getDB().getPlayer(entity.getUniqueId());
		if (owner != null) {
			if (player.getName().equals(owner))
				EPChat.speak(
						plugin,
						player,
						plugin.getCfg().readString("translation.owneryou"),
						plugin.getCfg().readString(
								"translation.mobs."
										+ entity.getType().getName(),
								entity.getType().getName()));
			else
				EPChat.speak(
						plugin,
						player,
						plugin.getCfg().readString("translation.owner"),
						owner,
						plugin.getCfg().readString(
								"translation.mobs."
										+ entity.getType().getName(),
								entity.getType().getName()), owner);
		}

		if (plugin.getCache().setmode.containsKey(player)) {
			if (owner != null) {
				plugin.getDB().setCount(owner,
						plugin.getDB().getCount(owner) - 1);
				plugin.getDB().delete(entity.getUniqueId());
			}
			// Set to owner.
			if (!plugin.getCache().setmode.get(player).equals("")) {
				String set = plugin.getCache().setmode.get(player);
				plugin.getDB().add(set, entity.getUniqueId());
				plugin.getDB().setCount(set, plugin.getDB().getCount(set) + 1);
			}
			EPChat.speak(plugin, player, "Owner successfully reset!");
		}

		if (entity instanceof MushroomCow
				&& player.getItemInHand().getType().equals(Material.SHEARS)
				&& !(owner.equals(player.getName()))) {
			EPChat.speak(plugin, player,
					plugin.getCfg().readString("translation.sorry"));
			event.setCancelled(true);
			player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
			return;
		}
		if (entity instanceof Sheep
				&& player.getItemInHand().getType().equals(Material.SHEARS)
				&& ((owner == null || owner.equals(player.getName())) || hasPermission(
						"shear", player))) {
			EPChat.speak(plugin, player,
					plugin.getCfg().readString("translation.sorry"));
			event.setCancelled(true);
			player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
			return;
		}
		if (entity instanceof Ageable) {
			if (((Ageable) entity).canBreed()
					&& BreedCheck.check(entity.getType(), player
							.getItemInHand().getType())) {
				if (canBreed(player)
						&& (player.getName().equals(owner) || hasPermission(
								"breed", player))) {
					plugin.getCache().breedCacheAdd(player.getName(),
							(Creature) entity);
				} else {
					if (!(player.getName().equals(owner) || hasPermission(
							"breed", player))) {
						EPChat.speak(plugin, player, plugin.getCfg()
								.readString("translation.sorry"));
						plugin.getLogger().info(
								player.getName()
										+ " tried to breed "
										+ plugin.getCfg().readString(
												"translation.mobs."
														+ entity.getType()
																.getName(),
												entity.getType().getName())
										+ ", but parent's owner is " + owner
										+ ", so denied.");
					} else {
						EPChat.speak(plugin, player, plugin.getCfg()
								.readString("translation.breedsorry"));
						plugin.getLogger().info(
								player.getName()
										+ " tried to breed "
										+ plugin.getCfg().readString(
												"translation.mobs."
														+ entity.getType()
																.getName(),
												entity.getType().getName())
										+ ", but player exceeded count.");
					}
					event.setCancelled(true);
					player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
					return;
				}
			}
		}
		if (entity instanceof Tameable) {
			if (!((Tameable) entity).isTamed()
					&& (entity instanceof Wolf && player.getItemInHand()
							.getType().equals(Material.BONE))
					|| (entity instanceof Ocelot && player.getItemInHand()
							.getType().equals(Material.RAW_FISH))) {
				if (!canBreed(player)) {
					// Since every wolf and ocelot that have owner is tamed, we
					// don't need to check owner
					EPChat.speak(plugin, player,
							plugin.getCfg()
									.readString("translation.breedsorry"));
					plugin.getLogger().info(
							player.getName()
									+ " tried to breed "
									+ plugin.getCfg().readString(
											"translation.mobs."
													+ entity.getType()
															.getName(),
											entity.getType().getName())
									+ ", but player exceeded count.");
					event.setCancelled(true);
					player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
			plugin.getCache().breedCacheClear();
			Entity orignal = event.getEntity();
			List<Entity> entities = orignal.getNearbyEntities(1, 1, 1);
			for (Entity entity : entities) {
				if (!entity.getType().equals(EntityType.EGG))
					continue;
				int index = plugin.getCache().breedCacheEntity.indexOf(entity);
				if (index == -1)
					continue;
				String player = plugin.getCache().breedCachePlayer.get(index);
				if (!processBreed(player, orignal)) {
					event.setCancelled(true);
					return;
				}
			}
		}
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
			if (event.getEntity().getType().equals(EntityType.VILLAGER))
				return;
			plugin.getCache().breedCacheClear();
			Entity orignal = event.getEntity();
			List<Entity> entities = orignal.getNearbyEntities(1, 1, 1);
			Boolean foundPlayer = false;
			String player = "";
			short naturalCount = 0;
			for (Entity entity : entities) {
				if (!entity.getType().equals(orignal.getType()))
					continue;
				int index = plugin.getCache().breedCacheEntity.indexOf(entity);
				if (index == -1)
					continue;
				if (!foundPlayer) {
					player = plugin.getCache().breedCachePlayer.get(index);
					if (!processBreed(player, orignal)) {
						event.setCancelled(true);
						return;
					}
					foundPlayer = true;
				}
				if (naturalCount < 2) {
					if (plugin.getDB().getPlayer(entity.getUniqueId()) == null
							&& plugin.getCfg().readBoolean(
									"environment.setowneratbreed")) {
						if (!processBreed(player, entity, true)) {
							killAnimal((Creature) entity);
						} else {
							naturalCount += 1;
							respawnAnimal(entity.getType(),
									entity.getLocation(), entity.getWorld());
						}
					}
				}
			}
			if (naturalCount > 0) {
				if (plugin.getServer().getPlayerExact(player) != null) {
					Player gotPlayer = plugin.getServer()
							.getPlayerExact(player);
					EPChat.speak(plugin, gotPlayer, "");
					EPChat.speak(
							plugin,
							gotPlayer,
							plugin.getCfg().readString("translation.setowner"),
							Integer.toString(naturalCount),
							plugin.getCfg().readString(
									"translation.mobs."
											+ orignal.getType().getName(),
									orignal.getType().getName()), Integer
									.toString(plugin.getCfg().readInt(
											"general.maxentity")
											- plugin.getDB().getCount(player)));
				}
				plugin.getLogger().info(
						player
								+ " breeded ownerless "
								+ Integer.toString(naturalCount)
								+ " "
								+ plugin.getCfg().readString(
										"translation.mobs."
												+ orignal.getType().getName(),
										orignal.getType().getName())
								+ ", he can breed"
								+ Integer.toString(plugin.getCfg().readInt(
										"general.maxentity")
										- plugin.getDB().getCount(player))
								+ " more animals.");
			}
		}
	}

	private void respawnAnimal(EntityType type, Location loc, World world) {
		final class respawner extends BukkitRunnable {
			public EntityType type;
			public Location location;
			public World world;

			public void run() {

				ArrayList<Location> loc1st = new ArrayList<Location>();
				ArrayList<Location> loc2st = new ArrayList<Location>();
				ArrayList<Location> loc3st = new ArrayList<Location>();
				for (int x = -5; x <= 10; ++x) {
					for (int z = -5; z <= 10; ++z) {
						Location loc = location.clone();
						loc.add(x, 0, z);
						loc.setY(world.getHighestBlockYAt(loc) - 1);
						if (loc.getBlock().getType().equals(Material.GRASS)
								|| loc.getBlock().getType()
										.equals(Material.DIRT)
								|| loc.getBlock().getType()
										.equals(Material.MYCEL)
								|| loc.getBlock().getType()
										.equals(Material.SAND)) {
							loc1st.add(loc);
						} else if (loc.getBlock().getType().isSolid()) {
							loc2st.add(loc);
						} else
							loc3st.add(loc);
					}
				}
				if (loc1st.size() > 0) {
					Location loc = loc1st.get((int) (Math.random() * loc1st
							.size()));
					world.spawnEntity(loc.add(0, 1, 0), type);
				} else if (loc2st.size() > 0) {
					Location loc = loc2st.get((int) (Math.random() * loc2st
							.size()));
					world.spawnEntity(loc.add(0, 1, 0), type);
				} else if (loc3st.size() > 0) {
					Location loc = loc3st.get((int) (Math.random() * loc3st
							.size()));
					world.spawnEntity(loc.add(0, 1, 0), type);
				}
			}
		}
		respawner run = new respawner();
		run.location = loc.clone();
		run.type = type;
		run.world = world;
		run.runTaskLater(plugin,
				plugin.getCfg().readInt("environment.extinction.respawntime"));
	}

	private void killAnimal(Creature entity) {
		entity.damage(entity.getHealth());
	}

	private boolean processBreed(String player, Entity entity, boolean silent) {
		if (plugin.getServer().getPlayerExact(player) != null) {
			Player gotPlayer = plugin.getServer().getPlayerExact(player);
			if (!canBreed(gotPlayer)) {
				EPChat.speak(plugin, gotPlayer,
						plugin.getCfg().readString("translation.breedsorry"));
				gotPlayer.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
				plugin.getLogger().info(
						player
								+ " tried to breed "
								+ plugin.getCfg().readString(
										"translation.mobs."
												+ entity.getType().getName(),
										entity.getType().getName())
								+ ", but player exceeded count.");
				return false;
			}
			if (isBypassing(gotPlayer)) {
				EPChat.speak(plugin, gotPlayer,
						plugin.getCfg().readString("translation.bypass"));
				plugin.getLogger().info(
						player + " is bypassing breeding count.");
			}
			if (!silent)
				EPChat.speak(
						plugin,
						gotPlayer,
						plugin.getCfg().readString("translation.breed"),
						plugin.getCfg().readString(
								"translation.mobs."
										+ entity.getType().getName(),
								entity.getType().getName()), Integer
								.toString(plugin.getCfg().readInt(
										"general.maxentity")
										- plugin.getDB().getCount(player) - 1));
		} else {
			if (!canBreed(player)) {
				plugin.getLogger().info(
						player
								+ " tried to breed "
								+ plugin.getCfg().readString(
										"translation.mobs."
												+ entity.getType().getName(),
										entity.getType().getName())
								+ ", but player exceeded count.");
				return false;
			}
		}
		// Register animal into database.
		plugin.getDB().add(player, entity.getUniqueId());
		plugin.getDB().setCount(player, plugin.getDB().getCount(player) + 1);
		if (!silent)
			plugin.getLogger().info(
					player
							+ " breeded "
							+ plugin.getCfg().readString(
									"translation.mobs."
											+ entity.getType().getName(),
									entity.getType().getName())
							+ ", he can breed"
							+ Integer.toString(plugin.getCfg().readInt(
									"general.maxentity")
									- plugin.getDB().getCount(player))
							+ " more animals.");
		return true;
	}

	private boolean processBreed(String player, Entity entity) {
		return processBreed(player, entity, false);
	}

	private boolean hasPermission(String key, Player player) {
		if (plugin.getCfg().readBoolean("protect." + key) == false)
			return true;
		if (player.hasPermission("entityprotect.bypass." + key))
			return true;
		return false;
	}

	private boolean canBreed(Player player) {
		if (plugin.getDB().getCount(player.getName()) < plugin.getCfg()
				.readInt("general.maxentity"))
			return true;
		if (plugin.getCfg().readBoolean("general.bypass")
				&& player.hasPermission("entityprotect.bypass"))
			return true;
		return false;
	}

	private boolean isBypassing(Player player) {
		if (plugin.getDB().getCount(player.getName()) < plugin.getCfg()
				.readInt("general.maxentity"))
			return false;
		return plugin.getCfg().readBoolean("general.bypass")
				&& player.hasPermission("entityprotect.bypass");
	}

	private boolean canBreed(String player) {
		return plugin.getDB().getCount(player) < plugin.getCfg().readInt(
				"general.maxentity");
	}

}
