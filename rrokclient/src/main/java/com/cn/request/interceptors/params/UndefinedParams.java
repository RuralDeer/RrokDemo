package com.cn.request.interceptors.params;

import com.cn.request.request.base.IParams;
import com.cn.request.model.HttpParams;

import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 11:38 AM
 * <p>
 * author: 鹿文龙
 * <p>
 * 未定义的类型
 */
public class UndefinedParams extends IParams {

	public UndefinedParams(Request request, HttpParams httpParams) {
		super(request, httpParams);
	}

	@Override
	public Request request() {
		return request;
	}
}
