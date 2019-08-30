package com.cn.demo.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 不可直接使用
 * 这只是个DEMO
 */
public class SafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        //验证主机名是否匹配
        return hostname.equals("xxx.xxx");
    }
}