package com.cn.request.mock;

import android.util.Log;

import com.cn.request.mock.anno.Mock;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Date: 2019-08-28
 * <p>
 * Time: 17:16
 * <p>
 * author: 鹿文龙
 */
public final class MockerHandler<T> implements InvocationHandler {

    private Retrofit retrofit;
    private T api;

    public MockerHandler(Retrofit retrofit, T api) {
        this.retrofit = retrofit;
        this.api = api;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        boolean isMockExist = method.isAnnotationPresent(Mock.class);

        //如果注解不存在 正常走流程
        if (!isMockExist) {
            return invoke(method, args);
        }

        Mock mock = method.getAnnotation(Mock.class);

        //如果mock 设置为false 正常返回
        if (!mock.enable()) {
            return invoke(method, args);
        }

        String baseUrl = mock.baseurl().trim();
        if (null != baseUrl && baseUrl.length() != 0 && baseUrl.startsWith("http")) {
            setNewBaseUrl(method, baseUrl);
            return invoke(method, args);
        } else {
            String value = mock.value().trim();
            //如果http开头 替换成url 正常请求
            if (value.startsWith("http")) {
                preLoadServiceMethod(method, value);
                return invoke(method, args);
            } else if (value.endsWith(".json")) {
                String mockResponseJson = readAssets(value);
                Object responseObj = retrofit.nextResponseBodyConverter(null, getReturnTye(method), method.getDeclaredAnnotations()).convert(ResponseBody.create(MediaType.parse("application/json"), mockResponseJson));
                return (retrofit.nextCallAdapter(null, method.getGenericReturnType(), method.getAnnotations())).adapt(new MockerCall(responseObj));
            } else { //其他情况正常请求
                return invoke(method, args);
            }
        }
    }

    private Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (args == null) {
            return method.invoke(api);
        } else {
            return method.invoke(api, args);
        }
    }

    private Type getReturnTye(Method method) {
        return ((ParameterizedType) (method.getGenericReturnType())).getActualTypeArguments()[0];
    }

    //修改url地址
    private void preLoadServiceMethod(Method method, String relativeUrl) {
        try {
            Method loadServiceMethod = retrofit.getClass().getDeclaredMethod("loadServiceMethod", Method.class);
            loadServiceMethod.setAccessible(true);
            //获得serviceMethod的值
            Object serviceMethod = loadServiceMethod.invoke(retrofit, method);
            //反射修改实体类中的值
            fixRetrofit240(serviceMethod, relativeUrl);
            fixRetrofit250(serviceMethod, relativeUrl);
        } catch (Exception e) {

        }
    }

    //针对retrofit 2.4.0版本做适配
    private void fixRetrofit240(Object serviceMethod, String relativeUrl) {
        try {
            Field relativeUrlField = serviceMethod.getClass().getDeclaredField("relativeUrl");
            relativeUrlField.setAccessible(true);
            relativeUrlField.set(serviceMethod, relativeUrl);
        } catch (Exception e) {

        }
    }

    //针对retrofit 2.5.0版本做适配
    private void fixRetrofit250(Object serviceMethod, String relativeUrl) {
        try {
            Field requestFactoryField = serviceMethod.getClass().getDeclaredField("requestFactory");
            requestFactoryField.setAccessible(true);
            Object requestFactory = requestFactoryField.get(serviceMethod);

            Field httpUrlField = requestFactory.getClass().getDeclaredField("baseUrl");
            httpUrlField.setAccessible(true);

            Field baseUrlField = httpUrlField.getDeclaringClass().getDeclaredField("url");
            baseUrlField.setAccessible(true);
            baseUrlField.set(httpUrlField, "http://test");


            Field relativeUrlField = requestFactory.getClass().getDeclaredField("relativeUrl");
            relativeUrlField.setAccessible(true);
            relativeUrlField.set(requestFactory, relativeUrl);
        } catch (Exception e) {

        }
    }

    //读取文件
    private String readAssets(String fileName) {
        try {
            InputStream is = this.getClass().getResourceAsStream("/assets/" + fileName.trim());
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("utf-8"));
        } catch (Exception e) {

        }
        return "读取错误，请检查文件名";
    }

    /**
     * 替换BaseUrl
     *
     * @param newBaseUrl
     */
    private void setNewBaseUrl(Method method, String newBaseUrl) {
        try {

            Method loadServiceMethod = retrofit.getClass().getDeclaredMethod("loadServiceMethod", Method.class);
            loadServiceMethod.setAccessible(true);
            //获得serviceMethod的值
            Object serviceMethod = loadServiceMethod.invoke(retrofit, method);

            Field requestFactoryField = serviceMethod.getClass().getDeclaredField("requestFactory");
            requestFactoryField.setAccessible(true);
            Object requestFactory = requestFactoryField.get(serviceMethod);

            Field httpUrlField = requestFactory.getClass().getDeclaredField("baseUrl");
            httpUrlField.setAccessible(true);

            Log.d("MockerHandler", httpUrlField.getDeclaringClass().getName());

            Field [] fields = httpUrlField.getDeclaringClass().getDeclaredFields();
            for(int i = 0;i<fields.length;i++){
                Log.i("MockerHandler", fields[i].getName());
            }

            Field baseUrlField = httpUrlField.getDeclaringClass().getDeclaredField("url");
            baseUrlField.setAccessible(true);
            baseUrlField.set(httpUrlField, newBaseUrl);
        } catch (Exception e) {
            Log.e("MockerHandler", e.getMessage());
        }
    }
}