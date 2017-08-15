package com.ubtechinc.nets.http.cookie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Cookie;

/**
 * @desc : Cookie缓存
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/15
 * @modifier:
 * @modify_time:
 */

public final class CookieCache {
    private static final String COOKIE_SHARE_PREFS = "netutil_cookies_prefs";
    private final Set<Cookie> cookies;
    private final SharedPreferences spf;

    public CookieCache(Context context) {
        this.cookies = new HashSet<>();
        this.spf = context.getSharedPreferences(COOKIE_SHARE_PREFS, Context.MODE_PRIVATE);
        load();
    }

    private void load(){
        Iterator<String> iterator = spf.getAll().keySet().iterator();
        SharedPreferences.Editor editor = spf.edit();
        while (iterator.hasNext()){
            String key = iterator.next();
            String hexCookieStr = spf.getString(key, null);
            Cookie cookie = CookieUtil.deserialize(hexCookieStr);
            if (cookie == null) continue;
            if (!CookieUtil.cookieExpires(cookie)){
                editor.putString(key, null);
            }else {
                cookies.add(cookie);
            }
        }
        editor.commit();
    }

    synchronized void add(Collection<Cookie> cookies){
        this.cookies.addAll(cookies);
    }

    synchronized void add(Cookie cookie){
        cookies.add(cookie);
    }

    synchronized void remove(Collection<Cookie> cookies){
       this.cookies.removeAll(cookies);
    }

    synchronized void remove(Cookie cookie){
        this.cookies.remove(cookie);
    }

    synchronized void clear(){
        cookies.clear();
    }

    synchronized public void commit(){
        SharedPreferences.Editor editor = spf.edit();
        for (Cookie cookie: cookies){
            editor.putString(CookieUtil.cookieKey(cookie), CookieUtil.serialize(cookie));
        }
        editor.commit();
    }

    synchronized Iterator<Cookie> iterator(){
        return cookies.iterator();
    }
}
