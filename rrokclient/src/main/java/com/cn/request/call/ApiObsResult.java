package com.cn.request.call;

import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/7/19
 * <p>
 * Time: 11:35 AM
 * <p>
 * author: 鹿文龙
 */
public abstract class ApiObsResult <T> extends ApiBaseResult<T>{

	/**
	 * Observer模式
	 *
	 * @param disposable
	 */
	public abstract void onDisposable(Disposable disposable);


}
