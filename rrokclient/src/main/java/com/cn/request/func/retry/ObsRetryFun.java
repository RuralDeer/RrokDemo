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
public class ObsRetryFun implements Function<Observable<? extends Throwable>, Observable<?>> {

	private int maxNum;//最大出错重试次数

	private int retryCount = 0;//当前出错重试次数

	private int retryDelay;//重试间隔时间

	public ObsRetryFun(int maxNum, int retryDelay) {
		this.maxNum = maxNum;
		this.retryDelay = retryDelay;
	}

	@Override
	public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
		return observable
			.flatMap(new Function<Throwable, ObservableSource<?>>() {
				@Override
				public ObservableSource<?> apply(Throwable throwable) throws Exception {

					Log.e("ObsRetryFun", "1:"+throwable.getMessage());

					Log.d("ObsRetryFun", "retryCount:" + retryCount);

					if (++retryCount <= maxNum) {
						return Observable.timer(retryDelay * retryCount, TimeUnit.MILLISECONDS);
					}
					Log.e("ObsRetryFun", "2:"+throwable.getMessage());
					return Observable.error(throwable);
				}
			});
	}
}
