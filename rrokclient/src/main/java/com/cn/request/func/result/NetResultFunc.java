package com.cn.request.func.result;

import com.cn.request.cache.LruCache;
import com.cn.request.utils.HttpStatus;
import com.cn.request.utils.HttpUtils;

import io.reactivex.functions.Function;
import okhttp3.Request;

/**
 * Date: 2019/7/22
 * <p>
 * Time: 3:48 PM
 * <p>
 * author: 鹿文龙
 */
public class NetResultFunc<T> implements Function<T, T> {

	private Request request;

	public NetResultFunc(Request request) {
		this.request = request;
	}

	@Override
	public T apply(T t) throws Exception {
		String cacheKey = HttpUtils.getCacheKey(request);
		if (!cacheKey.equals(HttpUtils.md5Decode(HttpStatus.CACHE_KEY_Unknown.getValue()))) {
			LruCache.getInstance().put(cacheKey, t);
		}
		return t;
	}
}
