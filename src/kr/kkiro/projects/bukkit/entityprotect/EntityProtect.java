package kr.kkiro.projects.bukkit.entityprotect;

import org.bukkit.plugin.java.JavaPlugin;

public class EntityProtect extends JavaPlugin {
	
    @Override
    public void onEnable(){
    	getLogger().info("EntityProtect version "+getDescription().getVersion()+" Starting up!");
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("EntityProtect version "+getDescription().getVersion()+" Shutting down!");
    }
    
}
