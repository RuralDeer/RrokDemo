package com.cn.request.factory;

import com.cn.request.request.flow.FlowRequest;
import com.cn.request.request.flow.FlowProxyRequest;
import com.cn.request.request.obs.ObsRequest;
import com.cn.request.request.obs.ObsProxyRequest;
import com.cn.request.request.base.ApiRequest;
import com.cn.request.request.base.IRxRequest;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Date: 2019/7/11
 * <p>
 * Time: 2:40 PM
 * <p>
 * author: 鹿文龙
 */
public class RxRequestFactory {


	/**
	 * 直接请求
	 *
	 * @param apiRequest
	 * @param observable
	 * @return
	 */
	public static <T> IRxRequest create(ApiRequest apiRequest, Observable<Call<T>> observable) {
		if (null == apiRequest.getLifecycleOwner()) {
			return new ObsRequest(apiRequest, observable);
		} else {
			return new ObsProxyRequest(apiRequest, observable);
		}
	}

	/**
	 * 背压式请求
	 *
	 * @param apiRequest
	 * @param flowable
	 * @return
	 */
	public static IRxRequest create(ApiRequest apiRequest, Flowable flowable) {
		if (null == apiRequest.getLifecycleOwner()) {
			return new FlowRequest(apiRequest, flowable);
		} else {
			return new FlowProxyRequest(apiRequest, flowable);
		}
	}
}
