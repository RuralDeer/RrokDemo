package com.cn.demo.fragment.base;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cn.demo.R;
import com.cn.demo.adapter.DataAdapter;
import com.cn.demo.bean.TestBean;
import com.cn.RrokClient;
import com.cn.request.call.ApiObsResult;
import com.cn.request.enums.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by luwenlong on 2017/8/29.
 */

public abstract class BaseFragment extends Fragment {

	protected final static int page = 1;

	protected final static int offeset = 20;

	protected TextView mErrorTv;
	protected DataAdapter mNetAdapter;
	protected DataAdapter mCacheAdapter;

	protected abstract retrofit2.Call<List<TestBean>> getCall();

	protected abstract ApiObsResult<List<TestBean>> apiObsResult();


	protected void init(View view) {
		mErrorTv = view.findViewById(R.id.errorTv);
		RecyclerView mNetRecyclerView = view.findViewById(R.id.netRecyclerView);
		RecyclerView mCacheRecyclerView = view.findViewById(R.id.cacheRecyclerView);

		mNetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mCacheRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mNetAdapter = new DataAdapter();
		mCacheAdapter = new DataAdapter();

		mNetRecyclerView.setAdapter(mNetAdapter);
		mCacheRecyclerView.setAdapter(mCacheAdapter);
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			EventBus.getDefault().register(this);
		} else {
			EventBus.getDefault().unregister(this);
		}
	}

	/**
	 * 无缓存
	 */
	protected void req0() {
		RrokClient.sendByObservable(getCall())
			.setCacheMode(CacheMode.NONE_CACHE)
			.enqueue(apiObsResult());
	}

	/**
	 * 只要缓存
	 */
	protected void req1() {
		RrokClient.sendByObservable(getCall())
			.setCacheMode(CacheMode.ONLY_USR_CACHE)
			.enqueue(apiObsResult());
	}

	/**
	 * 缓存+网络
	 */
	protected void req2() {
		RrokClient.sendByObservable(getCall())
			.setCacheMode(CacheMode.USR_CACHE_NETWORK)
			.enqueue(apiObsResult());
	}

	/**
	 * 网络错误+缓存
	 */
	protected void req3() {
		RrokClient.sendByObservable(getCall())
			.setCacheMode(CacheMode.NETWORK_ERROR_USE_CACHE)
			.enqueue(apiObsResult());
	}

	/**
	 * 缓存错误+网络
	 */
	protected void req4() {
		RrokClient.sendByObservable(getCall())
			.setCacheMode(CacheMode.NOT_CACHE_USE_NETWORK)
			.enqueue(apiObsResult());
	}

}
