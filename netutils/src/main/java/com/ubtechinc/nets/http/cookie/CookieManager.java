package com.ubtechinc.nets.http.cookie;

import com.ubtech.utilcode.utils.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @desc : http cookie管理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/15
 * @modifier:
 * @modify_time:
 */

public class CookieManager implements CookieJar {
    private static final String TAG = "CookieManager";
    private CookieCache cache;

    public CookieManager(CookieCache cache) {
        this.cache = cache;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if(url.toString().contains("logout")){
            cache.clear();
        }else{
            cache.add(filterPersistentCookies(cookies));
        }
        commit();
    }

    private List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        LogUtils.d(TAG, cookies);
        List<Cookie> persistentCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
//            if (cookie.persistent()) {
                persistentCookies.add(cookie);
            LogUtils.d(TAG ,  "filterPersistentCookies persistentCookies.add = ", cookie);
//            }
        }
        return persistentCookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {

        List<Cookie> cookies = new ArrayList<>();
        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();
//            if (currentCookie.matches(url)) {
                LogUtils.d(TAG ,  "loadForRequest = ", currentCookie);
                cookies.add(currentCookie);
//            }
        }
        return cookies;
    }

    public void commit(){
        cache.commit();
    }
}
