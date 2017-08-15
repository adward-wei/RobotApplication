package com.ubt.alpha2.statistics;

import android.text.TextUtils;

/**
 * @author: liwushu
 * @description: 构建map 所需要的键值对
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
public class KeyValuePair {
    private String mKey;
    private String mValue;

    private KeyValuePair(String key, String value){
        mKey = key;
        mValue = value;
    }

    public static KeyValuePair produce(String key, String value){
        if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)){
            return  new KeyValuePair(key, value);
        }
        else {
            return null;
        }
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }
}