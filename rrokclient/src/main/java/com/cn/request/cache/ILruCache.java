package com.cn.request.cache;

/**
 * Date: 2019/7/15
 * <p>
 * Time: 4:30 PM
 * <p>
 * author: 鹿文龙
 */
public abstract class ILruCache {

	/**
	 * 插入任意类型数据
	 *
	 * @param key
	 * @param value
	 * @param <T>
	 */
	public abstract <T> void put(String key, T value);

	/**
	 * 获取数据
	 * <p>
	 * <p>
	 * LruCache 可以直接获取到对象
	 *
	 * @param key
	 * @param <T>
	 * @return
	 */
	public abstract <T> T get(String key);

	/**
	 * 删除数据
	 *
	 * @param key
	 */
	public abstract void remove(String key);

}
