package com.cn.demo.fragment;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cn.HttpClient;
import com.cn.demo.Api;
import com.cn.demo.R;
import com.cn.demo.bean.UserBean;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.request.cookie.CookieManager;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.utils.GsonUtils;
import com.orhanobut.logger.Logger;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Cookie;

public class CookieFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cookie, container, false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendEvent(SendEvent event) {
        HttpClient.create(Api.class).getCookie()
                .as(AutoDispose.<UserBean>autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new Consumer<UserBean>() {
                    @Override
                    public void accept(UserBean userBean) throws Exception {
 //                       CookieManager.getInstance().removeAllCookie();
                        List<Cookie> cookies = CookieManager.getInstance().getAllCookie();
                        if(null!=cookies && !cookies.isEmpty()){
                            for (Cookie cookie : cookies) {
                                Logger.i("cookie_name : " + cookie.name());
                                Logger.i("cookie_value : " + cookie.value());
                            }
                        }else {
                            Log.i("CookieFragment", "cookie is null");
                        }
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("HttpClient", "throwable:" + throwable);
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }
}
