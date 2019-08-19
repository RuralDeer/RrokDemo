package com.cn.request.request.obs;

import com.cn.request.call.ApiObsResult;
import com.cn.request.factory.RxScheduler;
import com.cn.request.request.base.IRxRequest;
import com.cn.request.func.request.ObsCacheRequestFunc;
import com.cn.request.func.request.ObsNetRequestFunc;
import com.cn.request.call.ApiSubscriber;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Date: 2019/7/11
 * <p>
 * Time: 2:30 PM
 * <p>
 * author: 鹿文龙
 */
public class ObsRequest<T> extends IRxRequest<Observable, T, ApiObsResult<T>> {

	private Observable<Call<T>> observable;

	public ObsRequest(ApiRequest apiRequest, Observable observable) {
		super(apiRequest);
		this.observable = observable;
	}

	@Override
	protected Observable doNet() {
		return netObs(observable)
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Observable doCache() {
		return Observable.concatArrayDelayError(cacheObs().compose(RxScheduler.<ApiResponse<T>>Obs_io_main()), netObs(observable).compose(RxScheduler.<ApiResponse<T>>Obs_io_main()));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Observable doBeforeNet() {
		return Observable.concatArrayDelayError(cacheObs().compose(RxScheduler.<ApiResponse<T>>Obs_io_main()), netObs(observable).compose(RxScheduler.<ApiResponse<T>>Obs_io_main()));
	}

	@Override
	protected Observable doAfterNetError() {
		return netObs(observable)
			.onErrorResumeNext(new ObsCacheRequestFunc<T>(apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main());

	}

	@Override
	@SuppressWarnings("unchecked")
	protected Observable doAfterCacheError() {
		return cacheObs()
			.onErrorResumeNext(new ObsNetRequestFunc<T>(observable,apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Obs_io_main());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void send(final ApiObsResult<T> apiObsResult) {
		if (null != apiObsResult) {
			toSubscribe().subscribe(new ApiSubscriber<T>(apiObsResult));
		}
	}
}

