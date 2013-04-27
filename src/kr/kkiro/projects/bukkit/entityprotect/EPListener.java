package kr.kkiro.projects.bukkit.entityprotect;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
         
         if(plugin.getDB().getPlayer(entity.getUniqueId()) != null) {
        	 
        	 
        	 
         }
         
    }
}
