package com.ubtechinc.nets.http;

import android.text.TextUtils;

import com.ubtech.utilcode.utils.*;
import com.ubtechinc.nets.HttpManager;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.utils.JsonUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by tanghongyu on 2017/5/21.
 */

public class HttpProxy {
    private static HttpProxy instance;

    private HttpProxy() {

    }
    public static HttpProxy get(){
        if (instance != null) return instance;
        synchronized(HttpProxy.class){
            if (instance == null) instance = new HttpProxy();
        }
        return instance;
    }

    public <T> void doPost(Object baseRequest, final ResponseListener<T> listener) {
        String requestUrl = null;

        if(baseRequest.getClass().isAnnotationPresent(Url.class)) {
            Url url = baseRequest.getClass().getAnnotation(Url.class);
            requestUrl = url.value();

        }
        if(!TextUtils.isEmpty(requestUrl)) {
            HttpManager.get(com.ubtech.utilcode.utils.Utils.getContext()).doPostWithJson(requestUrl, JsonUtil.object2Json(baseRequest),listener);
        }else {//没有注解Url报错
            if (listener == null) return;
                listener.onError( ErrorUtil.handleException(new NullUrlException()));
        }
    }

    public <T> void doGet(Object baseRequest, final ResponseListener<T> listener){
        Class<?> declaredClass = baseRequest.getClass();
        Field[] fields = declaredClass.getDeclaredFields();

        HashMap<String ,String> param = new HashMap<>();
        String requestUrl = null;
        if(declaredClass.isAnnotationPresent(Url.class)) {
            Url url = declaredClass.getAnnotation(Url.class);
            requestUrl = url.value();

        }

        for (Field field : fields) {
            field.setAccessible(true); //设置些属性是可以访问的
            try {
                String key = field.getName();
                Object value = field.get(baseRequest);

                if(value!=null){
                    param.put(key, value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        if(!TextUtils.isEmpty(requestUrl)) {

            HttpManager.get(com.ubtech.utilcode.utils.Utils.getContext()).doGet(requestUrl, param, listener);
        }else {//没有注解Url报错
            listener.onError( ErrorUtil.handleException(new NullUrlException()));
        }
    }

    public <T> void doGet(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        HttpManager.get(com.ubtech.utilcode.utils.Utils.getContext()).doGet(url,maps,listener);
    }


}
