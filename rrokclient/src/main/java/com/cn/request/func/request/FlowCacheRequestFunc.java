package com.cn.request.func.request;

import com.cn.request.func.result.CacheResultFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.utils.HttpUtils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Date: 2019/7/18
 * <p>
 * Time: 5:03 PM
 * <p>
 * author: 鹿文龙
 */
public class FlowCacheRequestFunc<T> implements Function<Throwable, Publisher<? extends ApiResponse<T>>> {

	private ApiRequest apiRequest;

	public FlowCacheRequestFunc(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}


	@Override
	public Publisher<? extends ApiResponse<T>> apply(Throwable throwable) throws Exception {
		return Flowable.just(HttpUtils.getCacheKey(apiRequest))
			.map(new CacheResultFunc<T>(apiRequest));
	}
}
