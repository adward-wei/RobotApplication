package com.ubtechinc.nets.http;


import com.ubtechinc.nets.BuildConfig;
import com.ubtechinc.nets.HttpManager;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @desc : 请求头插入
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public class HeaderInterceptor implements Interceptor {
    private final Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        String newUrl = request.url().toString();
        if (newUrl.startsWith(HttpManager.IM_TAG)) {
           newUrl = newUrl.replace(HttpManager.BASE_URL, BuildConfig.IM_HOST);
        }else if (newUrl.startsWith(HttpManager.SER_TAG)){
           newUrl = newUrl.replace(HttpManager.BASE_URL, BuildConfig.HOST);
        }
        Request.Builder builder = request.newBuilder();
        builder.url(newUrl);

        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, getValueEncoded(headers.get(headerKey))).build();
            }
        }
        return chain.proceed(builder.build());
    }

    private static String getValueEncoded(String value) throws UnsupportedEncodingException {
        if (value == null) return null;
        String newValue = value.replace("\n","");
        for (int i= 0, length = newValue.length(); i < length; i++){
            char c = newValue.charAt(i);
            //支持中文
            if (c <='\u001f' || c>= '\u007f'){
                return URLEncoder.encode(newValue, "UTF-8");
            }
        }
        return newValue;
    }
}
