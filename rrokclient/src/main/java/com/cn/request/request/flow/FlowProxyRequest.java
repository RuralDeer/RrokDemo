package com.cn.request.request.flow;

import android.arch.lifecycle.Lifecycle;

import com.cn.request.call.ApiFlowResult;
import com.cn.request.factory.RxScheduler;
import com.cn.request.request.base.IRxRequest;
import com.cn.request.func.request.FlowCacheRequestFunc;
import com.cn.request.func.request.FlowNetRequestFunc;
import com.cn.request.call.ApiSubscriber;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.FlowableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Flowable;
import retrofit2.Call;

/**
 * Date: 2019/7/16
 * <p>
 * Time: 10:21 AM
 * <p>
 * author: 鹿文龙
 * <p>
 * 背压处理模式
 * <p>
 * 利用 AutoDispose 管理 RxJava 解决 Activity Fragment 内存泄漏
 */
public class FlowProxyRequest<T> extends IRxRequest<FlowableSubscribeProxy, T, ApiFlowResult<T>> {

	private Flowable<Call<T>> flowable;

	public FlowProxyRequest(ApiRequest apiRequest, Flowable flowable) {
		super(apiRequest);
		this.flowable = flowable;
	}

	@Override
	protected FlowableSubscribeProxy doNet() {
		return netFlow(flowable)
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	protected FlowableSubscribeProxy doCache() {
		return Flowable.concatArrayDelayError(cacheFlow().compose(RxScheduler.<ApiResponse<T>>Flo_io_main()), netFlow(flowable).compose(RxScheduler.<ApiResponse<T>>Flo_io_main()))
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected FlowableSubscribeProxy doBeforeNet() {
		return Flowable.concatArrayDelayError(cacheFlow().compose(RxScheduler.<ApiResponse<T>>Flo_io_main()), netFlow(flowable).compose(RxScheduler.<ApiResponse<T>>Flo_io_main()))
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	protected FlowableSubscribeProxy doAfterNetError() {
		return netFlow(flowable)
			.onErrorResumeNext(new FlowCacheRequestFunc<T>(apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	protected FlowableSubscribeProxy doAfterCacheError() {
		return cacheFlow()
			.onErrorResumeNext(new FlowNetRequestFunc<T>(flowable,apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main())
			.as(AutoDispose.<ApiResponse<T>>autoDisposable(AndroidLifecycleScopeProvider.from(apiRequest.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void send(final ApiFlowResult<T> apiFlowResult) {
		if (null != apiFlowResult) {
			toSubscribe().subscribe(new ApiSubscriber<T>(apiFlowResult));
		}
	}
}
