package com.cn.demo.transformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.Flowable;
import io.reactivex.Observable;
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

	private static final String CLASS_NAME3 = "retrofit2.adapter.rxjava2.CallEnqueueObservable";
	private static final String CLASS_NAME4 = "retrofit2.adapter.rxjava2.CallExecuteObservable";

	public <T> Request generateRequest(Flowable<T> flowable) {
		Request request = null;
		try {
			Field upstream = flowable.getClass().getDeclaredField("upstream");
			upstream.setAccessible(true);
			Object obj = upstream.get(flowable);
			request = objectGenerateRequest(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	public <T> Request generateRequest(Observable<T> observable) {
		Request request = null;
		try {
			Field upstream = observable.getClass().getDeclaredField("upstream");
			upstream.setAccessible(true);
			Object obj = upstream.get(observable);
			request = objectGenerateRequest(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * Object 转 Request
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private Request objectGenerateRequest(Object obj) throws Exception {
		Object serviceMethodObj = null;
		Object[] args;
		Class cls = null;
		if (obj.getClass().getName().equals(CLASS_NAME3)) {
			cls = Class.forName(CLASS_NAME3);

		} else if (obj.getClass().getName().equals(CLASS_NAME4)) {
			cls = Class.forName(CLASS_NAME4);
		}
		if (cls == null) {
			return null;
		}
		Field foriginalCall = cls.getDeclaredField("originalCall");
		foriginalCall.setAccessible(true);
		Object OkhttpCallObj = foriginalCall.get(obj);

		Class clsOkhttpCall = Class.forName("retrofit2.OkHttpCall");
		Field fdArgs = clsOkhttpCall.getDeclaredField("args");


		fdArgs.setAccessible(true);
		args = (Object[]) fdArgs.get(OkhttpCallObj);

		Field fdserviceMethod = null;
		try {
			fdserviceMethod = clsOkhttpCall.getDeclaredField("serviceMethod");
		} catch (Exception e) {

		}
		if (fdserviceMethod == null) {
			Field filedRequestFactory = clsOkhttpCall.getDeclaredField("requestFactory");
			filedRequestFactory.setAccessible(true);
			serviceMethodObj = filedRequestFactory.get(OkhttpCallObj);

		} else {
			fdserviceMethod.setAccessible(true);
			serviceMethodObj = fdserviceMethod.get(OkhttpCallObj);
		}

		if (serviceMethodObj != null) {
			return buildRequest(serviceMethodObj, args);
		}
		return null;
	}

	/**
	 * 生成 Request
	 *
	 * @param serviceMethod
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private Request buildRequest(Object serviceMethod, Object[] args) throws Exception {
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
