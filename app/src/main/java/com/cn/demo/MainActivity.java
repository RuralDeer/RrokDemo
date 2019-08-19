package com.cn.demo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.CookieFragment;
import com.cn.demo.fragment.DownloadFragment;
import com.cn.demo.fragment.GetFragment;
import com.cn.demo.fragment.PostFormBodyFragment;
import com.cn.demo.fragment.PostJsonFragment;
import com.cn.demo.fragment.UploadFragment;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.demo.fragment.base.FragmentPagerAdapter;
import com.cn.demo.fragment.base.FragmentViewPager;
import com.lzt.flowviews.global.OnFlowClickListener;
import com.lzt.flowviews.view.FlowView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

	private List<BaseFragment> fragments = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();

		OkHttpClient okHttpClient = new OkHttpClient();
	}

	private void initView() {
		FlowView flowView = findViewById(R.id.flowView);
		flowView
			.addViewCommon(getResources().getStringArray(R.array.data_type), R.layout.textview_flow, 1, false)
			.setAutoUseGrid(true)
			.setUseSelected(false)
			.setOnFlowClickListener(new OnFlowClickListener() {
				@Override
				public void onClickedView(View view, Object value, int position, int type) {
					super.onClickedView(view, value, position, type);
					EventBus.getDefault().post(new SendEvent(position));
				}
			});

		TabLayout tabLayout = findViewById(R.id.tabLayout);
		for (String s : getResources().getStringArray(R.array.table)) {
			tabLayout.addTab(tabLayout.newTab());
		}
		FragmentViewPager viewPager = findViewById(R.id.mViewPager);
		fragments.add(new GetFragment());
		fragments.add(new PostFormBodyFragment());
		fragments.add(new PostJsonFragment());
		fragments.add(new UploadFragment());
		fragments.add(new DownloadFragment());
		fragments.add(new CookieFragment());
		viewPager.setScanScroll(true);
		viewPager.setOffscreenPageLimit(fragments.size());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setupWithViewPager(viewPager);
	}

	PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getResources().getStringArray(R.array.table)[position];
		}
	};
}
