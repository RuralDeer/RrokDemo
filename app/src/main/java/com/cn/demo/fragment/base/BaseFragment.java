package com.cn.demo.fragment.base;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cn.demo.R;
import com.cn.demo.adapter.DataAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by luwenlong on 2017/8/29.
 */

public abstract class BaseFragment extends Fragment {

	protected final static int page = 1;

	protected final static int offeset = 20;

	protected final static int retryNum = 3;
	protected final static int retryDelay = 3000;

	protected TextView mErrorTv;
	protected DataAdapter mNetAdapter;
	protected DataAdapter mCacheAdapter;


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

}
