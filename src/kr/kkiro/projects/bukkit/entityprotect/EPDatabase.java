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
		if (plugin == null)
			throw new Exception("Plugin can't be null");
		sql = new SQLite(Logger.getLogger("Minecraft"), "["
				+ plugin.getDescription().getName() + "] ", plugin
				.getDataFolder().getAbsolutePath(), plugin.getDescription()
				.getName(), ".db");
		if (sql.open()) {
			sql.query("CREATE TABLE IF NOT EXISTS `entity` ("
					+ "`id` INTEGER PRIMARY KEY," + "`entity` varchar(36),"
					+ "`player` varchar(255));");
			sql.query("CREATE TABLE IF NOT EXISTS `count` ("
					+ "`id` INTEGER PRIMARY KEY," + "`player` varchar(255),"
					+ "`count` INTEGER);");
		} else {
			throw new Exception("Cannot open database");
		}
	}

	public void closeDB() {
		sql.close();
	}

	public int getCount(String player) {
		try {
			PreparedStatement query = sql
					.prepare("SELECT * FROM `count` WHERE `player` =?;");
			query.setString(1, player);
			ResultSet result = query.executeQuery();
			int result2 = 0;
			if (result.next())
				result2 = result.getInt("count");
			result.close();
			query.close();
			return result2;
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean setCount(String player, int count) {
		try {
			PreparedStatement query = sql
					.prepare("INSERT OR REPLACE INTO `count` (id, player, count) VALUES ((select id from `count` where player=?),?,?);");
			query.setString(1, player);
			query.setString(2, player);
			query.setInt(3, count);
			query.executeUpdate();
			query.close();

			sql.getConnection().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean add(String player, UUID entity) {
		try {
			PreparedStatement query = sql.prepare("INSERT INTO `entity` ("
					+ "entity," + "player ) VALUES (" + "?,?)");
			query.setString(1, entity.toString());
			query.setString(2, player);
			query.executeUpdate();
			query.close();

			sql.getConnection().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getPlayer(UUID entity) {
		try {
			PreparedStatement query = sql
					.prepare("SELECT * FROM `entity` WHERE `entity` =?;");
			query.setString(1, entity.toString());
			ResultSet result = query.executeQuery();
			String result2 = null;
			if (result.next())
				result2 = result.getString("player");
			result.close();
			query.close();
			return result2;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean delete(UUID entity) {
		try {
			PreparedStatement query = sql
					.prepare("DELETE FROM `entity` WHERE `entity` =?;");
			query.setString(1, entity.toString());
			query.executeUpdate();
			query.close();

			sql.getConnection().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<UUID> getEntities(String player) {
		try {
			List<UUID> list = new ArrayList<UUID>();
			PreparedStatement query = sql
					.prepare("SELECT * FROM `entity` WHERE `player` =?;");
			query.setString(1, player);
			ResultSet result = query.executeQuery();
			while (result.next()) {
				list.add(UUID.fromString(result.getString("entity")));
			}
			result.close();
			query.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}
}
