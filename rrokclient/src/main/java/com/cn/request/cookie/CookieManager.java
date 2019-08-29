package com.cn.request.cookie;

import com.cn.request.cookie.impl.ImplCookieJar;
import com.cn.request.cookie.interfaces.IHtppCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * cookie 管理器
 */
public class CookieManager {

    private static volatile CookieManager instance;

    public static CookieManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Please initialize Your \"CookieManager\" in ImplCookieJar before use");
        }
        return instance;
    }

    public static void init(ImplCookieJar cookieJar) {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    instance = new CookieManager(cookieJar);
                }
            }
        }
    }

    private IHtppCookieStore htppCookieStore;

    private CookieManager(ImplCookieJar cookieJar) {
        htppCookieStore = cookieJar.getHtppCookieStore();
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, Cookie cookie) {
        htppCookieStore.saveCookie(url, cookie);
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, List<Cookie> cookies) {
        htppCookieStore.saveCookie(url, cookies);
    }


    /**
     * 获取当前url对应的所有的cookie
     */
    public List<Cookie> getCookie(HttpUrl url) {
        return htppCookieStore.getAllCookie();
    }

    /**
     * 获取当前所有保存的cookie
     */
    public List<Cookie> getAllCookie() {
        return htppCookieStore.getAllCookie();
    }

    /**
     * 根据url和cookie移除对应的cookie
     */
    public boolean removeCookie(HttpUrl url, Cookie cookie) {
        return htppCookieStore.removeCookie(url, cookie);
    }

    /**
     * 根据url移除所有的cookie
     */
    public boolean removeCookie(HttpUrl url) {
        return htppCookieStore.removeCookie(url);
    }

    /**
     * 移除所有的cookie
     */
    public boolean removeAllCookie() {
        return htppCookieStore.removeAllCookie();
    }

}
