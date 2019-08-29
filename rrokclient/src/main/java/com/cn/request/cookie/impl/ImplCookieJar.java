/*
 * Copyright (C) 2017 zhouyou(478319399@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.request.cookie.impl;

import com.cn.request.cookie.CookieManager;
import com.cn.request.cookie.interfaces.IHtppCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * cookie管理器
 */
public class ImplCookieJar implements CookieJar {

    private IHtppCookieStore htppCookieStore;

    public ImplCookieJar(IHtppCookieStore htppCookieStore) {
        CookieManager.init(this);
        if (htppCookieStore == null) {
            throw new IllegalArgumentException("IHtppCookieStore can not be null!");
        }
        this.htppCookieStore = htppCookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        htppCookieStore.saveCookie(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return htppCookieStore.loadCookie(url);
    }

    public IHtppCookieStore getHtppCookieStore() {
        return htppCookieStore;
    }
}
