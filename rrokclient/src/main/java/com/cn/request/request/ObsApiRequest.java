package com.cn.request.request;

import android.arch.lifecycle.LifecycleOwner;

import com.cn.request.call.ApiObsResult;
import com.cn.request.enums.CacheMode;
import com.cn.request.factory.RxRequestFactory;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.request.base.IRxRequest;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 4:40 PM
 * <p>
 * author: 鹿文龙
 */
public class ObsApiRequest<T> extends ApiRequest<T, ObsApiRequest<T>, ApiObsResult<T>> {

	public ObsApiRequest(Call<T> call) {
		super(call);
	}

	@Override
	public ObsApiRequest<T> setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
		return this;
	}

	@Override
	public ObsApiRequest<T> setLifecycleOwner(LifecycleOwner lifecycleOwner) {
		this.lifecycleOwner = lifecycleOwner;
		return this;
	}

	@Override
	public ObsApiRequest<T> setRetry(int retryNum,int retryDelay) {
		this.retryNum = retryNum;
		this.retryDelay = retryDelay;
		return this;
	}

	@Override
	public void enqueue(ApiObsResult<T> apiObsResult) {
		Observable<Call<T>> observable = Observable.just(call);
		IRxRequest request = RxRequestFactory.create(this, observable);
		request.send(apiObsResult);
	}
}
