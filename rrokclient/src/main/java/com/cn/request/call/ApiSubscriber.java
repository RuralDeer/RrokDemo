package com.cn.request.call;

import com.cn.request.enums.CacheMode;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;
import com.cn.request.utils.HttpStatus;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/7/22
 * <p>
 * Time: 6:08 PM
 * <p>
 * author: 鹿文龙
 */
public class ApiSubscriber<T> implements Observer<ApiResponse<T>>, FlowableSubscriber<ApiResponse<T>> {

	private ApiBaseResult<T> apiBaseResult;

	public ApiSubscriber(ApiBaseResult<T> apiBaseResult) {
		this.apiBaseResult = apiBaseResult;
	}

	@Override
	public void onSubscribe(Disposable d) {
		if (apiBaseResult instanceof ApiObsResult) {
			ApiObsResult apiObsResult = (ApiObsResult) apiBaseResult;
			apiObsResult.onDisposable(d);
		}
	}


	@Override
	public void onNext(ApiResponse<T> apiResponse) {
		try {
			if (apiResponse.cacheMode == CacheMode.ONLY_USR_CACHE) {
				if (apiResponse.dataSource == DataSource.CACHE) {
					apiBaseResult.onSuccess(apiResponse);
				}
			} else {
				apiBaseResult.onSuccess(apiResponse);
			}

		} catch (Exception e) {
			apiBaseResult.onFailure(e);
		}
	}

	@Override
	public void onError(Throwable e) {
		if (!e.getMessage().equals(HttpStatus.NO_CACHE_NOT_RETURN.getValue())) {
			apiBaseResult.onFailure(e);
		}
	}

	@Override
	public void onComplete() {
		apiBaseResult.onComplete();
	}

	@Override
	public void onSubscribe(Subscription s) {
		if (apiBaseResult instanceof ApiFlowResult) {
			ApiFlowResult apiFlowResult = (ApiFlowResult) apiBaseResult;
			apiFlowResult.onSubscription(s);
		}
	}
}
