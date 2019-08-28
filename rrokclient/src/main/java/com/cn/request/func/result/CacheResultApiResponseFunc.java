package com.cn.request.func.result;

import com.cn.request.cache.LruCache;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;

import io.reactivex.functions.Function;

/**
 * Date: 2019/7/18
 * <p>
 * Time: 5:00 PM
 * <p>
 * author: 鹿文龙
 */
public class CacheResultApiResponseFunc<T> implements Function<String, ApiResponse<T>> {
    @Override
    public ApiResponse<T> apply(String key) throws Exception {
        T t = LruCache.getInstance().<T>get(key);
        if(null == t){
            throw new NullPointerException("cache is empty");
        }
        return new ApiResponse<T>(t, DataSource.CACHE);
    }
}

