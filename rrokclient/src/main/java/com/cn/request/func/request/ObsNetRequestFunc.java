package com.cn.request.func.request;

import com.cn.request.func.result.NetResultFunc;
import com.cn.request.func.send.SendRequestFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Call;

/**
 * Date: 2019/7/19
 * <p>
 * Time: 10:21 AM
 * <p>
 * author: 鹿文龙
 */
public class ObsNetRequestFunc<T> implements Function<Throwable, ObservableSource<? extends ApiResponse<T>>> {

	private Observable<Call<T>> observable;

	private ApiRequest apiRequest;

	public ObsNetRequestFunc(Observable<Call<T>> observable, ApiRequest apiRequest) {
		this.observable = observable;
		this.apiRequest = apiRequest;
	}

	@Override
	public ObservableSource<? extends ApiResponse<T>> apply(Throwable throwable) throws Exception {
		return observable
			.map(new SendRequestFunc<T>())
			.map(new NetResultFunc<T>(apiRequest));
	}
}
