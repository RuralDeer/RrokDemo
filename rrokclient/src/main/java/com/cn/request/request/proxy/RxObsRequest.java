package com.cn.request.request.proxy;

import com.cn.request.enums.CacheMode;
import com.cn.request.filters.NetResponsFilter;
import com.cn.request.func.result.CacheResultFunc;
import com.cn.request.func.result.NetResultFunc;
import com.cn.request.transformer.GenerateRequest;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class RxObsRequest<T> extends IRxRequest<Observable<T>> {

    private Observable<T> upstream;

    public RxObsRequest(Observable<T> upstream, CacheMode cacheMode) {
        super(new GenerateRequest().generateRequest(upstream), cacheMode);
        this.upstream = upstream;
    }

    @Override
    public Observable<T> onlyNetRequest() {
        return upstream
                .map(new NetResultFunc<T>(request))
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }

    @Override
    public Observable<T> onlyReadCache() {
        return Observable.just(HttpUtils.getCacheKey(request))
                .map(new CacheResultFunc<T>())
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<T> readCacheThenCacheNetRequest() {
        return Observable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest().filter(new NetResponsFilter<T>()))
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<T> readCacheThenNetRequest() {
        return Observable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest())
                .onErrorResumeNext(onlyNetRequest())//当没有缓存的时候请求网络
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<T> readCacheErrorThenNetRequest() {
        return onlyReadCache()
                .onErrorResumeNext(onlyNetRequest())
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }

    @Override
    public Observable<T> netRequestErrorThenReadCache() {
        return onlyNetRequest()
                .onErrorResumeNext(onlyReadCache())
                .compose(RxSchedulersTransformer.<T>Obs_io_main());
    }
}
