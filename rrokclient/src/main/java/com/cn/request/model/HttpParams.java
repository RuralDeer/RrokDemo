package com.cn.request.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 2019/7/8
 * <p>
 * Time: 10:04 AM
 * <p>
 * author: 鹿文龙
 */
public class HttpParams implements Serializable {

    private LinkedHashMap<String, String> params = new LinkedHashMap<>();

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void put(HttpParams httpParams) {
        if (null == httpParams || null == httpParams.params) {return;}
        for (Map.Entry<String, String> entry : httpParams.params.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }
}