package com.cn.request.request.obs;

import android.arch.lifecycle.Lifecycle;

import com.cn.request.call.ApiObsResult;
import com.cn.request.call.ApiSubscriber;
import com.cn.request.factory.RxScheduler;
import com.cn.request.request.base.IRxRequest;
import com.cn.request.func.request.ObsCacheRequestFunc;
import com.cn.request.func.request.ObsNetRequestFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Date: 2019/7/19
 * <p>
 * Time: 10:24 AM
 * <p>
 * author: 鹿文龙
 * <p>
 * 利用 AutoDispose 管理 RxJava 解决 Activity Fragment 内存泄漏
 */
public class ObsProxyRequest<T> extends IRxRequest<ObservableSubscribeProxy, T, ApiObsResult<T>> {

	private Observable<Call<T>> observable;

	public ObsProxyRequest(ApiRequest apiRequest, Observable observable) {
		super(apiRequest);
		this.observable = observable;
	}

	@Override
	protected ObservableSubscribeProxy doNet() {
		return netObs(observable)
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ObservableSubscribeProxy doCache() {
		return Observable.concatArrayDelayError(cacheObs().compose(RxScheduler.<ApiResponse<T>>Obs_io_main()), netObs(observable).compose(RxScheduler.<ApiResponse<T>>Obs_io_main()))
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));

	}

	@Override
	@SuppressWarnings("unchecked")
	protected ObservableSubscribeProxy doBeforeNet() {
		return Observable.concatArrayDelayError(cacheObs().compose(RxScheduler.<ApiResponse<T>>Obs_io_main()), netObs(observable).compose(RxScheduler.<ApiResponse<T>>Obs_io_main()))
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	protected ObservableSubscribeProxy doAfterNetError() {
		return netObs(observable)
			.onErrorResumeNext(new ObsCacheRequestFunc<T>(apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	protected ObservableSubscribeProxy doAfterCacheError() {
		return cacheObs()
			.onErrorResumeNext(new ObsNetRequestFunc<T>(observable, apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void send(final ApiObsResult<T> apiObsResult) {
		if (null != apiObsResult) {
			toSubscribe().subscribe(new ApiSubscriber<T>(apiObsResult));
		}
	}
}
