package com.cn.request.func.result;

import com.cn.request.cache.LruCache;
import com.cn.request.enums.CacheMode;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.utils.HttpStatus;

import io.reactivex.functions.Function;

/**
 * Date: 2019/7/18
 * <p>
 * Time: 5:00 PM
 * <p>
 * author: 鹿文龙
 */
public class CacheResultFunc<T> implements Function<String, ApiResponse<T>> {

	private ApiRequest apiRequest;

	public CacheResultFunc(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	@Override
	public ApiResponse<T> apply(String key) throws Exception {
		T t = LruCache.getInstance().<T>get(key);
		if (null == t)
			throw new Exception(apiRequest.getCacheMode() == CacheMode.ONLY_USR_CACHE ? HttpStatus.NO_CACHE.getValue() : HttpStatus.NO_CACHE_NOT_RETURN.getValue());
		return new ApiResponse<>(t, key, apiRequest.getCacheMode(), DataSource.CACHE);
	}
}
