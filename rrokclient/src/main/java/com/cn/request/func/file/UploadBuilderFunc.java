package com.cn.request.func.file;

import com.cn.request.call.ApiBaseFileResult;
import com.cn.request.utils.UploadRequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Date: 2019/8/13
 * <p>
 * Time: 2:36 PM
 * <p>
 * author: 鹿文龙
 */
public class UploadBuilderFunc<T> implements Function<HashMap<String, File>, MultipartBody.Builder> {

	private ApiBaseFileResult apiBaseFileResult;

	public UploadBuilderFunc(ApiBaseFileResult<T> apiBaseFileResult) {
		this.apiBaseFileResult = apiBaseFileResult;
	}

	@Override
	public MultipartBody.Builder apply(HashMap<String, File> fileMap) throws Exception {
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		for (Map.Entry<String, File> entry : fileMap.entrySet()) {
			RequestBody body = new UploadRequestBody<T>(entry.getValue(), apiBaseFileResult);
			builder.addFormDataPart(entry.getKey(), entry.getValue().getName(), body);
		}
		return builder;
	}
}
