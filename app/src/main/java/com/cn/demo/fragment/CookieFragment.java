package com.cn.demo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.RrokClient;
import com.cn.demo.Api;
import com.cn.demo.R;
import com.cn.demo.bean.TestBean;
import com.cn.demo.bean.UserBean;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.request.call.ApiObsResult;
import com.cn.request.enums.CacheMode;
import com.cn.request.model.ApiResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;

public class CookieFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cookie, container, false);
	}

	@Override
	protected Call<List<TestBean>> getCall() {
		return null;
	}

	@Override
	protected ApiObsResult<List<TestBean>> apiObsResult() {
		return null;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSendEvent(SendEvent event) {
		RrokClient.sendByObservable(RrokClient.create(Api.class).login("张三", "1234567"))
			.setCacheMode(CacheMode.NONE_CACHE)
			.enqueue(new ApiObsResult<UserBean>() {
				@Override
				public void onDisposable(Disposable disposable) {

				}

				@Override
				public void onSuccess(ApiResponse<UserBean> apiResponse) {
					Log.i("CookieFragment", apiResponse.obj.toString());
				}

				@Override
				public void onFailure(Throwable throwable) {
					Log.e("CookieFragment", throwable.getMessage());
				}

				@Override
				public void onComplete() {

				}
			});
	}
}
