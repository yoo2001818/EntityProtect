package kr.kkiro.projects.bukkit.EntityProtect.events;

import kr.kkiro.projects.bukkit.EntityProtect.utils.BreedChecker;
import kr.kkiro.projects.bukkit.EntityProtect.utils.ChatUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.EntityActivity;
import kr.kkiro.projects.bukkit.EntityProtect.utils.EntityUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.PermissionUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.cache.BreedCache;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.EntitySet;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Material material = player.getItemInHand().getType();
		if (entity == null)
			return;
		if (!EntityUtils.isEnabled(entity.getType()))
			return;
		EntitySet entityset = DatabaseUtils.searchEntity(entity.getUniqueId());
		if (entityset != null) {
			String owner = entityset.getOwner().getPlayer();
			ChatUtils.sendLang(player, "owner-info", 
					"#mobs."+entity.getType().getName(),
					(owner == player.getName()) ? "#you" : owner);
		}
		if (BreedChecker.check(entity.getType(), material)) {
			if (PermissionUtils.canBypass(EntityActivity.BREED, player, entityset != null)) {
				BreedCache.getInstance().refresh();
				BreedCache.getInstance().add(player.getName(), entity);
				return;
			} else {
				ChatUtils.sendLang(player, "access-denied");
				event.setCancelled(true);
				return;
			}
		}
		if (entity.getType().equals(EntityType.SHEEP) && material.equals(Material.SHEARS)) {
			if (PermissionUtils.canBypass(EntityActivity.SHEAR_SHEEP, player, entityset != null)) {
				return;
			} else {
				ChatUtils.sendLang(player, "access-denied");
				event.setCancelled(true);
				return;
			}
		}
		if (entity.getType().equals(EntityType.MUSHROOM_COW) && material.equals(Material.SHEARS)) {
			if (PermissionUtils.canBypass(EntityActivity.SHEAR_MUSHROOM_COW, player, entityset != null)) {
				return;
			} else {
				ChatUtils.sendLang(player, "access-denied");
				event.setCancelled(true);
				return;
			}
		}
		if (entity.getType().equals(EntityType.COW) && material.equals(Material.BUCKET)) {
			if (PermissionUtils.canBypass(EntityActivity.FILL_COW, player, entityset != null)) {
				return;
			} else {
				ChatUtils.sendLang(player, "access-denied");
				event.setCancelled(true);
				return;
			}
		}
		if (entity.getType().equals(EntityType.MUSHROOM_COW) && material.equals(Material.BOWL)) {
			if (PermissionUtils.canBypass(EntityActivity.FILL_MUSHROOM_COW, player, entityset != null)) {
				return;
			} else {
				ChatUtils.sendLang(player, "access-denied");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		//TODO: Add cache
	}
	
	//TODO: Support monster eggs
}
