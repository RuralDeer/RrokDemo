package com.cn.request.factory;

import com.cn.request.utils.HttpUtils;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 4:04 PM
 * <p>
 * author: 鹿文龙
 */
public class RetrofitFactory {

	private OkHttpClient okHttpClient;

	private Converter.Factory factory;

	public RetrofitFactory(OkHttpClient okHttpClient, Converter.Factory factory) {
		this.okHttpClient = okHttpClient;
		this.factory = factory;
	}

	public Retrofit getRetrofit(String baseUrl) {
		baseUrl = HttpUtils.checkNotNull(baseUrl, "Please initialize Your \"BaseUrl\" in Application before use");
		return getRetrofit(baseUrl, okHttpClient, factory);
	}


	public Retrofit getRetrofit(String baseUrl, OkHttpClient okHttpClient, Converter.Factory factory) {
		return new Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(okHttpClient)
			.addConverterFactory(null == factory ? GsonConverterFactory.create() : factory)
			.build();
	}

}
