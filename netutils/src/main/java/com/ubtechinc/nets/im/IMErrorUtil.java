package com.ubtechinc.nets.im;

import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/16
 * @modifier:
 * @modify_time:
 */

public final class IMErrorUtil {
    public class ERROR {
        /**未知错误*/
        public static final int UNKNOWN = 1000;
        /** 机器人未连接*/
        public static final int ROBOT_NOT_AVAILABLE = 1002;
        /**添加节点错误*/
        public static final int ADD_ELEMENT_FAIL = 1003;
        /**解包失败*/
        public static final int UNPACK_FAIL = 1004;
        /**没有数据返回*/
        public static final int NULL_DATA = 1006;
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

    public static ThrowableWrapper handleException(int code) {
        ThrowableWrapper ex;
        ex = new ThrowableWrapper("未知错误", 500);
            switch (code) {
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
                    ex.setMessage("未知错误");
                    break;
            }
            return ex;
        }
    }




