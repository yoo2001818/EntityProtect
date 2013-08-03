package kr.kkiro.projects.bukkit.EntityProtect.utils;

import java.sql.Date;

import kr.kkiro.projects.bukkit.EntityProtect.bukkit.EntityProtect;
import kr.kkiro.projects.bukkit.EntityProtect.utils.config.Config;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.EntitySet;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.PlayerSet;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntityUtils {
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
			if(!PermissionUtils.canBreed(exactPlayer, null)) {
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
