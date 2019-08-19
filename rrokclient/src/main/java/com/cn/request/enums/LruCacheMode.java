package com.cn.request.enums;

/**
 * Date: 2019/7/16
 * <p>
 * Time: 4:20 PM
 * <p>
 * author: 鹿文龙
 */
public enum LruCacheMode {
	/**
	 * 内存缓存加磁盘缓存
	 */
	ALL,

	/**
	 * 磁盘缓存
	 */
	DISK,

	/**
	 * 内存缓存
	 */
	MEMORY
}
