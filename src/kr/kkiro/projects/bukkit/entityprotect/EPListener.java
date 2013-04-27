package kr.kkiro.projects.bukkit.entityprotect;

import kr.kkiro.projects.bukkit.entityprotect.utils.BreedCheck;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EPListener implements Listener {
	private EntityProtect plugin;
	
	public EPListener(EntityProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onMobInteract(PlayerInteractEntityEvent event){
    	
    	 Player player = event.getPlayer();
         Entity entity = event.getRightClicked();
         
         if(entity == null) return;
         
         if(plugin.getCfg().readBoolean("mob."+entity.getType().getName(), false) == false) return;
         
         String owner = plugin.getDB().getPlayer(entity.getUniqueId());
         if(owner != null) {
        	 if(player.getName().equals(owner))
        		 EPChat.speak(plugin, player, 
        				 plugin.getCfg().readString("translation.owneryou"),
        				 plugin.getCfg().readString("translation.mob."+entity.getType().getName(), entity.getType().getName()));
        	 else
        		 EPChat.speak(plugin, player, 
        				 plugin.getCfg().readString("translation.owneryou"),
        				 plugin.getCfg().readString("translation.mob."+entity.getType().getName(), entity.getType().getName()), owner); 
         }
         if(entity instanceof Ageable) {
             if(((Ageable)entity).canBreed() && BreedCheck.check(entity.getType(), player.getItemInHand().getType())) {
            	 if(player.getName().equals(owner) || hasPermission("breed", player)) {
            		 plugin.getCache().breedCacheAdd(player.getName(), (Creature)entity);
            	 } else {
            		 EPChat.speak(plugin, player, plugin.getCfg().readString("translation.sorry"));
            		 event.setCancelled(true);
            	 }
             } 
         }
         
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
    	
    }
    
    private boolean hasPermission(String key, Player player) {
    	if(plugin.getCfg().readBoolean("protect."+key) == false) return true;
    	if(player.hasPermission("entityprotect.bypass."+key)) return true;
    	return false;
    }
    
}
