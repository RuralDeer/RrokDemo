package com.cn.request.interceptors.params;

import com.cn.request.request.base.IParams;
import com.cn.request.model.HttpParams;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 11:32 AM
 * <p>
 * author: 鹿文龙
 * <p>
 * 表单类型添加公共参数
 */
public class PostFormBodyParams extends IParams {

	public PostFormBodyParams(Request request, HttpParams httpParams) {
		super(request, httpParams);
	}

	@Override
	public Request request() {

		FormBody oldFormBody = (FormBody) request.body();
		FormBody.Builder formBuilder = new FormBody.Builder();
		for (int i = 0; i < oldFormBody.size(); i++) {
			formBuilder.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
		}
		for (Map.Entry<String, String> entry : httpParams.getParams().entrySet()) {
			formBuilder.add(entry.getKey(), entry.getValue());
		}

		return request.newBuilder()
			.method(request.method(), formBuilder.build())
			.build();
	}
}
