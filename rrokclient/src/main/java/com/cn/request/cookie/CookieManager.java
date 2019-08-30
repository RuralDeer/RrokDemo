package com.cn.request.cookie;

import android.text.TextUtils;

import com.cn.request.cookie.interfaces.IHtppCookieStore;
import com.cn.request.utils.HttpUtils;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * cookie 管理器
 */
public class CookieManager {

    private static final String CookieManagerIsNull = "Please initialize Your \"CookieManager\" in CookieJar before use";

    private IHtppCookieStore htppCookieStore;

    private static volatile CookieManager instance;

    public static CookieManager getInstance() {
        if (instance == null) {
            throw new RuntimeException(CookieManagerIsNull);
        }
        return instance;
    }


    /**
     * 初始化
     * @param htppCookieStore
     */
    public static void init(IHtppCookieStore htppCookieStore) {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    instance = new CookieManager(htppCookieStore);
                }
            }
        }
    }

    private CookieManager(IHtppCookieStore htppCookieStore) {
        this.htppCookieStore = htppCookieStore;
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, Cookie cookie) {
        HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).saveCookie(url, cookie);
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, List<Cookie> cookies) {
        HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).saveCookie(url, cookies);
    }


    /**
     * 获取当前url对应的所有的cookie
     */
    public List<Cookie> getCookie(HttpUrl url) {
        return HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).getAllCookie();
    }

    /**
     * 获取当前所有保存的cookie
     */
    public List<Cookie> getAllCookie() {
        return HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).getAllCookie();
    }

    /**
     * 根据url和cookie移除对应的cookie
     */
    public boolean removeCookie(HttpUrl url, Cookie cookie) {
        return HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).removeCookie(url, cookie);
    }

    /**
     * 根据url移除所有的cookie
     */
    public boolean removeCookie(HttpUrl url) {
        return HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).removeCookie(url);
    }

    /**
     * 移除所有的cookie
     */
    public boolean removeAllCookie() {
        return HttpUtils.checkNotNull(htppCookieStore, CookieManagerIsNull).removeAllCookie();
    }
}
