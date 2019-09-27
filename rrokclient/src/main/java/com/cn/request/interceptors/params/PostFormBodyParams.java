package com.cn.request.interceptors.params;


import com.cn.request.model.HttpParams;
import com.cn.request.request.base.IParams;

import java.util.HashMap;
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

		Map<String,Object> formBodyMap = new HashMap<>();
		FormBody oldFormBody = (FormBody) request.body();
		FormBody.Builder formBuilder = new FormBody.Builder();
		for (int i = 0; i < oldFormBody.size(); i++) {
			formBodyMap.put(oldFormBody.encodedName(i),oldFormBody.encodedValue(i));
			formBuilder.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
		}

		//公共参数
		for (Map.Entry<String, String> entry : httpParams.getParams().entrySet()) {
			//如果参数中存在的则不在追加
			if(!formBodyMap.containsKey(entry.getKey()) && null!= entry.getValue()){
				formBuilder.add(entry.getKey(), entry.getValue());
			}
		}

		return request.newBuilder()
			.method(request.method(), formBuilder.build())
			.build();
	}
}
