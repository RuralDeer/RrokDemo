package com.cn.demo.transformer;

import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Date: 2019-08-19
 * <p>
 * Time: 17:17
 * <p>
 * author: 鹿文龙
 * <p>
 * 这里使用了 https://github.com/yale8848/RetrofitCache 中的源码
 */
public class CacheTransformer {

	private static final String CLASS_NAME1 = "retrofit2.adapter.rxjava2.BodyObservable";
	private static final String CLASS_NAME2 = "retrofit2.adapter.rxjava2.ResultObservable";
	private static final String CLASS_NAME3 = "retrofit2.adapter.rxjava2.CallEnqueueObservable";
	private static final String CLASS_NAME4 = "retrofit2.adapter.rxjava2.CallExecuteObservable";

	public static <T> ObservableTransformer<T, T> emptyTransformer() {
		return new ObservableTransformer<T, T>() {
			@Override
			public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
				String name = upstream.getClass().getName();
				if (name.equals(CLASS_NAME1) || name.equals(CLASS_NAME2)) {
					observable(upstream);
				}
				return upstream;
			}
		};
	}

	private static <T> void observable(Observable<T> observable) {

		try {
			Object serviceMethodObj = null;
			Object[] args;
			Field upstream = observable.getClass().getDeclaredField("upstream");
			upstream.setAccessible(true);

			Object obj = upstream.get(observable);

			Class cls = null;
			if (obj.getClass().getName().equals(CLASS_NAME3)) {
				cls = Class.forName(CLASS_NAME3);

			} else if (obj.getClass().getName().equals(CLASS_NAME4)) {
				cls = Class.forName(CLASS_NAME4);
			}
			if (cls == null) {
				return;
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
				GenerateRequest generateRequest = new GenerateRequest();
				generateRequest.buildRequest(serviceMethodObj, args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
