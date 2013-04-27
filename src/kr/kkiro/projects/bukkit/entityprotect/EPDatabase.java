package kr.kkiro.projects.bukkit.entityprotect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.SQLite;

public class EPDatabase {
	private Database sql;
	public EPDatabase(EntityProtect plugin) throws Exception {
		if(plugin == null) throw new Exception("Plugin can't be null");
		sql = new SQLite(Logger.getLogger("Minecraft"), 
	             "["+plugin.getDescription().getName()+"] ", 
	             plugin.getDataFolder().getAbsolutePath(), 
	             plugin.getDescription().getName(), 
	             ".db");
		if(sql.open()) {
			if(!sql.isTable("entity")) {
				sql.query("CREATE TABLE `entity` (" +
						"`id` INTEGER PRIMARY KEY," +
						"`entity` UUID," +
						"`player` varchar(255));");
			}
		} else {
			throw new Exception("Cannot open database");
		}
	}
	public boolean add(String player, UUID entity) {
		try {
			PreparedStatement query =  sql.prepare("INSERT INTO `entity` (" +
					"entity," +
					"player ) VALUES (" +
					" ? , ? )");
			query.setString(1, entity.toString());
			query.setString(2, player);
			query.executeQuery();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public String getPlayer(UUID entity) {
		try {
			PreparedStatement query = sql.prepare("SELECT * FROM `entity` WHERE `entity` = ? ;");
			query.setString(1, entity.toString());
			ResultSet result = query.executeQuery();
			if(result.wasNull()) return null;
			else return result.getString("player");
		} catch (Exception e) {
			return null;
		}
	}
	public boolean delete(UUID entity) {
		try {
			PreparedStatement query = sql.prepare("DELETE FROM `entity` WHERE `entity` = ? ;");
			query.setString(1, entity.toString());
			query.executeQuery();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public List<UUID> getEntities(String player) {
		try {
			List<UUID> list = new ArrayList<UUID>();
			PreparedStatement query = sql.prepare("SELECT * FROM `entity` WHERE `player` = ' ? ';");
			query.setString(1, player);
			ResultSet result = query.executeQuery();
			while(!result.isLast()) {
				list.add(UUID.fromString(result.getString("entity")));
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}
}
