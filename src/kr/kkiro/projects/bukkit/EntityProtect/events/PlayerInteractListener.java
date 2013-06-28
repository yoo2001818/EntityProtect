package kr.kkiro.projects.bukkit.EntityProtect.events;

import kr.kkiro.projects.bukkit.EntityProtect.utils.BreedChecker;
import kr.kkiro.projects.bukkit.EntityProtect.utils.ChatUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.EntityUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.DatabaseUtils;
import kr.kkiro.projects.bukkit.EntityProtect.utils.database.EntitySet;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
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
			PermissionUtils.
		}
	}

	@EventHandler
	public void onPlayerEggThrow(PlayerEggThrowEvent e) {

	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

	}
}
