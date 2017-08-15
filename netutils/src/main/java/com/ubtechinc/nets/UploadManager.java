package com.ubtechinc.nets;

import android.content.Context;

import com.google.gson.Gson;
import com.ubtechinc.nets.http.RawCallback;
import com.ubtechinc.nets.http.RestNet;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.http.Utils;

import java.lang.ref.SoftReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * @desc : 文件上传模块
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public final class UploadManager {

    private static final String BASE_URL = "http://services.ubtrobot.com/ubx/";
    private static UploadManager instance;
    private Context mContext;
    private RestNet mRestApi;

    private UploadManager(Context cxt) {
        this.mContext = cxt;
        init();
    }

    private void init() {
        RestNet.Builder builder = new RestNet.Builder(mContext);
        mRestApi = builder.cacheable(false)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .loggable(BuildConfig.DEBUG)
                .cookiable(false)
                .addInterceptor(GenericHeaders.createGenericHeaders(mContext))
                .connectPool(new ConnectionPool(1,8,TimeUnit.SECONDS))
                .baseUrl(BASE_URL)
                .build();
    }

    public static UploadManager get(Context cxt){
        if (instance != null) return instance;
        synchronized(HttpManager.class){
            if (instance == null) instance = new UploadManager(cxt);
        }
        return instance;
    }

    public <T> void upLoadImage(String url, String description, RequestBody image, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        Subscription subscription = mRestApi.upLoadImage(url, description, image, new ProxyRawCallback<T>(mContext, type, listener));
    }

    public <T> void upLoadFile(String url, String description, RequestBody file, final ResponseListener<T> listener){
        Type[] types = listener.getClass().getGenericInterfaces();
        if (Utils.hasUnresolvableType(types[0])){
            return;
        }
        Type type = Utils.getParameterUpperBound(0, (ParameterizedType) types[0]);
        Subscription subscription = mRestApi.upLoadFile(url, description, file,  new ProxyRawCallback<T>(mContext, type, listener));
    }

    private static final class ProxyRawCallback<T> implements RawCallback {
        private ResponseListener<T> listener;
        private final Type type;

        public ProxyRawCallback(Context cxt, Type type, ResponseListener<T> listener) {
            this.listener = listener;
            this.type = type;
        }

        @Override
        public void onError(ThrowableWrapper e) {
            if (listener !=null){
                listener.onError(e);
            }
        }

        @Override
        public void onSuccess(byte[] rawBytes) {
            if (listener == null) return;
            final String jsonStr = new String(rawBytes);
            listener.onSuccess((T) new Gson().fromJson(jsonStr, type));
        }
    }
}
