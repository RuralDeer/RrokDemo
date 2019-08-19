package com.cn.request.request;

import android.arch.lifecycle.LifecycleOwner;

import com.cn.request.call.ApiFlowResult;
import com.cn.request.enums.CacheMode;
import com.cn.request.factory.RxRequestFactory;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.request.base.IRxRequest;

import io.reactivex.Flowable;
import retrofit2.Call;

/**
 * Date: 2019/7/16
 * <p>
 * Time: 4:43 PM
 * <p>
 * author: 鹿文龙
 */
public class FlowApiRequest<T> extends ApiRequest<T, FlowApiRequest<T>, ApiFlowResult<T>> {

	public FlowApiRequest(Call<T> call) {
		super(call);
	}

	@Override
	public FlowApiRequest<T> setCacheMode(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
		return this;
	}


	@Override
	public FlowApiRequest<T> setLifecycleOwner(LifecycleOwner lifecycleOwner) {
		this.lifecycleOwner = lifecycleOwner;
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void enqueue(ApiFlowResult<T> apiFlowResult) {
		Flowable<Call<T>> flowable = Flowable.just(call);
		IRxRequest request = RxRequestFactory.create(this, flowable);
		request.send(apiFlowResult);
	}
}
