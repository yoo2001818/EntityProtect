package kr.kkiro.projects.bukkit.EntityProtect.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		//TODO: Get reason and process
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		//TODO: Get killer and process
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		//TODO: Get reason and process
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		//TODO: Get damager and process
	}
}
