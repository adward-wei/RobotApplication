package com.ubtechinc.nets.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public final class ErrorUtil {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int ACCESS_DENIED = 302;
    private static final int HANDEL_ERRROR = 417;

    public static ThrowableWrapper handleException(Throwable e) {
        ThrowableWrapper ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ThrowableWrapper(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.setMessage("未授权的请求");
                case FORBIDDEN:
                    ex.setMessage("禁止访问");
                case NOT_FOUND:
                    ex.setMessage("服务器地址未找到");
                case REQUEST_TIMEOUT:
                    ex.setMessage("请求超时");
                case GATEWAY_TIMEOUT:
                    ex.setMessage("网关响应超时");
                case INTERNAL_SERVER_ERROR:
                    ex.setMessage("服务器出错");
                case BAD_GATEWAY:
                    ex.setMessage("无效的请求");
                case SERVICE_UNAVAILABLE:
                    ex.setMessage("服务器不可用");
                case ACCESS_DENIED:
                    ex.setMessage("网络错误");
                case HANDEL_ERRROR:
                    ex.setMessage("接口处理失败");
                default:
                    ex.setMessage(e.getMessage());
                    break;
            }
            return ex;
        }  else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ThrowableWrapper(e, ERROR.PARSE_ERROR);
            ex.setMessage("解析错误");
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ThrowableWrapper(e, ERROR.NETWORD_ERROR);
            ex.setMessage("连接失败");
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ThrowableWrapper(e, ERROR.SSL_ERROR);
            ex.setMessage("证书验证失败");
            return ex;
        } else if (e instanceof java.security.cert.CertPathValidatorException) {
            ex = new ThrowableWrapper(e, ERROR.SSL_NOT_FOUND);
            ex.setMessage("证书路径没找到");
            return ex;
        }
        else if (e instanceof ConnectTimeoutException){
            ex = new ThrowableWrapper(e, ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ThrowableWrapper(e, ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            return ex;
        } else if (e instanceof java.lang.ClassCastException) {
            ex = new ThrowableWrapper(e, ERROR.FORMAT_ERROR);
            ex.setMessage("类型转换出错");
            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new ThrowableWrapper(e, ERROR.FORMAT_ERROR);
            ex.setMessage("数据格式错误");
            return ex;
        } else if (e instanceof IOException){
            ex = new ThrowableWrapper(e, ERROR.FORMAT_ERROR);
            ex.setMessage("数据格式错误");
            return ex;
        }else if(e instanceof NullUrlException) {
            ex = new ThrowableWrapper(e, ERROR.NULL_URL);
            ex.setMessage("请求Url为空");
            return ex;
        } else {
            ex = new ThrowableWrapper(e, ERROR.UNKNOWN);
            ex.setMessage(e.getLocalizedMessage());
            return ex;
        }
    }

    public class ERROR {
        /**未知错误*/
        public static final int UNKNOWN = 1000;
        /** 解析错误*/
        public static final int PARSE_ERROR = 1001;
        /**网络错误*/
        public static final int NETWORD_ERROR = 1002;
        /**协议出错*/
        public static final int HTTP_ERROR = 1003;
        /**证书出错*/
        public static final int SSL_ERROR = 1005;
        /**连接超时*/
        public static final int TIMEOUT_ERROR = 1006;
        /**证书未找到*/
        public static final int SSL_NOT_FOUND = 1007;
        /**出现空值*/
        public static final int NULL = 1008;
        /**格式错误*/
        public static final int FORMAT_ERROR = 1009;
        /**请求Url为空*/
        public static final int NULL_URL = 1010;
    }
}
