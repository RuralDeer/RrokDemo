package com.cn.request.mock;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Date: 2019-08-28
 * <p>
 * Time: 17:18
 * <p>
 * author: 鹿文龙
 *
 * 模拟call方法 不会走真正Okhttp流程
 */
final class MockerCall<T> implements Call<T> {

    private T data;

    public MockerCall(T data){
        this.data = data;
    }

    @Override
    public Response<T> execute() throws IOException {
        return Response.success( data );
    }

    @Override
    public void enqueue(Callback<T> callback) {
        callback.onResponse( null, Response.success( data ));
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return this;
    }

    @Override
    public Request request() {
        return null;
    }
}