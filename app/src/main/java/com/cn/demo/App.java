package com.cn.demo;

import android.app.Application;

import com.cn.HttpClient;
import com.cn.demo.gson.CustGsonConverterFactory;
import com.cn.request.cookie.impl.ImplCookieJar;
import com.cn.request.cookie.model.SharedCookieStore;
import com.cn.request.https.HttpsUtils;
import com.cn.request.interceptors.CommonParamInterceptor;
import com.cn.request.model.HttpHeaders;
import com.cn.request.model.HttpParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("RrokDemo_Log")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("key-1", "header-1");
        httpHeaders.put("key-2", "header-2");
        httpHeaders.put("key-3", "header-3");

        HttpParams httpParams = new HttpParams();
        httpParams.put("key-1", "");
        httpParams.put("key-2", "");
        httpParams.put("key-3", "param-3");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(6 * 1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(6 * 1000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(6 * 1000, TimeUnit.MILLISECONDS);

        //1.不初始化SpName、CookieNamePrefix, 此时将使用默认的参数
        SharedCookieStore cookieStore = new SharedCookieStore(this);

        //2.初始化SpName、CookieNamePrefix,将使用你自己的参数
        //SharedCookieStore cookieStore = new SharedCookieStore(this, "SpName", "cookie_");

        builder.cookieJar(new ImplCookieJar(cookieStore));
        //1.如果需要添加 公共头、公共参数 则必须添加次拦截器，
        //2.当然你也可以自定义 , 如果使用自定义请注意  setHttpHeaders和setHttpParams 将不生效
        builder.addInterceptor(new CommonParamInterceptor());

        //方法1：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

//        //方法2：自定义信任规则，校验服务端证书,此处 SafeTrustManager、SafeHostnameVerifier 不可直接使用，需根据情况定义规则
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        builder.hostnameVerifier(new SafeHostnameVerifier());

//        //方法 3：使用预埋证书，校验服务端证书（自签名证书）SafeHostnameVerifier 需根据情况定义
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.cer"));
//        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        builder.hostnameVerifier(new SafeHostnameVerifier());

//        //方法4：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书），SafeHostnameVerifier 需根据情况定义
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
//        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        HttpClient
                .init(this, Api.BASE_URL, true)
                .setHttpHeaders(httpHeaders)
                .setHttpParams(httpParams)
                .setConverterFactory(CustGsonConverterFactory.create())
                .build(builder.build());
    }
}
