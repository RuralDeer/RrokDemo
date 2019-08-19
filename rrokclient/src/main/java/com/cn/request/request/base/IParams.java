package com.cn.request.request.base;

import com.cn.request.model.HttpParams;

import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 11:00 AM
 * <p>
 * author: 鹿文龙
 */
public abstract class IParams {

	protected Request request;

	protected HttpParams httpParams;

	public IParams(Request request, HttpParams httpParams) {
		this.request = request;
		this.httpParams = httpParams;
	}

	/**
	 * 返回 ObsApiRequest
	 *
	 * @return
	 */
	public abstract Request request();
}
