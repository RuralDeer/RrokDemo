package com.cn.request.model;

import com.cn.request.enums.CacheMode;
import com.cn.request.enums.DataSource;

/**
 * Date: 2019/7/22
 * <p>
 * Time: 2:15 PM
 * <p>
 * author: 鹿文龙
 */
public final class ApiResponse<T> {

	public T obj;

	public String cacheKey;

	public CacheMode cacheMode;

	public DataSource dataSource;


	public ApiResponse(T obj, String cacheKey, CacheMode cacheMode, DataSource dataSource) {
		this.obj = obj;
		this.cacheKey = cacheKey;
		this.cacheMode = cacheMode;
		this.dataSource = dataSource;

	}
}
