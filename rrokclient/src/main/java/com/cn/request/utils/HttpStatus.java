package com.cn.request.utils;

/**
 * Date: 2019/7/30
 * <p>
 * Time: 3:13 PM
 * <p>
 * author: 鹿文龙
 */
public enum HttpStatus {

	NO_CACHE_NOT_RETURN("Do not return empty cache"),

	NO_CACHE("cache empty"),

	/**
	 * 此模式下不缓存数据
	 */
	CACHE_KEY_Unknown("Unknown");

	private final String value;

	HttpStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
