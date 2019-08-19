package com.cn.request.func.send;

import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Date: 2019/7/26
 * <p>
 * Time: 3:32 PM
 * <p>
 * author: 鹿文龙
 * <p>
 * Retrofit 在这里发送同步请求
 */
public class SendRequestFunc<T> implements Function<Call<T>, T> {
	@Override
	public T apply(Call<T> tCall) throws Exception {
		Response<T> response = tCall.clone().execute();
		if (!response.isSuccessful()) {
			throw new Exception(response.errorBody().string());
		}
		return response.body();
	}
}
