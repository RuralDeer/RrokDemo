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
public class HttpHeaders implements Serializable {

    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public void put(HttpHeaders httpParams) {
        if (null == httpParams || null == httpParams.headers) {return;}
        for (Map.Entry<String, String> entry : httpParams.headers.entrySet()) {
            headers.put(entry.getKey(), entry.getValue());
        }
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }
}