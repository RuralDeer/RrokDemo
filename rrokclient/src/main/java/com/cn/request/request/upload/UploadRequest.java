package com.cn.request.request.upload;

import com.cn.request.call.UploadCallBack;
import com.cn.request.func.upload.UploadBuilderFunc;
import com.cn.request.func.upload.UploadFunc;
import com.cn.request.transformer.RxSchedulersTransformer;
import com.cn.request.request.base.BaseRequest;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:41 PM
 * <p>
 * author: 鹿文龙
 * <p>
 * 文件上传
 */
public class UploadRequest extends BaseRequest<UploadRequest> {

	private LinkedHashMap<String, File> fileMap;

	public UploadRequest(String url) {
		super(url);
		fileMap = new LinkedHashMap<>();
	}


	public UploadRequest file(String key, File file) {
		fileMap.put(key, file);
		return this;
	}

	public UploadRequest files(String key, List<File> files) {
		for (int i = 0; i < files.size(); i++) {
			fileMap.put(key + i, files.get(i));
		}
		return this;
	}

	public UploadRequest files(HashMap<String, File> files) {
		for (Map.Entry<String, File> entry : files.entrySet()) {
			fileMap.put(entry.getKey(), files.get(entry.getValue()));
		}
		return this;
	}

	//################ 回调 ###################
	public <T> void enqueue(final UploadCallBack<T> uploadCallBack) {
		Observable.just(fileMap)
			.map(new UploadBuilderFunc<T>(uploadCallBack))
			.concatMap(new UploadFunc<T>(uploadCallBack,this))
			.compose(RxSchedulersTransformer.<T>obsIoMain())
			.subscribe(new Observer<T>() {
				@Override
				public void onSubscribe(Disposable d) {
					uploadCallBack.onDisposable(d);
				}

				@Override
				public void onNext(T t) {
					uploadCallBack.onSuccess(t);
				}

				@Override
				public void onError(Throwable e) {
					uploadCallBack.onFailure(e);
				}

				@Override
				public void onComplete() {
					uploadCallBack.onComplete();
				}
			});
	}
}
