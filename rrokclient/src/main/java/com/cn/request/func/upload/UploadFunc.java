package com.cn.request.func.upload;

import com.cn.HttpClient;
import com.cn.request.call.UploadCallBack;
import com.cn.request.request.base.BaseApi;
import com.cn.request.request.base.BaseRequest;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;

/**
 * Date: 2019/8/13
 * <p>
 * Time: 2:38 PM
 * <p>
 * author: 鹿文龙
 */
public class UploadFunc<T> implements Function<MultipartBody.Builder, Observable<T>> {

	private UploadCallBack uploadCallBack;

	private BaseRequest request;

	public UploadFunc(UploadCallBack uploadCallBack, BaseRequest request) {
		this.uploadCallBack = uploadCallBack;
		this.request = request;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Observable<T> apply(MultipartBody.Builder builder) throws Exception {
		return HttpClient.create(BaseApi.class)
			.uploadFile(request.headers, request.url, request.params, builder.build().parts())
			.map(new JsonConvertFunc<T>(uploadCallBack.getClass()));
	}
}
