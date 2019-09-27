package com.cn.request.interceptors.params;

import android.text.TextUtils;

import com.cn.request.model.HttpParams;
import com.cn.request.request.base.IParams;

import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 11:28 AM
 * <p>
 * author: 鹿文龙
 * <p>
 * get请求添加公共参数
 */
public class GetParams extends IParams {

	public GetParams(Request request, HttpParams httpParams) {
		super(request, httpParams);
	}

	@Override
	public Request request() {
		HttpUrl httpUrl = request.url();
		HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
		for (Map.Entry<String, String> entry : httpParams.getParams().entrySet()) {
			//如果参数中存在的则不在追加
			if(TextUtils.isEmpty(httpUrl.queryParameter(entry.getKey())) && null!= entry.getValue()){
				urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
			}
		}
		return request.newBuilder()
			.method(request.method(), request.body())
			.url(urlBuilder.build())
			.build();
	}
}
