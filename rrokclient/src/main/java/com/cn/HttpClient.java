package com.cn;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cn.request.factory.RetrofitFactory;
import com.cn.request.https.HttpsUtils;
import com.cn.request.interceptors.CommonParamInterceptor;
import com.cn.request.mock.MockerHandler;
import com.cn.request.model.HttpHeaders;
import com.cn.request.model.HttpParams;
import com.cn.request.request.download.DownloadRequest;
import com.cn.request.request.upload.UploadRequest;
import com.cn.request.utils.HttpUtils;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 11:12 AM
 * <p>
 * author: 鹿文龙
 */
public class HttpClient {

    private final static long DEFAULT_MILLISECONDS = 6 * 1000;

    private Context context;
    private String mBaseUrl;                       //base地址
    private OkHttpClient okHttpClient;             //请求客户端
    private Converter.Factory factory;             //解析器
    private HttpHeaders httpHeaders;               //公共头
    private HttpParams httpParams;                 //公共参数
    private Retrofit retrofit;
    private boolean needMocker;

    @SuppressLint("StaticFieldLeak")
    private static volatile HttpClient instance;

    /**
     * 初始化
     *
     * @return instance
     */
    public static HttpClient init(Context context, String baseUrl, boolean needMocker) {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient(context, baseUrl, needMocker);
                }
            }
        }
        return instance;
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            throw new RuntimeException("Please initialize Your \"HttpClient\" in Application before use");
        }
        return instance;
    }

    public static Context getContext() {
        return instance.context;
    }


    /**
     * 初始化
     */
    private HttpClient(Context context, String baseUrl, boolean needMocker) {
        this.context = context;
        this.mBaseUrl = baseUrl;
        this.needMocker = needMocker;
    }

    /**
     * 设置公共参数
     * @param key
     * @param value
     */
    public HttpClient setHttpHeaders(String key,String value){
        if(null == httpParams){
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.put(key,value);
        return this;
    }

    public HttpClient setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * 设置公共参数
     * @param key
     * @param value
     */
    public HttpClient setHttpParams(String key,String value){
        if(null == httpParams){
            httpParams = new HttpParams();
        }
        httpParams.put(key,value);
        return this;
    }

    public HttpClient setHttpParams(HttpParams httpParams) {
        this.httpParams = httpParams;
        return this;
    }

    public HttpParams getHttpParams() {
        return httpParams;
    }

    public HttpClient removeParams(String key){
        if(null != httpParams){
            httpParams.remove(key);
        }
        return this;
    }

    public HttpClient clearParams(){
        if(null != httpParams){
            httpParams.clear();
        }
        return this;
    }

    /**
     * 添加自定义解析器
     *
     * @param factory
     * @return this
     */
    public HttpClient setConverterFactory(Converter.Factory factory) {
        this.factory = HttpUtils.checkNotNull(factory, "factory == null");
        return this;
    }

    public void build() {
        build(null);
    }

    /**
     * 添加自定义OkHttpClient
     *
     * @param okHttpClient
     * @return
     */
    public void build(OkHttpClient okHttpClient) {
        if (null == okHttpClient) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
            builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
            builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
            builder.addInterceptor(new CommonParamInterceptor());

            //信任所有证书,不安全有风险
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
            this.okHttpClient = builder.build();
        } else {
            this.okHttpClient = HttpUtils.checkNotNull(okHttpClient, "okHttpClient is null");
        }
    }

    /**
     * 获取 Retrofit 实例
     *
     * @return
     */
    private Retrofit getRetrofit() {
        if (null == retrofit) {
            RetrofitFactory retrofitFactory = new RetrofitFactory(okHttpClient, factory);
            retrofit = retrofitFactory.getRetrofit(mBaseUrl);
        }
        return retrofit;
    }

    /**
     * 构建请求Api
     *
     * @param clz 请求接口
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clz) {
        return create(clz, instance.needMocker);
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clz, boolean needMoker) {
        Retrofit retrofit = instance.getRetrofit();
        T api = retrofit.create(clz);
        if (needMoker) {
            return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class<?>[]{clz}, new MockerHandler(retrofit, api));
        }
        return api;
    }

    /**
     * 上传
     *
     * @param url 完整的请求地址
     */
    public static UploadRequest upload(String url) {
        return new UploadRequest(url);
    }


    /**
     * 下载
     *
     * @param url
     * @return
     */
    public static DownloadRequest download(String url) {
        return new DownloadRequest(url);
    }
}

