package com.cn.request.cache;

/**
 * Date: 2019/7/15
 * <p>
 * Time: 6:15 PM
 * <p>
 * author: 鹿文龙
 */
public class LruCache extends ILruCache {

	private static LruCache instance = new LruCache();

	private LruDiskCache diskCache;

	private LruMemoryCache memoryCache;

	private LruCache() {
		diskCache = LruDiskCache.getInstance();
		memoryCache = LruMemoryCache.getInstance();
	}

	public static LruCache getInstance() {
		return instance;
	}

	@Override
	public <T> void put(String key, T value) {
		diskCache.put(key, value);
		memoryCache.put(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		T value = memoryCache.get(key);
		return value == null ? (T) diskCache.get(key) : value;
	}

	@Override
	public void remove(String key) {
		diskCache.remove(key);
		memoryCache.remove(key);
	}
}
