package kr.kkiro.projects.bukkit.entityprotect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EPCache {

	public static final int BREED_CACHE_TIME = 20000;
	public static final int EGG_CACHE_TIME = 5000;

	public List<Long> breedCacheTime;
	public List<String> breedCachePlayer;
	public List<Entity> breedCacheEntity;
	public Map<Player, String> setmode = new HashMap<Player, String>();

	public EPCache(EntityProtect plugin) {
		breedCacheTime = new ArrayList<Long>();
		breedCachePlayer = new ArrayList<String>();
		breedCacheEntity = new ArrayList<Entity>();
	}

	public void breedCacheClear() {
		int size = breedCacheTime.size();
		for (int i = 0; i < size; ++i) {
			if ((System.currentTimeMillis() - BREED_CACHE_TIME) > breedCacheTime
					.get(i)) {
				breedCacheTime.remove(i);
				breedCachePlayer.remove(i);
				breedCacheEntity.remove(i);
				i -= 1;
				size -= 1;
			}
		}
	}

	public void breedCacheAdd(String player, Entity entity) {
		breedCacheClear();
		breedCacheTime.add(System.currentTimeMillis());
		breedCachePlayer.add(player);
		breedCacheEntity.add(entity);
	}
}
