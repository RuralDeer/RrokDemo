package com.cn.demo.transformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import okhttp3.Request;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 17:17
 * <p>
 * author: 鹿文龙
 * <p>
 */
public class CacheTransformer {

	private static final String CLASS_NAME1 = "retrofit2.adapter.rxjava2.BodyObservable";
	private static final String CLASS_NAME2 = "retrofit2.adapter.rxjava2.ResultObservable";

	public static <T> ObservableTransformer<T, T> obsTransformer() {
		return new ObservableTransformer<T, T>() {
			@Override
			public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
				String name = upstream.getClass().getName();
				if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
					observable(upstream);
				}
				return upstream;
			}
		};
	}

	public static <T> FlowableTransformer<T, T> flowTransformer() {
		return new FlowableTransformer<T, T>() {
			@Override
			public Publisher<T> apply(Flowable<T> upstream) {
				String name = upstream.getClass().getName();
				if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
					flowable(upstream);
				}
				return upstream;
			}
		};
	}

	private static <T> void observable(Observable<T> observable) {
		Request request = new GenerateRequest().generateRequest(observable);
	}

	private static <T> void flowable(Flowable<T> flowable) {
		Request request = new GenerateRequest().generateRequest(flowable);
	}
}
