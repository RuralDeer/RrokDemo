package com.cn.request.request.base;

import android.arch.lifecycle.LifecycleOwner;

import com.cn.request.enums.CacheMode;

import retrofit2.Call;

/**
 * Date: 2019/7/16
 * <p>
 * Time: 4:37 PM
 * <p>
 * author: 鹿文龙
 *
 * @param <T>      数据类型
 * @param <R>      request类型
 * @param <Result> 返回类型
 */

public abstract class ApiRequest<T, R extends ApiRequest, Result> {

	/**
	 * 缓存模式
	 */
	protected CacheMode cacheMode = CacheMode.NONE_CACHE;

	/**
	 * 是否自动管理Rx
	 */
	protected LifecycleOwner lifecycleOwner;

	/**
	 * 请求call
	 */
	protected Call<T> call;

	public ApiRequest(Call<T> call) {
		this.call = call;
	}

	public abstract R setCacheMode(CacheMode cacheMode);

	public abstract R setLifecycleOwner(LifecycleOwner lifecycleOwner);

	public abstract void enqueue(Result result);

	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public LifecycleOwner getLifecycleOwner() {
		return lifecycleOwner;
	}

	public Call<T> getCall() {
		return call;
	}
}
