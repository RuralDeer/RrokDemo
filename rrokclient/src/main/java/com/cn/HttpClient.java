package com.cn;

import android.content.Context;

import com.cn.request.cookie.CookieJarImpl;
import com.cn.request.cookie.SPCookieStore;
import com.cn.request.factory.RetrofitFactory;
import com.cn.request.https.HttpsUtils;
import com.cn.request.interceptors.ParamInterceptor;
import com.cn.request.model.HttpHeaders;
import com.cn.request.model.HttpParams;
import com.cn.request.request.DownloadRequest;
import com.cn.request.request.UploadRequest;
import com.cn.request.utils.HttpUtils;

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

    private static volatile HttpClient instance;

    /**
     * 初始化
     *
     * @return
     */
    public static HttpClient init(Context context, String baseUrl) {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient(context, baseUrl);
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
    private HttpClient(Context context, String baseUrl) {
        this.context = context;
        this.mBaseUrl = baseUrl;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.addInterceptor(new ParamInterceptor());
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(context)));

        //信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    /**
     * 添加自定义OkHttpClient
     *
     * @param okHttpClient
     * @return
     */
    public HttpClient setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        return this;
    }

    /**
     * 添加自定义解析器
     *
     * @param factory
     * @return
     */
    public HttpClient setConverterFactory(Converter.Factory factory) {
        this.factory = HttpUtils.checkNotNull(factory, "factory == null");
        return this;
    }

    public HttpClient setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public HttpClient setHttpParams(HttpParams httpParams) {
        this.httpParams = httpParams;
        return this;
    }

    public HttpParams getHttpParams() {
        return httpParams;
    }

    public CookieJarImpl getCookieStore() {
        return (CookieJarImpl) okHttpClient.cookieJar();
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
        return instance.getRetrofit().create(clz);
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
    public static DownloadRequest download(String url, String destFileDir, String destFileName) {
        return new DownloadRequest(url, destFileDir, destFileName);
    }

}

