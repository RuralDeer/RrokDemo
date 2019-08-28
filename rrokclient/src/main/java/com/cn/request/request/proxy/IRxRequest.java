package com.cn.request.request.proxy;

import com.cn.request.enums.CacheMode;

import okhttp3.Request;


public abstract class IRxRequest<Rx> {

    protected Request request;

    protected CacheMode cacheMode;

    public IRxRequest(Request request, CacheMode cacheMode) {
        this.request = request;
        this.cacheMode = cacheMode;
    }

    /**
     * 只要网络数据
     *
     * @return
     */
    protected abstract Rx onlyNetRequest();

    /**
     * 只要缓存数据
     *
     * @return
     */
    protected abstract Rx onlyReadCache();

    /**
     * 只要缓存数据，但是依然会缓存网络数据,
     * <p>
     * 也就是，网络请求下一次生效
     *
     * @return
     */
    protected abstract Rx readCacheThenCacheNetRequest();

    /**
     * 先读缓存，后读网络
     *
     * @return
     */
    protected abstract Rx readCacheThenNetRequest();

    /**
     * 读取缓存失败 才会获取网络
     *
     * @return
     */
    protected abstract Rx readCacheErrorThenNetRequest();


    /**
     * 获取网络失败，才会读取缓存
     *
     * @return
     */
    protected abstract Rx netRequestErrorThenReadCache();
}
