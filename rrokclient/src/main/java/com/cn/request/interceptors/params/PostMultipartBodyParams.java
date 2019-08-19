package com.cn.request.interceptors.params;

import com.cn.request.request.base.IParams;
import com.cn.request.model.HttpParams;

import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 1:39 PM
 * <p>
 * author: 鹿文龙
 */
public class PostMultipartBodyParams extends IParams {

	public PostMultipartBodyParams(Request request, HttpParams httpParams) {
		super(request, httpParams);
	}

	@Override
	public Request request() {
		return request;
	}
}
