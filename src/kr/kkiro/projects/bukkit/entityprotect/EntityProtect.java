package kr.kkiro.projects.bukkit.entityprotect;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class EntityProtect extends JavaPlugin {
	
	private EPConfig config;
	private EPDatabase db;
	
    @Override
    public void onEnable(){
    	getLogger().info("EntityProtect version "+getDescription().getVersion()+" Starting up!");
    	try {
    		config = new EPConfig(this);
    		db = new EPDatabase(this);
    	} catch (Exception e) {
    		if(e.getMessage() == "ConfigRequired") {
    			this.setEnabled(false);
    			return;
    		}
    		getLogger().severe("Plugin could not started:");
    		getLogger().severe(e.getMessage());
    		e.printStackTrace();
    		this.setEnabled(false);
    	}
    	try {
    		Metrics metrics = new Metrics(this);
    		metrics.start();
    	} catch (IOException e) {
    		
    	}
    	getServer().getPluginManager().registerEvents(new EPListener(this), this);
    	getCommand("entityprotect").setExecutor(new EPCommand());
    }
 
    @Override
    public void onDisable() {
    	getLogger().info("EntityProtect version "+getDescription().getVersion()+" Shutting down!");
    }
    
    public EPDatabase getDB() {
    	return db;
    }

    public EPConfig getCfg() {
    	return config;
    }
    
}
