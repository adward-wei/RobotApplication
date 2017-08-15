package com.ubtech.utilcode.utils;

import android.content.Context;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 16/12/08
 *     desc  : Utils初始化相关
 *
 */
public class Utils {

    private static Context context;
    private static SPUtils spUtils;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
        spUtils = new SPUtils("utilcode");
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static SPUtils getSpUtils() {
        return spUtils;
    }
}