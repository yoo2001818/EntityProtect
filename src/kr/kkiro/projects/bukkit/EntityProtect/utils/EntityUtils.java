package kr.kkiro.projects.bukkit.EntityProtect.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;
import kr.kkiro.projects.bukkit.EntityProtect.utils.cache.KillCache;
import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.EntitySet;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.PlayerSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityUtils {
	public static boolean isOwnerNear(Entity entity, EntitySet entityset) {
		if(entityset == null) return false;
		int range = Config.getInt("general.no-protection-range");
		Player player = EntityProtect.getInstance().getServer().getPlayerExact(entityset.getOwnerItem().getPlayer());
		if(player == null) return false;
		if(!player.isOnline()) return false;
		if(player.getWorld() != entity.getWorld()) return false;
		return player.getLocation().distance(entity.getLocation()) < range;
	}
	public static void respawnEntity(Entity entity, String reason) {
		KillCache.getInstance().refresh();
		if(KillCache.getInstance().hasEntity(entity)) return;
		KillCache.getInstance().add(entity);
		double percent = Config.getDouble("protect-environment.extinction."+reason);
		Random random = new Random();
		int delay = Config.getInt("protect-environment.extinction.respawn-delay");
		if(percent < random.nextDouble()) return;
		final class Respawner implements Runnable {
			public Location location;
			public EntityType entitytype;
			public Random random;
			public void run() {
				ArrayList<Location> possibleLocation = new ArrayList<Location>();
				ArrayList<Location> failsafeLocation = new ArrayList<Location>();
				int deadX = location.getBlockX();
				int deadZ = location.getBlockZ();
				World deadWorld = location.getWorld();
				List<Short> allowedBlocks = Config.getShortList("protect-environment.extinction.respawn-blocks");
				int maxRange = Config.getInt("protect-environment.extinction.respawn-maxrange");
				int minRange = Config.getInt("protect-environment.extinction.respawn-minrange");
				boolean failsafe = Config.getBoolean("protect-environment.extinction.respawn-failsafe");
				for(int addX = -maxRange; addX <= maxRange; ++ addX) {
					if(Math.abs(addX) < minRange) continue;
					for(int addZ = -maxRange; addZ <= maxRange; ++ addZ) {
						if(Math.abs(addZ) < minRange) continue;
						int currentX = deadX + addX;
						int currentZ = deadZ + addZ;
						Block currentBlock = deadWorld.getHighestBlockAt(currentX, currentZ);
						if(!allowedBlocks.contains(currentBlock.getTypeId()))
							if(failsafe && currentBlock.getType().isSolid())
								failsafeLocation.add(currentBlock.getLocation());
						 else possibleLocation.add(currentBlock.getLocation());
					}
				}
				Location spawnLoc;
				if(possibleLocation.size() > 0) {
					spawnLoc = possibleLocation.get(random.nextInt(possibleLocation.size()));
				} else {
					if(!failsafe) return;
					if(failsafeLocation.size() == 0) return;
					spawnLoc = failsafeLocation.get(random.nextInt(failsafeLocation.size()));
				}
				spawnLoc.add(0, 1, 0);
				LivingEntity entity = (LivingEntity) (deadWorld.spawnEntity(spawnLoc, entitytype));
				if(entity instanceof Horse) {
					Horse horse = (Horse)entity;
					Horse.Style[] styles = Horse.Style.values();
					Horse.Color[] colors = Horse.Color.values();
					horse.setMaxHealth(random.nextInt(16)+15);
					if(random.nextInt(100) < 10) horse.setVariant(Variant.DONKEY);
					horse.setStyle(styles[random.nextInt(styles.length)]);
					horse.setColor(colors[random.nextInt(colors.length)]);
				}
				entity.setRemoveWhenFarAway(false);
				/*//TODO firework to track spawned mobs
				final class Fireworker implements Runnable {
					public World deadWorld;
					public Location spawnLoc;
					public int repeats = 0;
					public int id;
					public void run() {
						repeats += 1;
						if(repeats > 40) {
							EntityProtect.getInstance().getServer().getScheduler().cancelTask(id);
						}
						Firework fw = (Firework) deadWorld.spawnEntity(spawnLoc, EntityType.FIREWORK);
						FireworkMeta fwm = fw.getFireworkMeta();
						FireworkEffect effect = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.WHITE).withFlicker().withTrail().withFade(Color.AQUA).build();
						fwm.addEffect(effect);
						fwm.setPower(3);
						fw.setFireworkMeta(fwm);
					}
				}
				Fireworker fw = new Fireworker();
				fw.deadWorld = deadWorld;
				fw.spawnLoc = spawnLoc;
				fw.id = EntityProtect.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(EntityProtect.getInstance(), fw, 0, 5);
				*/
			}
		}
		Respawner respawner = new Respawner();
		respawner.location = entity.getLocation();
		respawner.entitytype = entity.getType();
		respawner.random = random;
		EntityProtect.getInstance().getServer().getScheduler().runTaskLater(EntityProtect.getInstance(), respawner, delay);
	}
	public static boolean isEnabled(EntityType type) {
		return Config.getBoolean("enabled-entities." + type.getName());
	}
	public static void playEffect(Player player, Entity entity) {
		player.playSound(entity.getLocation(), Sound.FALL_BIG, 2, 1);
		player.playEffect(entity.getLocation(), Effect.SMOKE, 0);
	}
	public static boolean removeEntity(Entity entity, String killer) {
		EntitySet entitySet = DatabaseUtils.searchEntity(entity.getUniqueId());
		if (entitySet == null) {
			return false;
		}
		PlayerSet playerSet = entitySet.getOwnerItem();
		String player = playerSet.getPlayer();
		Player exactPlayer = EntityProtect.getInstance().getServer().getPlayerExact(player);
		int breedCount = playerSet.getBreedCount();
		int maxBreedCount = Config.getInt("general.max-entities-per-player");
		int remainBreedCount = maxBreedCount-breedCount;
		if(killer == null) {
			killer = Config.getString("language.environment");
		}
		if(exactPlayer != null) {
			ChatUtils.sendLang(exactPlayer, "slay-animal", (player.equals(killer) ? "#you" : killer),
					"#mobs."+entity.getType().getName(), Integer.toString(remainBreedCount+1));
		}
		ChatUtils.sendLang(EntityProtect.getInstance().getServer().getConsoleSender(), "console.slay-animal",
				killer, player, "#mobs."+entity.getType().getName(), Integer.toString(remainBreedCount+1)); 
		
		playerSet.setBreedCount(breedCount - 1);
		DatabaseUtils.save(playerSet);
		DatabaseUtils.delete(entitySet);
		return true;
	}
	public static boolean registerEntity(String player, Entity entity, Boolean silent) {
		if (DatabaseUtils.searchEntity(entity.getUniqueId()) != null) {
			return true;
		}
		Player exactPlayer = EntityProtect.getInstance().getServer().getPlayerExact(player);
		PlayerSet playerSet = DatabaseUtils.searchPlayer(player);
		if (playerSet == null) {
			playerSet = new PlayerSet();
			playerSet.setPlayer(player);
			playerSet.setBreedCount(0);
			DatabaseUtils.save(playerSet);
		}
		int breedCount = playerSet.getBreedCount();
		int maxBreedCount = Config.getInt("general.max-entities-per-player");
		int remainBreedCount = maxBreedCount-breedCount;
		if(exactPlayer != null) {
			if(!PermissionUtils.canBreed(exactPlayer, null, null)) {
				return false;
			}
			if(!silent) {
				ChatUtils.sendLang(exactPlayer, "breed-complete", 
						"#mobs."+entity.getType().getName(), Integer.toString(remainBreedCount-1));
			}
		} else {
			if(!PermissionUtils.canBreed(player)) {
				return false;
			}
		}
		if(!silent) {
			ChatUtils.sendLang(EntityProtect.getInstance().getServer().getConsoleSender(), "console.breed-complete",
					player, "#mobs."+entity.getType().getName(), Integer.toString(remainBreedCount-1)); 
		}
		playerSet.setBreedCount(breedCount + 1);
		DatabaseUtils.save(playerSet);
		EntitySet entitySet = new EntitySet();
		entitySet.setEntity(entity.getUniqueId());
		entitySet.setRegtime(new Date(System.currentTimeMillis()));
		entitySet.setLocation(entity.getLocation());
		entitySet.setOwner(playerSet.getId());
		DatabaseUtils.save(entitySet);
		return true;
	}
	public static boolean registerEntity(String player, Entity entity) {
		return registerEntity(player, entity, false);
	}
}
