package kr.kkiro.projects.bukkit.EntityProtect.events;

import java.util.List;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;
import kr.kkiro.projects.bukkit.EntityProtect.utils.ChatUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.EntityUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.cache.BreedCache;
import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.PlayerSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class EntityListener implements Listener {
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		LivingEntity original = event.getEntity();
		List<Entity> entities = original.getNearbyEntities(1, 1, 1);
		if (!EntityUtils.isEnabled(original.getType()))
			return;
		EntityProtect.info("Passed 1");
		if(event.getSpawnReason().equals(SpawnReason.EGG)) {
			BreedCache.getInstance().refresh();
			for (Entity entity : entities) {
				if (!entity.getType().equals(EntityType.EGG)) continue;
				String player = BreedCache.getInstance().findPlayer(entity);
				if (player == null) continue;
				if (!EntityUtils.registerEntity(player, original)) {
					event.setCancelled(true);
				}
				return;
			}
		}
		if(event.getSpawnReason().equals(SpawnReason.BREEDING)) {
			EntityProtect.info("Passed 2");
			BreedCache.getInstance().refresh();
			Boolean foundPlayer = false;
			String player = "";
			short naturalCount = 0;
			for (Entity entity : entities) {
				EntityProtect.info(entity.getType().toString()+" loop");
				if (!entity.getType().equals(original.getType())) continue;
				String playerMatch = BreedCache.getInstance().findPlayer(entity);
				EntityProtect.info("result"+playerMatch);
				if(playerMatch == null) continue;
				if(!foundPlayer) {
					EntityProtect.info("Success");
					player = playerMatch;
					if (!EntityUtils.registerEntity(player, original)) {
						event.setCancelled(true);
						return;
					}
					foundPlayer = true;
				}
				if (naturalCount < 2) {
					if(Config.getBoolean("protect-environment.set-owner-when-breeding-natural") &&
							DatabaseUtils.searchEntity(entity.getUniqueId()) == null) {
						if (!EntityUtils.registerEntity(player, entity, true)) {
							if (entity instanceof Creature) {
								((Creature) entity).setHealth(0);
							} else {
								entity.remove();
							}
						} else {
							naturalCount += 1;
							//TODO: try to respawn mobs
						}
					}
				}
			}
			if (naturalCount > 0) {
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
				Player exactPlayer = EntityProtect.getInstance().getServer().getPlayerExact(player);
				if(exactPlayer != null) {
					ChatUtils.sendLang(exactPlayer, "breed-setowner", Integer.toString(naturalCount),
							"#mobs."+original.getType().getName(), Integer.toString(remainBreedCount));
				}
				ChatUtils.sendLang(EntityProtect.getInstance().getServer().getConsoleSender(), "console.breed-setowner",
						player, Integer.toString(naturalCount),
						"#mobs."+original.getType().getName(), Integer.toString(remainBreedCount));
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (!EntityUtils.isEnabled(entity.getType()))
			return;
		String killer = null;
		if(entity.getKiller() != null) killer = entity.getKiller().getName();
		EntityUtils.removeEntity(entity, killer);
		
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		//TODO: Get reason and process
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		//TODO: Get damager and process
	}
	
	@EventHandler
	public void onEntityTame(EntityTameEvent event) {
		if (!EntityUtils.isEnabled(event.getEntity().getType()))
			return;
		if (!EntityUtils.registerEntity(event.getOwner().getName(), event.getEntity())) {
			event.setCancelled(true);
			return;
		}
	}
}
