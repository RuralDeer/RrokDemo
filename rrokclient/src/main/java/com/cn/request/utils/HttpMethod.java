package com.cn.request.utils;

/**
 * Date: 2019/7/11
 * <p>
 * Time: 5:19 PM
 * <p>
 * author: 鹿文龙
 */
public enum HttpMethod {

	GET("GET"),

	POST("POST"),

	PUT("PUT"),

	DELETE("DELETE"),

	HEAD("HEAD"),

	PATCH("PATCH"),

	OPTIONS("OPTIONS"),

	TRACE("TRACE");

	private final String value;

	HttpMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
