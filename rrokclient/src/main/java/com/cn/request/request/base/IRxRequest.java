package com.cn.request.request.base;

import com.cn.request.enums.CacheMode;
import com.cn.request.func.result.CacheResultFunc;
import com.cn.request.func.result.NetResultFunc;
import com.cn.request.func.retry.FlowRetryFun;
import com.cn.request.func.retry.ObsRetryFun;
import com.cn.request.func.send.SendRequestFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.transformer.CacheTransformer;
import com.cn.request.utils.HttpUtils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;


/**
 * Date: 2019/7/11
 * <p>
 * Time: 2:26 PM
 * <p>
 * author: 鹿文龙
 */
public abstract class IRxRequest<R, T, Result> {

	protected ApiRequest apiRequest;

	protected IRxRequest(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	/**
	 * 网络请求
	 *
	 * @return
	 */
	protected abstract R doNet();

	/**
	 * 获取缓存
	 *
	 * @return
	 */
	protected abstract R doCache();

	/**
	 * 先获取缓存,然后继续使用网络
	 *
	 * @return
	 */
	protected abstract R doBeforeNet();

	/**
	 * 网络失败后读取缓存
	 *
	 * @return
	 */
	protected abstract R doAfterNetError();


	/**
	 * 获取缓存失败后读取网络数据
	 *
	 * @return
	 */
	protected abstract R doAfterCacheError();


	/**
	 * 回调
	 *
	 * @param result
	 */
	public abstract void send(Result result);


	protected R toSubscribe() {
		if (CacheMode.ONLY_USR_CACHE == apiRequest.getCacheMode()) {
			return doCache();
		} else if (CacheMode.USR_CACHE_NETWORK == apiRequest.getCacheMode()) {
			return doBeforeNet();
		} else if (CacheMode.NETWORK_ERROR_USE_CACHE == apiRequest.getCacheMode()) {
			return doAfterNetError();
		} else if (CacheMode.NOT_CACHE_USE_NETWORK == apiRequest.getCacheMode()) {
			return doAfterCacheError();
		} else {
			return doNet();
		}
	}

	/**
	 * 获取缓存
	 *
	 * @param
	 * @return
	 */
	protected Observable<ApiResponse<T>> cacheObs() {
		return Observable.just(HttpUtils.getCacheKey(apiRequest))
			.map(new CacheResultFunc<T>(apiRequest));
	}

	/**
	 * 获取缓存
	 *
	 * @param
	 * @return
	 */
	protected Observable<ApiResponse<T>> cacheObs(String key) {
		return Observable.just(key)
			.map(new CacheResultFunc<T>(apiRequest));
	}

	/**
	 * 请求网络
	 *
	 * @param observable
	 * @return
	 */
	protected Observable<ApiResponse<T>> netObs(Observable<Call<T>> observable) {
		return observable
			.map(new SendRequestFunc<T>())
			.map(new NetResultFunc<T>(apiRequest))
			.compose(CacheTransformer.<ApiResponse<T>>obsTransformer(CacheMode.NONE_CACHE))
			.retryWhen(new ObsRetryFun(apiRequest.getRetryNum(), apiRequest.getRetryDelay()));
	}


	/**
	 * 获取缓存
	 *
	 * @param
	 * @return
	 */
	protected Flowable<ApiResponse<T>> cacheFlow() {
		return Flowable.just(HttpUtils.getCacheKey(apiRequest))
			.map(new CacheResultFunc<T>(apiRequest));
	}

	/**
	 * 请求网络
	 *
	 * @param flowable
	 * @return
	 */
	protected Flowable<ApiResponse<T>> netFlow(Flowable<Call<T>> flowable) {
		return flowable
			.map(new SendRequestFunc<T>())
			.map(new NetResultFunc<T>(apiRequest))
			.retryWhen(new FlowRetryFun(apiRequest.getRetryNum(),apiRequest.getRetryDelay()));
	}

}
