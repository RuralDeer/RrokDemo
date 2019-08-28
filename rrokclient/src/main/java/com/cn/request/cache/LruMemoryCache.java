package com.cn.request.cache;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * Date: 2019/7/15
 * <p>
 * Time: 4:00 PM
 * <p>
 * author: 鹿文龙
 */
public class LruMemoryCache extends ILruCache {

	private static LruMemoryCache instance = new LruMemoryCache();

	private LruCache<String, Object> cache = null;

	private LruMemoryCache() {
		this.cache = new LruCache(2 * 1024 * 1024);// 2MiB
	}

	public static LruMemoryCache getInstance() {
		return instance;
	}

	@Override
	public <T> void put(String key, T value) {
		if (TextUtils.isEmpty(key) || null == value) {return;}
		this.cache.put(key, value);
	}

	@Override
	public <T> T get(String key) {
		return (T) this.cache.get(key);
	}


	@Override
	public void remove(String key) {
		this.cache.remove(key);
	}
}
