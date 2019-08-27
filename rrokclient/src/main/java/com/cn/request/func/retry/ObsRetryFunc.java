package com.cn.request.func.retry;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 10:13
 * <p>
 * author: 鹿文龙
 */
public class ObsRetryFunc implements Function<Observable<? extends Throwable>, Observable<?>> {

	private int maxNum;//最大出错重试次数

	private int retryCount = 0;//当前出错重试次数

	private int retryDelay;//重试间隔时间

	public ObsRetryFunc(int maxNum, int retryDelay) {
		this.maxNum = maxNum;
		this.retryDelay = retryDelay;
	}

	@Override
	public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
		return observable
			.flatMap(new Function<Throwable, ObservableSource<?>>() {
				@Override
				public ObservableSource<?> apply(Throwable throwable) throws Exception {
					if (++retryCount <= maxNum) {
						return Observable.timer(retryDelay * retryCount, TimeUnit.MILLISECONDS);
					}
					return Observable.error(throwable);
				}
			});
	}
}
