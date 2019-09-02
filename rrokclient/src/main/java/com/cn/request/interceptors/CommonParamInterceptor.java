package com.cn.request.interceptors;

import android.text.TextUtils;

import com.cn.HttpClient;
import com.cn.request.factory.ParamsFactory;
import com.cn.request.request.base.IParams;
import com.cn.request.model.HttpHeaders;
import com.cn.request.model.HttpParams;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date: 2019/7/9
 * <p>
 * Time: 2:57 PM
 * <p>
 * author: 鹿文龙
 */
public class CommonParamInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request original = chain.request();
		Request request = addHeaders(original);
		Request addParams = addParams(request);
		return chain.proceed(addParams);
	}

	/**
	 * 添加统一通用header
	 * <p>
	 * 不会覆盖前面的header
	 *
	 * @return
	 */
	private Request addHeaders(Request request) {
		HttpHeaders httpHeaders = HttpClient.getInstance().getHttpHeaders();
		if (null == httpHeaders || null == httpHeaders.getHeaders()) {return request;}
		Request.Builder builder = request.newBuilder();
		for (Map.Entry<String, String> entry : httpHeaders.getHeaders().entrySet()) {
			if(TextUtils.isEmpty(request.headers().get(entry.getKey()))){
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return builder.build();

	}

	/**
	 * 添加公共参数
	 *
	 * @param request
	 * @return
	 */
	private Request addParams(Request request) {
		HttpParams httpParams = HttpClient.getInstance().getHttpParams();
		if (null == httpParams || null == httpParams.getParams()) {return request;}
		IParams params = ParamsFactory.params(request, httpParams);
		return params.request();
	}
}