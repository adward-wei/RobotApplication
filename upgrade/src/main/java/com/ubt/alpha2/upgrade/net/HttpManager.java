package com.ubt.alpha2.upgrade.net;

import android.content.Context;

import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtechinc.nets.GenericHeaders;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.RawCallback;
import com.ubtechinc.nets.http.RestNet;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.http.Utils;

import java.lang.ref.SoftReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;

/**
 * @author: slive
 * @description: 管理 http 请求
 * @create: 2017/7/14
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class HttpManager {

    private static final String CACHE_DIR = "cache/http";

    private static HttpManager instance;
    private Context mContext;

    private RestNet mRestApi;
    private RestNet mRestActionApi;
    private static final String TAG = "HttpManager";

    private HttpManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void init(Context context,String baseUrl) {
        RestNet.Builder builder = new RestNet.Builder(context);
        mRestApi= builder.cacheable(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .loggable(true)
                .cookiable(true)
                .addInterceptor(GenericHeaders.createGenericHeaders(mContext))
                .cacheDirName(CACHE_DIR)
                .connectPool(new ConnectionPool(3,8,TimeUnit.SECONDS))
                .baseUrl(baseUrl)
                .build();
    }

    public void initAction(Context context,String baseUrl){
        RestNet.Builder builder = new RestNet.Builder(context);
        mRestActionApi = builder.cacheable(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .loggable(true)
                .cookiable(true)
                .addInterceptor(GenericHeaders.createGenericHeaders(mContext))
                .cacheDirName(CACHE_DIR)
                .connectPool(new ConnectionPool(3,8,TimeUnit.SECONDS))
                .baseUrl(baseUrl)
                .build();
    }

    public static HttpManager get(Context context){
        if(instance == null){
            synchronized(HttpManager.class){
                if (instance == null)
                    instance = new HttpManager(context);
            }
        }
        return instance;
    }

    public <T> void doGet(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);

        mRestApi.doGet(url, maps, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doPostWithForm(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        mRestApi.doPostWithForm(url, maps, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doPostWithJson(String url, String jsonStr, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        mRestApi.doPostWithJson(url, jsonStr, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doPutWithForm(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        mRestApi.doPutWithForm(url, maps, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doPutWithJson(String url, String jsonStr, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
       mRestApi.doPutWithJson(url, jsonStr, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doDelete(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        mRestApi.doDelete(url, maps, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    private static final class ProxyRawCallback<T> implements RawCallback {

        private ResponseListener<T> softListener;
        private SoftReference<Context> soltCxt;
        private final Type type;

        public ProxyRawCallback(Context cxt, Type type, ResponseListener<T> listener) {
            softListener = listener;
            this.soltCxt = new SoftReference<Context>(cxt);
            this.type = type;
        }

        @Override
        public void onError(ThrowableWrapper e) {
            ResponseListener<T> listener = softListener;
            if (listener !=null){
                listener.onError(e);
            }
        }

        @Override
        public void onSuccess(byte[] rawBytes) {

            ResponseListener<T> listener = softListener;
            if (listener == null) return;
            final String jsonStr = new String(rawBytes);

            listener.onSuccess((T) JsonUtils.getObject(jsonStr, type));
        }
    }

    //转换接口
    public interface HttpResponseListener<T> extends ResponseListener<T>{

    }

    public <T> void doGetAction(String url, HashMap<String, String> maps, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);

        mRestActionApi.doGet(url, maps, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void doPostAction(String url,String jsonStr, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        mRestActionApi.doPostWithJson(url, jsonStr, new HttpManager.ProxyRawCallback<T>(mContext, type, listener));
    }
}
