package com.cn.request.request.base;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:16 PM
 * <p>
 * author: 鹿文龙
 */
public abstract class BaseRequest<R extends BaseRequest> {

	public String url;

	public HashMap<String, String> headers;

	public HashMap<String, String> params;

	public BaseRequest(String url) {
		this.url = url;
		headers = new LinkedHashMap<>();
		params = new LinkedHashMap<>();
	}

	//################ header ###################

	@SuppressWarnings("unchecked")
	public R header(String key, String value) {
		headers.put(key, value);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R header(String key, int value) {
		headers.put(key, String.valueOf(value));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R header(String key, double value) {
		headers.put(key, String.valueOf(value));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R haders(HashMap<String, String> headers) {
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			this.headers.put(entry.getKey(), entry.getValue());
		}
		return (R) this;
	}

	//################ 参数 ###################

	@SuppressWarnings("unchecked")
	public R param(String key, String value) {
		params.put(key, value);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R param(String key, int value) {
		params.put(key, String.valueOf(value));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R param(String key, double value) {
		params.put(key, String.valueOf(value));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R params(HashMap<String, String> params) {
		for (Map.Entry<String, String> entry : params.entrySet()) {
			this.params.put(entry.getKey(), entry.getValue());
		}
		return (R) this;
	}
}
