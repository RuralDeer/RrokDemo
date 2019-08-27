package com.cn.request.transformer;

import com.cn.request.enums.CacheMode;
import com.cn.request.factory.RequestFactory;
import com.cn.request.func.result.NetResultApiResponseFunc;
import com.cn.request.model.ApiResponse;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 17:17
 * <p>
 * author: 鹿文龙
 * <p>
 */
public class RxResponseCacheTransformer {

    private static final String CLASS_NAME1 = "retrofit2.adapter.rxjava2.BodyObservable";
    private static final String CLASS_NAME2 = "retrofit2.adapter.rxjava2.ResultObservable";

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, ApiResponse<T>> obsTransformer(final CacheMode cacheMode) {
        return new ObservableTransformer<T, ApiResponse<T>>() {
            @Override
            public ObservableSource<ApiResponse<T>> apply(Observable<T> upstream) {
                String name = upstream.getClass().getName();
                if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
                    return RequestFactory.apiResponseObservable(upstream, cacheMode);
                }
                return upstream.map(new NetResultApiResponseFunc<T>());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> FlowableTransformer<T, ApiResponse<T>> flowTransformer(final CacheMode cacheMode) {
        return new FlowableTransformer<T, ApiResponse<T>>() {
            @Override
            public Publisher<ApiResponse<T>> apply(Flowable<T> upstream) {
                String name = upstream.getClass().getName();
                if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
                    return RequestFactory.apiResponseFlowable(upstream, cacheMode);
                }
                return upstream.map(new NetResultApiResponseFunc<T>());
            }
        };
    }
}
