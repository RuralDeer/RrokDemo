package com.cn.request.func.request;

import com.cn.request.func.result.NetResultFunc;
import com.cn.request.func.send.SendRequestFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.Call;

/**
 * Date: 2019/7/19
 * <p>
 * Time: 10:21 AM
 * <p>
 * author: 鹿文龙
 */
public class FlowNetRequestFunc<T> implements Function<Throwable, Publisher<? extends ApiResponse<T>>> {

	private Flowable<Call<T>> flowable;

	private ApiRequest apiRequest;

	public FlowNetRequestFunc(Flowable<Call<T>> flowable, ApiRequest apiRequest) {
		this.flowable = flowable;
		this.apiRequest = apiRequest;
	}


	@Override
	public Publisher<? extends ApiResponse<T>> apply(Throwable throwable) throws Exception {
		return flowable
			.map(new SendRequestFunc<T>())
			.map(new NetResultFunc<T>(apiRequest));
	}
}
