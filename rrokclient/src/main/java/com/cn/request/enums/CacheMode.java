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
     * 不使用缓存
     */
    NONE_CACHE,

    /**
     * 只要缓存数据，但是依然会缓存网络数据,
     * <p>
     * 也就是，网络请求下一次生效
     */
    READ_CACHE_THEN_CACHE_NET_REQUEST,

    /**
     * 先使用缓存，不管是否存在，仍然请求网络
     */
    READ_CACHE_THEN_NET_REQUEST,


    /**
     * 请求网络失败后，读取缓存
     */
    NET_REQUEST_ERROR_THEN_READ_CACHE,


    /**
     * 如果缓存不存在才请求网络，否则使用缓存
     */
    READ_CACHE_ERROR_THEN_NET_REQUEST

}
