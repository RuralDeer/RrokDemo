package com.cn.request.func.retry;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 10:23
 * <p>
 * author: 鹿文龙
 */
public class FlowRetryFunc implements Function<Flowable<Throwable>, Publisher<?>> {

	private int maxNum;//最大出错重试次数

	private int retryCount = 0;//当前出错重试次数

	private int retryDelay;//重试间隔时间

	public FlowRetryFunc(int maxNum, int retryDelay) {
		this.maxNum = maxNum;
		this.retryDelay = retryDelay;
	}


	@Override
	public Publisher<?> apply(Flowable<Throwable> flowable) throws Exception {
		return flowable
			.flatMap(new Function<Throwable, Publisher<?>>() {
				@Override
				public Publisher<?> apply(Throwable throwable) throws Exception {
					if (++retryCount <= maxNum) {
						return Flowable.timer(retryDelay * retryCount, TimeUnit.MILLISECONDS);
					}
					return Flowable.error(throwable);
				}
			});
	}
}
