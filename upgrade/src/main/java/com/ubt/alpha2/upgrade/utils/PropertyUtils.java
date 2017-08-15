package com.ubt.alpha2.upgrade.utils;

import android.content.Context;

/**
 * Created by ubt on 2017/6/30.
 */

public class PropertyUtils {

    public static void setSystemProperty(Context mContext, String key,String value){
        //Alpha2Property.set(mContext, key,value);
    }

    public static String getSystemProperty(Context mContext, String key,String defValue){
        return Alpha2Property.get(mContext, key,defValue);
    }

    public static String getSystemProperty(Context mContext, String key){
        return Alpha2Property.get(mContext, key);
    }
}
