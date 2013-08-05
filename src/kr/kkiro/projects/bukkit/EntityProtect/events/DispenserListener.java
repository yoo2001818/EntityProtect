package kr.kkiro.projects.bukkit.EntityProtect.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DispenserListener implements Listener {

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		if(!event.getBlock().getType().equals(Material.DISPENSER)) return;
		if(!(event.getItem().getType().equals(Material.EGG) || event.getItem().getType().equals(Material.MONSTER_EGG) || event.getItem().getType().equals(Material.MONSTER_EGGS))) return;
		event.setCancelled(true);
		// TODO: add to cache
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		// TODO: set owner
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		// TODO: set owner
	}
}
