package com.cn.request.func.upload;

import com.cn.request.utils.GsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Date: 2019/8/6
 * <p>
 * Time: 2:43 PM
 * <p>
 * author: 鹿文龙
 */
public class JsonConvertFunc<T> implements Function<ResponseBody, T> {

	private Class clazz;

	public JsonConvertFunc(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public T apply(ResponseBody responseBody) throws Exception {
		Type genType = clazz.getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Type type = params[0];
		Type finalNeedType;
		if (params.length > 1) {
			if (!(type instanceof ParameterizedType)) {
				throw new IllegalStateException("没有填写泛型参数");
			}
			finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
		} else {
			finalNeedType = type;
		}
		return GsonUtils.fromJson(responseBody.string(), type);
	}
}
