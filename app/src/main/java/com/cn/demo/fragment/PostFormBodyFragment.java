package com.cn.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.demo.Api;
import com.cn.demo.R;
import com.cn.demo.bean.TestBean;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.RrokClient;
import com.cn.request.call.ApiObsResult;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;

public class PostFormBodyFragment extends BaseFragment {

	@Override
	protected Call<List<TestBean>> getCall() {
		return RrokClient.create(Api.class).post(page, offeset);
	}

	@Override
	protected ApiObsResult<List<TestBean>> apiObsResult() {
		return apiObsResult;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post_form_body, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init(view);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSendEvent(SendEvent event) {
		mErrorTv.setText("请求中...");
		mNetAdapter.clear();
		mCacheAdapter.clear();
		switch (event.position) {
			case 0:
				req0();
				break;
			case 1:
				req1();
				break;
			case 2:
				req2();
				break;
			case 3:
				req3();
				break;
			case 4:
				req4();
				break;
		}
	}

	private ApiObsResult<List<TestBean>> apiObsResult = new ApiObsResult<List<TestBean>>() {
		@Override
		public void onDisposable(Disposable disposable) {

		}

		@Override
		public void onSuccess(ApiResponse<List<TestBean>> apiResponse) {
			if (apiResponse.dataSource == DataSource.NET) {
				mNetAdapter.addData(apiResponse.obj);
			} else {
				mCacheAdapter.addData(apiResponse.obj);
			}
		}

		@Override
		public void onFailure(Throwable throwable) {
			mErrorTv.setText(throwable.getMessage());
		}

		@Override
		public void onComplete() {
			mErrorTv.setText("请求完毕");
		}
	};
}
