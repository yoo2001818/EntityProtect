package kr.kkiro.projects.bukkit.EntityProtect.utils.cache;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;

public class BreedCache {
	public static final int EXPIRE_TIME = 30000;

	private static BreedCache instance;

	private List<Long> cacheTime;
	private List<String> cachePlayer;
	private List<Entity> cacheEntity;

	public static BreedCache getInstance() {
		return instance;
	}

	public static void init() {
		instance = new BreedCache();
	}

	public BreedCache() {
		cacheTime = new ArrayList<Long>();
		cachePlayer = new ArrayList<String>();
		cacheEntity = new ArrayList<Entity>();
	}

	public void refresh() {
		int size = cacheTime.size();
		long expireTime = System.currentTimeMillis() - EXPIRE_TIME;
		for (int i = 0; i < size; ++i) {
			long time = cacheTime.get(i);
			if (expireTime > time) {
				cacheTime.remove(i);
				cachePlayer.remove(i);
				cacheEntity.remove(i);
				--i;
				--size;
			}
		}
	}

	public void clear() {
		cacheTime.removeAll(cacheTime);
		cachePlayer.removeAll(cachePlayer);
		cacheEntity.removeAll(cacheEntity);
	}

	public void add(String player, Entity entity) {
		cacheTime.add(System.currentTimeMillis());
		cachePlayer.add(player);
		cacheEntity.add(entity);
	}

	public void remove(String player) {
		int i = cachePlayer.indexOf(player);
		if (i == -1)
			return;
		cacheTime.remove(i);
		cachePlayer.remove(i);
		cacheEntity.remove(i);
	}

	public void remove(Entity entity) {
		int i = cacheEntity.indexOf(entity);
		if (i == -1)
			return;
		cacheTime.remove(i);
		cachePlayer.remove(i);
		cacheEntity.remove(i);
	}

	public String findPlayer(Entity entity) {
		int i = cacheEntity.indexOf(entity);
		if (i == -1)
			return null;
		return cachePlayer.get(i);
	}
}
