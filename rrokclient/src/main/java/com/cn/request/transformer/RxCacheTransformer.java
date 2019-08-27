package com.cn.request.transformer;

import com.cn.request.enums.CacheMode;
import com.cn.request.factory.RequestFactory;

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
public class RxCacheTransformer {

    private static final String CLASS_NAME1 = "retrofit2.adapter.rxjava2.BodyObservable";
    private static final String CLASS_NAME2 = "retrofit2.adapter.rxjava2.ResultObservable";

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> obsTransformer(final CacheMode cacheMode) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                String name = upstream.getClass().getName();
                if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
                    return RequestFactory.tObservable(upstream, cacheMode);
                }
                return upstream;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> FlowableTransformer<T, T> flowTransformer(final CacheMode cacheMode) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                String name = upstream.getClass().getName();
                if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
                    return RequestFactory.tFlowable(upstream, cacheMode);
                }
                return upstream;
            }
        };
    }
}
