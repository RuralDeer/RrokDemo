package com.cn.request.enums;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 5:25 PM
 * <p>
 * author: 鹿文龙
 */
public enum CacheMode {

	/**
	 * 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
	 */
	DEFAULT,

	/**
	 * 不使用缓存
	 */
	NONE_CACHE,

	/**
	 * 只用缓存,但是依然会请求网络，只是不回调
	 */
	ONLY_USR_CACHE,

	/**
	 * 先使用缓存，不管是否存在，仍然请求网络
	 */
	USR_CACHE_NETWORK,


	/**
	 * 请求网络失败后，读取缓存
	 */
	NETWORK_ERROR_USE_CACHE,


	/**
	 * 如果缓存不存在才请求网络，否则使用缓存
	 */
	NOT_CACHE_USE_NETWORK

}
