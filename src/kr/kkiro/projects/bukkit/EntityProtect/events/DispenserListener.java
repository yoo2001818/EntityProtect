package kr.kkiro.projects.bukkit.EntityProtect.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DispenserListener implements Listener {

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent e) {
		// TODO: add to cache
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		// TODO: set owner
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent e) {
		// TODO: set owner
	}
}
