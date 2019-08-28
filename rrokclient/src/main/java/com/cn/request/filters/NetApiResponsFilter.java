package com.cn.request.filters;

import com.cn.request.model.ApiResponse;

import io.reactivex.functions.Predicate;

/**
 * 拦截网络请求的数据，使其不返回
 *
 * @param <T>
 */
public class NetApiResponsFilter<T> implements Predicate<ApiResponse<T>> {
    @Override
    public boolean test(ApiResponse<T> tApiResponse) throws Exception {
        return false;
    }
}
