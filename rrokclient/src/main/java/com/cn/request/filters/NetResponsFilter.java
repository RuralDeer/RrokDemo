package com.cn.request.filters;

import io.reactivex.functions.Predicate;

/**
 * 拦截网络请求的数据，使其不返回
 *
 * @param <T>
 */
public class NetResponsFilter<T> implements Predicate<T> {
    @Override
    public boolean test(T t) throws Exception {
        return false;
    }
}
