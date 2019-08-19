package com.cn.request.request.flow;

import com.cn.request.call.ApiFlowResult;
import com.cn.request.call.ApiSubscriber;
import com.cn.request.factory.RxScheduler;
import com.cn.request.request.base.IRxRequest;
import com.cn.request.func.request.FlowCacheRequestFunc;
import com.cn.request.func.request.FlowNetRequestFunc;
import com.cn.request.model.ApiResponse;
import com.cn.request.request.base.ApiRequest;

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
 */
public class FlowRequest<T> extends IRxRequest<Flowable, T, ApiFlowResult<T>> {

	private Flowable<Call<T>> flowable;

	public FlowRequest(ApiRequest apiRequest, Flowable flowable) {
		super(apiRequest);
		this.flowable = flowable;
	}

	@Override
	protected Flowable doNet() {
		return netFlow(flowable)
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Flowable doCache() {
		return Flowable.concatArrayDelayError(cacheFlow().compose(RxScheduler.<ApiResponse<T>>Flo_io_main()), netFlow(flowable).compose(RxScheduler.<ApiResponse<T>>Flo_io_main()));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Flowable doBeforeNet() {
		return Flowable.concatArrayDelayError(cacheFlow().compose(RxScheduler.<ApiResponse<T>>Flo_io_main()), netFlow(flowable).compose(RxScheduler.<ApiResponse<T>>Flo_io_main()));
	}

	@Override
	protected Flowable doAfterNetError() {
		return netFlow(flowable)
			.onErrorResumeNext(new FlowCacheRequestFunc<T>(apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main());

	}

	@Override
	protected Flowable doAfterCacheError() {
		return cacheFlow()
			.onErrorResumeNext(new FlowNetRequestFunc<T>(flowable, apiRequest))
			.compose(RxScheduler.<ApiResponse<T>>Flo_io_main());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void send(final ApiFlowResult<T> apiFlowResult) {
		if (null != apiFlowResult) {
			toSubscribe().subscribe(new ApiSubscriber<T>(apiFlowResult));
		}
	}
}
