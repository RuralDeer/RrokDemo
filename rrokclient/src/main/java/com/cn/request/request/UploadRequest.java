package com.cn.request.request;

import com.cn.request.call.ApiBaseFileResult;
import com.cn.request.factory.RxScheduler;
import com.cn.request.func.file.UploadBuilderFunc;
import com.cn.request.func.file.UploadFunc;
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
	public <T> void enqueue(final ApiBaseFileResult<T> apiBaseFileResult) {
		Observable.just(fileMap)
			.map(new UploadBuilderFunc<T>(apiBaseFileResult))
			.concatMap(new UploadFunc<T>(apiBaseFileResult,this))
			.compose(RxScheduler.<T>Obs_io_main())
			.subscribe(new Observer<T>() {
				@Override
				public void onSubscribe(Disposable d) {
					apiBaseFileResult.onDisposable(d);
				}

				@Override
				public void onNext(T t) {
					apiBaseFileResult.onSuccess(t);
				}

				@Override
				public void onError(Throwable e) {
					apiBaseFileResult.onFailure(e);
				}

				@Override
				public void onComplete() {
					apiBaseFileResult.onComplete();
				}
			});
	}
}
