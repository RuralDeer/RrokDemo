package com.cn.request.request;

import com.cn.RrokClient;
import com.cn.request.request.base.BaseApi;
import com.cn.request.request.base.BaseRequest;
import com.cn.request.call.ApiBaseFileResult;
import com.cn.request.func.file.FileConvertFunc;
import com.cn.request.factory.RxScheduler;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:52 PM
 * <p>
 * author: 鹿文龙
 */
public class DownloadRequest extends BaseRequest<DownloadRequest> {

	private String destFileDir;
	private String destFileName;

	public DownloadRequest(String url, String destFileDir, String destFileName) {
		super(url);
		this.destFileDir = destFileDir;
		this.destFileName = destFileName;
	}

	public void enqueue(final ApiBaseFileResult<File> apiBaseFileResult) {
		RrokClient.create(BaseApi.class)
			.downLoadFile(headers, url, params)
			.map(new FileConvertFunc(destFileDir, destFileName, apiBaseFileResult))
			.compose(RxScheduler.<File>Obs_io_main())
			.subscribe(new Observer<File>() {
				@Override
				public void onSubscribe(Disposable d) {
					apiBaseFileResult.onDisposable(d);
				}

				@Override
				public void onNext(File file) {
					apiBaseFileResult.onSuccess(file);
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
