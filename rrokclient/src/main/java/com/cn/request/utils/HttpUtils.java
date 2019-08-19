package com.cn.request.utils;

import com.cn.request.request.base.ApiRequest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Date: 2019/7/5
 * <p>
 * Time: 3:31 PM
 * <p>
 * author: 鹿文龙
 */
public class HttpUtils {
	/**
	 * 检查对象
	 *
	 * @param object
	 * @param message
	 * @param <T>
	 * @return
	 */
	public static <T> T checkNotNull(T object, String message) {
		if (object == null) {
			throw new NullPointerException(message);
		}
		return object;
	}

	/**
	 * 根据 Request中的 地址 和 参数生成缓存Key
	 *
	 * @param apiRequest
	 * @return
	 */
	public static String getCacheKey(ApiRequest apiRequest) {
		Request request = apiRequest.getCall().request();
		HttpUrl httpUrl = request.url();
		String url = httpUrl.url().toString();

		if (HttpMethod.GET.getValue().equals(request.method())) {
			return md5Decode(url);
		} else if (HttpMethod.POST.getValue().equals(request.method())) {
			if (request.body() instanceof FormBody) {
				StringBuffer sb = new StringBuffer(url).append("?");
				FormBody body = (FormBody) request.body();
				for (int i = 0; i < body.size(); i++) {
					sb.append(body.encodedName(i))
						.append("==")
						.append(body.encodedValue(i))
						.append("&");
				}
				return md5Decode(sb.toString());
			} else if (request.body() instanceof RequestBody) {
				// TODO: 2019/7/31 参数如何获取？
				HttpTrace.i("HttpUtils", request.toString());
				return md5Decode(request.toString());
			}
		}
		return md5Decode(HttpStatus.CACHE_KEY_Unknown.getValue());
	}


	public static String md5Decode(String content) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("NoSuchAlgorithmException", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		//对生成的16字节数组进行补零操作
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
