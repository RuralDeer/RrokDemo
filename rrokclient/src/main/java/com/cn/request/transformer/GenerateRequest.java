package com.cn.request.transformer;

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

    private static final String CLASS_NAME_1 = "retrofit2.adapter.rxjava2.CallEnqueueObservable";
    private static final String CLASS_NAME_2 = "retrofit2.adapter.rxjava2.CallExecuteObservable";
    private static final String CLASS_NAME_3 = "retrofit2.OkHttpCall";
    private static final String CLASS_NAME_4 = "retrofit2.HttpServiceMethod";
    private static final String CLASS_NAME_5 = "retrofit2.RequestFactory";
    private static final String CLASS_NAME_7 = "retrofit2.ServiceMethod";

    private static final String FIELD_UPSTREAM = "upstream";
    private static final String FIELD_ORIGINALCALL = "originalCall";
    private static final String FIELD_ARGS = "args";
    private static final String FIELD_SERVICEMETHOD = "serviceMethod";
    private static final String FIELD_REQUESTFACTORY = "requestFactory";

    private static final String METHOD_CREATE = "create";
    private static final String METHOD_TO_REQUEST = "toRequest";

    public <T> Request generateRequest(Flowable<T> flowable) {
        Request request = null;
        try {
            Field upstream = flowable.getClass().getDeclaredField(FIELD_UPSTREAM);
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
            Field upstream = observable.getClass().getDeclaredField(FIELD_UPSTREAM);
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
        if (CLASS_NAME_1.equals(obj.getClass().getName())) {
            cls = Class.forName(CLASS_NAME_1);

        } else if (CLASS_NAME_2.equals(obj.getClass().getName())) {
            cls = Class.forName(CLASS_NAME_2);
        }
        if (cls == null) {
            return null;
        }
        Field foriginalCall = cls.getDeclaredField(FIELD_ORIGINALCALL);
        foriginalCall.setAccessible(true);
        Object okhttpCallObj = foriginalCall.get(obj);

        Class clsOkhttpCall = Class.forName(CLASS_NAME_3);
        Field fdArgs = clsOkhttpCall.getDeclaredField(FIELD_ARGS);


        fdArgs.setAccessible(true);
        args = (Object[]) fdArgs.get(okhttpCallObj);

        Field fdserviceMethod = null;
        try {
            fdserviceMethod = clsOkhttpCall.getDeclaredField(FIELD_SERVICEMETHOD);
        } catch (Exception e) {

        }
        if (fdserviceMethod == null) {
            Field filedRequestFactory = clsOkhttpCall.getDeclaredField(FIELD_REQUESTFACTORY);
            filedRequestFactory.setAccessible(true);
            serviceMethodObj = filedRequestFactory.get(okhttpCallObj);

        } else {
            fdserviceMethod.setAccessible(true);
            serviceMethodObj = fdserviceMethod.get(okhttpCallObj);
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

        if (CLASS_NAME_4.equals(objName)) {
            Class clsHttpServiceMethod = Class.forName(CLASS_NAME_4);
            Field fieldRequestFactory = clsHttpServiceMethod.getDeclaredField(FIELD_REQUESTFACTORY);
            fieldRequestFactory.setAccessible(true);
            serviceMethod = fieldRequestFactory.get(serviceMethod);
            objName = serviceMethod.getClass().getName();
        }

        if (CLASS_NAME_5.equals(objName)) {
            Class clsServiceMethod = Class.forName(CLASS_NAME_5);
            toRequestMethod = clsServiceMethod.getDeclaredMethod(METHOD_CREATE, Object[].class);
        } else {
            Class clsServiceMethod = Class.forName(CLASS_NAME_7);
            toRequestMethod = clsServiceMethod.getDeclaredMethod(METHOD_TO_REQUEST, Object[].class);
        }
        toRequestMethod.setAccessible(true);
        try {
            return (Request) toRequestMethod.invoke(serviceMethod, new Object[]{args});
        } catch (Exception e) {
        }
        return null;
    }
}
