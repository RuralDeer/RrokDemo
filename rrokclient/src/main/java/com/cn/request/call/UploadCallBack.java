package com.cn.request.call;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:44 PM
 * <p>
 * author: 鹿文龙
 */
public abstract class UploadCallBack<T> {

	public abstract void onDisposable(Disposable disposable);

	public abstract void onProgress(File file, int progress, long total);

	public abstract void onSuccess(T t);

	public abstract void onFailure(Throwable e);

	public abstract void onComplete();
}
