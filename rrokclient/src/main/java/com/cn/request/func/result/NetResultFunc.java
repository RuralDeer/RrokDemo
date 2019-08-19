package com.cn.request.func.result;

import com.cn.request.cache.LruCache;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.utils.HttpStatus;
import com.cn.request.utils.HttpUtils;

import io.reactivex.functions.Function;

/**
 * Date: 2019/7/22
 * <p>
 * Time: 3:48 PM
 * <p>
 * author: 鹿文龙
 */
public class NetResultFunc<T> implements Function<T, ApiResponse<T>> {

	private ApiRequest apiRequest;

	public NetResultFunc(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	@Override
	public ApiResponse<T> apply(T t) throws Exception {
		String cacheKey = HttpUtils.getCacheKey(apiRequest);
		if (!cacheKey.equals(HttpUtils.md5Decode(HttpStatus.CACHE_KEY_Unknown.getValue()))) {
			LruCache.getInstance().put(cacheKey, t);
		} else {
			cacheKey = HttpStatus.CACHE_KEY_Unknown.getValue();
		}
		return new ApiResponse<>(t, cacheKey, apiRequest.getCacheMode(), DataSource.NET);
	}
}
