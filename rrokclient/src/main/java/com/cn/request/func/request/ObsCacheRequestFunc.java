package com.cn.request.func.request;

import com.cn.request.func.result.CacheResultFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Date: 2019/7/18
 * <p>
 * Time: 5:03 PM
 * <p>
 * author: 鹿文龙
 */
public class ObsCacheRequestFunc<T> implements Function<Throwable, ObservableSource<? extends ApiResponse<T>>> {

	private ApiRequest apiRequest;

	public ObsCacheRequestFunc(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	@Override
	public ObservableSource<? extends ApiResponse<T>> apply(Throwable throwable) throws Exception {
		return Observable.just(HttpUtils.getCacheKey(apiRequest))
			.map(new CacheResultFunc<T>(apiRequest));
	}
}
