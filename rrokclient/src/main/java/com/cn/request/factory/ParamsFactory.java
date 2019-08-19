package com.cn.request.factory;

import com.cn.request.request.base.IParams;
import com.cn.request.interceptors.params.PostFormBodyParams;
import com.cn.request.interceptors.params.GetParams;
import com.cn.request.interceptors.params.PostMultipartBodyParams;
import com.cn.request.interceptors.params.UndefinedParams;
import com.cn.request.model.HttpParams;
import com.cn.request.utils.HttpMethod;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Date: 2019/7/12
 * <p>
 * Time: 11:34 AM
 * <p>
 * author: 鹿文龙
 */
public class ParamsFactory {

	public static IParams params(Request request, HttpParams httpParams) {
		if (HttpMethod.GET.getValue().equals(request.method())) {
			return new GetParams(request, httpParams);
		} else if (HttpMethod.POST.getValue().equals(request.method())) {
			if (request.body() instanceof FormBody) {
				return new PostFormBodyParams(request, httpParams);
			} else if (request.body() instanceof MultipartBody) {
				return new PostMultipartBodyParams(request, httpParams);
			}
		}
		return new UndefinedParams(request, httpParams);
	}
}
