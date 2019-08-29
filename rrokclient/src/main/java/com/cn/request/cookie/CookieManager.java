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

    private static final String COOKIE_PREFS_NAME = "http_cookie";       //cookie使用prefs保存
    private static final String COOKIE_NAME_PREFIX = "cookie_";         //cookie持久化的统一前缀

    private static final String CookieManagerIsNull = "Please initialize Your \"CookieManager\" in Application before use";
    private static final String IHtppCookieStoreIsNull = "Please setHtppCookieStore Your \"IHtppCookieStore\" in CookieJar before use";


    private String cookiePrefsName = COOKIE_PREFS_NAME;
    private String cookieNamePrefix = COOKIE_NAME_PREFIX;
    private IHtppCookieStore htppCookieStore;

    private static volatile CookieManager instance;

    public static CookieManager getInstance() {
        if (instance == null) {
            throw new RuntimeException(CookieManagerIsNull);
        }
        return instance;
    }


    /**
     * 初始化 cookie SharedPreferences
     *
     * @param prefsName
     * @param namePrefix
     */
    public static void init(String prefsName, String namePrefix) {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    instance = new CookieManager(prefsName, namePrefix);
                }
            }
        }
    }

    private CookieManager(String prefsName, String namePrefix) {
        cookiePrefsName = TextUtils.isEmpty(prefsName) ? cookiePrefsName : prefsName;
        cookieNamePrefix = TextUtils.isEmpty(namePrefix) ? cookieNamePrefix : namePrefix;
    }

    public void setHtppCookieStore(IHtppCookieStore htppCookieStore) {
        this.htppCookieStore = htppCookieStore;
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, Cookie cookie) {
        HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).saveCookie(url, cookie);
    }

    /**
     * 保存url对应所有cookie
     */
    public void saveCookie(HttpUrl url, List<Cookie> cookies) {
        HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).saveCookie(url, cookies);
    }


    /**
     * 获取当前url对应的所有的cookie
     */
    public List<Cookie> getCookie(HttpUrl url) {
        return HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).getAllCookie();
    }

    /**
     * 获取当前所有保存的cookie
     */
    public List<Cookie> getAllCookie() {
        return HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).getAllCookie();
    }

    /**
     * 根据url和cookie移除对应的cookie
     */
    public boolean removeCookie(HttpUrl url, Cookie cookie) {
        return HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).removeCookie(url, cookie);
    }

    /**
     * 根据url移除所有的cookie
     */
    public boolean removeCookie(HttpUrl url) {
        return HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).removeCookie(url);
    }

    /**
     * 移除所有的cookie
     */
    public boolean removeAllCookie() {
        return HttpUtils.checkNotNull(htppCookieStore,IHtppCookieStoreIsNull).removeAllCookie();
    }

    public String getCookiePrefsName() {
        return cookiePrefsName;
    }

    public String getCookieNamePrefix() {
        return cookieNamePrefix;
    }
}
