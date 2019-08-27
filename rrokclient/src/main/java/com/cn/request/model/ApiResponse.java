package com.cn.request.model;

import com.cn.request.enums.DataSource;

/**
 * Date: 2019/7/22
 * <p>
 * Time: 2:15 PM
 * <p>
 * author: 鹿文龙
 */
public final class ApiResponse<T> {

    public T data;

    public DataSource dataSource;

    public ApiResponse(T data, DataSource dataSource) {
        this.data = data;
        this.dataSource = dataSource;

    }
}
