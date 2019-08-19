package com.cn.demo.transformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import okhttp3.Request;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 17:20
 * <p>
 * author: 鹿文龙
 * <p>
 * 生成 Request
 * <p>
 * 这里使用了 https://github.com/yale8848/RetrofitCache 中部 RetrofitCache.java 分源码
 */
public class GenerateRequest {

	public Request buildRequest(Object serviceMethod, Object[] args) throws Exception {
		String objName = serviceMethod.getClass().getName();
		Method toRequestMethod = null;

		if (objName.equals("retrofit2.HttpServiceMethod")) {
			Class clsHttpServiceMethod = Class.forName("retrofit2.HttpServiceMethod");
			Field fieldRequestFactory = clsHttpServiceMethod.getDeclaredField("requestFactory");
			fieldRequestFactory.setAccessible(true);
			serviceMethod = fieldRequestFactory.get(serviceMethod);
			objName = serviceMethod.getClass().getName();
		}

		if (objName.equals("retrofit2.RequestFactory")) {
			Class clsServiceMethod = Class.forName("retrofit2.RequestFactory");
			toRequestMethod = clsServiceMethod.getDeclaredMethod("create", Object[].class);
		} else {
			Class clsServiceMethod = Class.forName("retrofit2.ServiceMethod");
			toRequestMethod = clsServiceMethod.getDeclaredMethod("toRequest", Object[].class);
		}
		toRequestMethod.setAccessible(true);
		try {
			return (Request) toRequestMethod.invoke(serviceMethod, new Object[]{args});
		} catch (Exception e) {
		}
		return null;
	}
}
