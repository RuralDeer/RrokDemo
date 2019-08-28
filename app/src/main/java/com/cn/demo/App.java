package com.cn.demo;

import android.app.Application;

import com.cn.HttpClient;
import com.cn.demo.gson.CustGsonConverterFactory;
import com.cn.request.model.HttpHeaders;
import com.cn.request.model.HttpParams;

/**
 * Date: 2019/7/10
 * <p>
 * Time: 3:49 PM
 * <p>
 * author: 鹿文龙
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("key-1", "header-1");
        httpHeaders.put("key-2", "header-2");
        httpHeaders.put("key-3", "header-3");

        HttpParams httpParams = new HttpParams();
        httpParams.put("key-1", "param-1");
        httpParams.put("key-2", "param-2");
        httpParams.put("key-3", "param-3");

        HttpClient
                .init(this, Api.BASE_URL, true)
                .setHttpHeaders(httpHeaders)
                .setHttpParams(httpParams)
                .setConverterFactory(CustGsonConverterFactory.create());
    }
}
