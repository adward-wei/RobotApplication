package com.ubtechinc.alpha.service;

import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/23.
 */
public class ServiceCache {

    /**cache binders object*/
    private static final Map<String, IBinder> sCache = new HashMap<>();

    public static void addService(String name, IBinder service) {
        sCache.put(name, service);
    }

    public static IBinder removeService(String name) {
        return sCache.remove(name);
    }

    public static IBinder getService(String name) {
        return sCache.get(name);
    }
}
