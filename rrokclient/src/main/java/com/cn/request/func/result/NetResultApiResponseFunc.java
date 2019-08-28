package com.cn.request.func.result;

import com.cn.request.cache.LruCache;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;
import com.cn.request.utils.HttpStatus;
import com.cn.request.utils.HttpUtils;

import io.reactivex.functions.Function;
import okhttp3.Request;

/**
 * t 转 ApiResponse<T>
 *
 * @param <T>
 */

public class NetResultApiResponseFunc<T> implements Function<T, ApiResponse<T>> {

    private Request request;

    public NetResultApiResponseFunc() {
        this.request = null;
    }

    public NetResultApiResponseFunc(Request request) {
        this.request = request;
    }

    @Override
    public ApiResponse<T> apply(T t) throws Exception {
        if (null != request) {
            String cacheKey = HttpUtils.getCacheKey(request);
            if (!cacheKey.equals(HttpUtils.md5Decode(HttpStatus.CACHE_KEY_Unknown.getValue()))) {
                LruCache.getInstance().put(cacheKey, t);
            }
        }
        return new ApiResponse<T>(t, DataSource.NET);
    }
}
