package com.cn.request.factory;

import com.cn.request.enums.CacheMode;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.proxy.RxFlowRequest;
import com.cn.request.request.proxy.RxFlowRequestApiResponse;
import com.cn.request.request.proxy.RxObsRequest;
import com.cn.request.request.proxy.RxObsRequestApiResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;


public class RequestFactory {

    @SuppressWarnings("unchecked")
    public static <T> Observable tObservable(Observable<T> upstream, CacheMode cacheMode) {
        RxObsRequest<T> tRxObsRequest = new RxObsRequest<>(upstream, cacheMode);
        if (cacheMode == CacheMode.READ_CACHE_THEN_CACHE_NET_REQUEST) {
            return tRxObsRequest.readCacheThenCacheNetRequest();
        } else if (cacheMode == CacheMode.READ_CACHE_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheThenNetRequest();
        } else if (cacheMode == CacheMode.NET_REQUEST_ERROR_THEN_READ_CACHE) {
            return tRxObsRequest.netRequestErrorThenReadCache();
        } else if (cacheMode == CacheMode.READ_CACHE_ERROR_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheErrorThenNetRequest();
        }
        return tRxObsRequest.onlyNetRequest();
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<ApiResponse<T>> apiResponseObservable(Observable<T> upstream, CacheMode cacheMode) {
        RxObsRequestApiResponse<T> tRxObsRequest = new RxObsRequestApiResponse<>(upstream, cacheMode);
        if (cacheMode == CacheMode.READ_CACHE_THEN_CACHE_NET_REQUEST) {
            return tRxObsRequest.readCacheThenCacheNetRequest();
        } else if (cacheMode == CacheMode.READ_CACHE_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheThenNetRequest();
        } else if (cacheMode == CacheMode.NET_REQUEST_ERROR_THEN_READ_CACHE) {
            return tRxObsRequest.netRequestErrorThenReadCache();
        } else if (cacheMode == CacheMode.READ_CACHE_ERROR_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheErrorThenNetRequest();
        }
        return tRxObsRequest.onlyNetRequest();
    }

    @SuppressWarnings("unchecked")
    public static <T> Flowable<T> tFlowable(Flowable<T> upstream, CacheMode cacheMode) {
        RxFlowRequest<T> tRxObsRequest = new RxFlowRequest<>(upstream, cacheMode);
        if (cacheMode == CacheMode.READ_CACHE_THEN_CACHE_NET_REQUEST) {
            return tRxObsRequest.readCacheThenCacheNetRequest();
        } else if (cacheMode == CacheMode.READ_CACHE_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheThenNetRequest();
        } else if (cacheMode == CacheMode.NET_REQUEST_ERROR_THEN_READ_CACHE) {
            return tRxObsRequest.netRequestErrorThenReadCache();
        } else if (cacheMode == CacheMode.READ_CACHE_ERROR_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheErrorThenNetRequest();
        }
        return tRxObsRequest.onlyNetRequest();
    }

    @SuppressWarnings("unchecked")
    public static <T> Flowable<ApiResponse<T>> apiResponseFlowable(Flowable<T> upstream, CacheMode cacheMode) {
        RxFlowRequestApiResponse<T> tRxObsRequest = new RxFlowRequestApiResponse<>(upstream, cacheMode);
        if (cacheMode == CacheMode.READ_CACHE_THEN_CACHE_NET_REQUEST) {
            return tRxObsRequest.readCacheThenCacheNetRequest();
        } else if (cacheMode == CacheMode.READ_CACHE_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheThenNetRequest();
        } else if (cacheMode == CacheMode.NET_REQUEST_ERROR_THEN_READ_CACHE) {
            return tRxObsRequest.netRequestErrorThenReadCache();
        } else if (cacheMode == CacheMode.READ_CACHE_ERROR_THEN_NET_REQUEST) {
            return tRxObsRequest.readCacheErrorThenNetRequest();
        }
        return tRxObsRequest.onlyNetRequest();
    }


}
