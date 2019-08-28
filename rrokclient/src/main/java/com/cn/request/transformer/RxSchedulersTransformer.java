package com.cn.request.transformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 4:33 PM
 * <p>
 * author: 鹿文龙
 * <p>
 * description：RXjava 线程调度
 */
public class RxSchedulersTransformer {
	/**
	 * 统一线程处理
	 * <p>
	 * 背压
	 *
	 * @param <T> 指定的泛型类型
	 * @return FlowableTransformer
	 * <p>
	 * https://www.jianshu.com/p/ff8167c1d191/
	 */

	public static <T> FlowableTransformer<T, T> floIoMain() {
		return new FlowableTransformer<T, T>() {
			@Override
			public Publisher<T> apply(@NonNull Flowable<T> upstream) {
				return upstream.subscribeOn(Schedulers.io())
					.unsubscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread());
			}
		};
	}

	/**
	 * 统一线程处理
	 *
	 * @param <T> 指定的泛型类型
	 * @return ObservableTransformer
	 */
	public static <T> ObservableTransformer<T, T> obsIoMain() {
		return new ObservableTransformer<T, T>() {
			@Override
			public ObservableSource<T> apply(Observable<T> upstream) {
				return upstream.subscribeOn(Schedulers.io())
					.unsubscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread());
			}
		};
	}
}
