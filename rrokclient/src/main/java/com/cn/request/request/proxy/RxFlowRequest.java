package com.cn.request.request.proxy;

import com.cn.request.enums.CacheMode;
import com.cn.request.filters.NetResponsFilter;
import com.cn.request.func.result.CacheResultFunc;
import com.cn.request.func.result.NetResultFunc;
import com.cn.request.transformer.GenerateRequest;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Flowable;

public class RxFlowRequest<T> extends IRxRequest<Flowable<T>> {

    private Flowable<T> upstream;

    public RxFlowRequest(Flowable<T> upstream, CacheMode cacheMode) {
        super(new GenerateRequest().generateRequest(upstream), cacheMode);
        this.upstream = upstream;
    }

    @Override
    public Flowable<T> onlyNetRequest() {
        return upstream
                .map(new NetResultFunc<T>(request))
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }

    @Override
    public Flowable<T> onlyReadCache() {
        return Flowable.just(HttpUtils.getCacheKey(request))
                .map(new CacheResultFunc<T>())
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<T> readCacheThenCacheNetRequest() {
        return Flowable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest().filter(new NetResponsFilter<T>()))
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<T> readCacheThenNetRequest() {
        return Flowable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest())
                .onErrorResumeNext(onlyNetRequest())  //当没有缓存的时候请求网络
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<T> readCacheErrorThenNetRequest() {
        return onlyReadCache()
                .onErrorResumeNext(onlyNetRequest())
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }

    @Override
    public Flowable<T> netRequestErrorThenReadCache() {
        return onlyNetRequest()
                .onErrorResumeNext(onlyReadCache())
                .compose(RxSchedulersTransformer.<T>Flo_io_main());
    }
}
