package kr.kkiro.projects.bukkit.EntityProtect.utils.cache;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;

public class KillCache {
	public static final int EXPIRE_TIME = 30000;

	private static KillCache instance;

	private List<Long> cacheTime;
	private List<Entity> cacheEntity;

	public static KillCache getInstance() {
		return instance;
	}

	public static void init() {
		instance = new KillCache();
	}

	public KillCache() {
		cacheTime = new ArrayList<Long>();
		cacheEntity = new ArrayList<Entity>();
	}

	public void refresh() {
		int size = cacheTime.size();
		long expireTime = System.currentTimeMillis() - EXPIRE_TIME;
		for (int i = 0; i < size; ++i) {
			long time = cacheTime.get(i);
			if (expireTime > time) {
				cacheTime.remove(i);
				cacheEntity.remove(i);
				--i;
				--size;
			}
		}
	}

	public void clear() {
		cacheTime.removeAll(cacheTime);
		cacheEntity.removeAll(cacheEntity);
	}

	public void add(Entity entity) {
		cacheTime.add(System.currentTimeMillis());
		cacheEntity.add(entity);
	}

	public void remove(Entity entity) {
		int i = cacheEntity.indexOf(entity);
		if (i == -1)
			return;
		cacheTime.remove(i);
		cacheEntity.remove(i);
	}

	public boolean hasEntity(Entity entity) {
		return cacheEntity.contains(entity);
	}
}
