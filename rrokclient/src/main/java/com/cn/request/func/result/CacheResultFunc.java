package com.cn.request.func.result;

import com.cn.request.cache.LruCache;

import io.reactivex.functions.Function;

/**
 * Date: 2019/7/18
 * <p>
 * Time: 5:00 PM
 * <p>
 * author: 鹿文龙
 */
public class CacheResultFunc<T> implements Function<String, T> {
	@Override
	public T apply(String key) throws Exception {
		T t = LruCache.getInstance().<T>get(key);
		if(null == t){
			throw new NullPointerException("cache is empty");
		}
		return t;
	}
}

