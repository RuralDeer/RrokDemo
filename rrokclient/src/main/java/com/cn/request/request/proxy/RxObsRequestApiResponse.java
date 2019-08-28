package com.cn.request.request.proxy;

import com.cn.request.enums.CacheMode;
import com.cn.request.filters.NetApiResponsFilter;
import com.cn.request.func.result.CacheResultApiResponseFunc;
import com.cn.request.func.result.NetResultApiResponseFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.transformer.GenerateRequest;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Observable;

public class RxObsRequestApiResponse<T> extends IRxRequest<Observable<ApiResponse<T>>> {

    private Observable<T> upstream;

    public RxObsRequestApiResponse(Observable<T> upstream, CacheMode cacheMode) {
        super(new GenerateRequest().generateRequest(upstream), cacheMode);
        this.upstream = upstream;
    }

    @Override
    public Observable<ApiResponse<T>> onlyNetRequest() {
        return upstream
                .map(new NetResultApiResponseFunc<T>(request))
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }

    @Override
    public Observable<ApiResponse<T>> onlyReadCache() {
        return Observable.just(HttpUtils.getCacheKey(request))
                .map(new CacheResultApiResponseFunc<T>())
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ApiResponse<T>> readCacheThenCacheNetRequest() {
        return Observable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest().filter(new NetApiResponsFilter<T>()))
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ApiResponse<T>> readCacheThenNetRequest() {
        return Observable
                .concatArrayDelayError(onlyReadCache(), onlyNetRequest())
                .onErrorResumeNext(onlyNetRequest())//当没有缓存的时候请求网络
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ApiResponse<T>> readCacheErrorThenNetRequest() {
        return onlyReadCache()
                .onErrorResumeNext(onlyNetRequest())
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }

    @Override
    public Observable<ApiResponse<T>> netRequestErrorThenReadCache() {
        return onlyNetRequest()
                .onErrorResumeNext(onlyReadCache())
                .compose(RxSchedulersTransformer.<ApiResponse<T>>obsIoMain());
    }
}
