package com.cn.request.func.file;

import com.cn.RrokClient;
import com.cn.request.call.ApiBaseFileResult;
import com.cn.request.request.base.Api;
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

	private ApiBaseFileResult apiBaseFileResult;

	private BaseRequest request;

	public UploadFunc(ApiBaseFileResult apiBaseFileResult, BaseRequest request) {
		this.apiBaseFileResult = apiBaseFileResult;
		this.request = request;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Observable<T> apply(MultipartBody.Builder builder) throws Exception {
		return RrokClient.create(Api.class)
			.uploadFile(request.headers, request.url, request.params, builder.build().parts())
			.map(new JsonConvertFunc<T>(apiBaseFileResult.getClass()));
	}
}
