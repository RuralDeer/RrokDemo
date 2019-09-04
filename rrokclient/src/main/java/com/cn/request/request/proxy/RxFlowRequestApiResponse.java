package com.cn.request.request.proxy;

import com.cn.request.enums.CacheMode;
import com.cn.request.filters.NetApiResponsFilter;
import com.cn.request.func.result.CacheResultApiResponseFunc;
import com.cn.request.func.result.NetResultApiResponseFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.transformer.GenerateRequest;
import com.cn.request.transformer.RxScheduler;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Flowable;

public class RxFlowRequestApiResponse<T> extends IRxRequest<Flowable<ApiResponse<T>>> {

    private Flowable<T> upstream;

    public RxFlowRequestApiResponse(Flowable<T> upstream, CacheMode cacheMode) {
        super(new GenerateRequest().generateRequest(upstream), cacheMode);
        this.upstream = upstream;
    }

    @Override
    public Flowable<ApiResponse<T>> onlyNetRequest() {
        return upstream
                .map(new NetResultApiResponseFunc<T>(request))
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }

    @Override
    public Flowable<ApiResponse<T>> onlyReadCache() {
        return Flowable.just(HttpUtils.getCacheKey(request))
                .map(new CacheResultApiResponseFunc<T>())
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<ApiResponse<T>> readCacheThenCacheNetRequest() {
        return Flowable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest().filter(new NetApiResponsFilter<T>()))
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<ApiResponse<T>> readCacheThenNetRequest() {
        return Flowable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest())
                .onErrorResumeNext(onlyNetRequest())//当没有缓存的时候请求网络
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flowable<ApiResponse<T>> readCacheErrorThenNetRequest() {
        return onlyReadCache()
                .onErrorResumeNext(onlyNetRequest())
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }

    @Override
    public Flowable<ApiResponse<T>> netRequestErrorThenReadCache() {
        return onlyNetRequest()
                .onErrorResumeNext(onlyReadCache())
                .compose(RxScheduler.<ApiResponse<T>>floIoMain());
    }
}
