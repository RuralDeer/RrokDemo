package com.cn.demo.fragment;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.demo.Api;
import com.cn.demo.R;
import com.cn.demo.bean.TestBean;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.HttpClient;
import com.cn.request.enums.CacheMode;
import com.cn.request.enums.DataSource;
import com.cn.request.model.ApiResponse;
import com.cn.request.transformer.RxCacheTransformer;
import com.cn.request.transformer.RxResponseCacheTransformer;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.utils.GsonUtils;
import com.orhanobut.logger.Logger;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class PostJsonFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_json, container, false);
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
                request(CacheMode.NONE_CACHE);
                break;
            case 1:
                request(CacheMode.READ_CACHE_THEN_CACHE_NET_REQUEST);
                break;
            case 2:
                request(CacheMode.READ_CACHE_THEN_NET_REQUEST);
                break;
            case 3:
                request(CacheMode.NET_REQUEST_ERROR_THEN_READ_CACHE);
                break;
            case 4:
                request(CacheMode.READ_CACHE_ERROR_THEN_NET_REQUEST);
                break;
        }
    }

    private void request(CacheMode cacheMode) {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", page);
        map.put("offeset", offeset);

        HttpClient.create(Api.class).postBody(GsonUtils.formatJson(map))
                .compose(RxResponseCacheTransformer.<List<TestBean>>obsTransformer(cacheMode))
                .compose(RxSchedulersTransformer.<ApiResponse<List<TestBean>>>obsIoMain())
                .as(AutoDispose.<ApiResponse<List<TestBean>>>autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new Consumer<ApiResponse<List<TestBean>>>() {
                    @Override
                    public void accept(ApiResponse<List<TestBean>> listApiResponse) throws Exception {
                        if (listApiResponse.dataSource == DataSource.CACHE) {
                            mCacheAdapter.addData(listApiResponse.data);
                            Logger.json(GsonUtils.formatJson(listApiResponse.data));
                        } else {
                            mNetAdapter.addData(listApiResponse.data);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mErrorTv.setText(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mErrorTv.setText("请求完毕");
                    }
                });
    }

}
