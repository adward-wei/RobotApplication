package com.ubt.alpha2.upgrade.net;

import com.ubt.alpha2.upgrade.BuildConfig;

import java.util.HashMap;

/**
 * @author: slive
 * @description: 网络请求常量定义
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 * @link http://10.10.20.30/doku.php?id=%E7%BB%9F%E4%B8%80%E5%90%8E%E5%8F%B0:
 * %E6%96%87%E6%A1%A3%E8%AF%B4%E6%98%8E:%E5%9B%BA%E4%BB%B6%E6%9B%B4%E6%96%B0
 */
public class HttpCommon {
    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String BUILD_MODEL = "Alpha2";
    public static String ACTION_BASE_URL = BuildConfig.ACTION_BASE_URL;

    /*
     * http GET 请求
     * url:oauth/token
     * 获得access_token 信息
     */
    public static class AccessToken{
        public static final String URL = "/oauth/token";
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET ="client_secret";
        public static final String GRANT_TYPE ="grant_type";
        public static final String USER_NAME ="username";
        public static final String PASSWORD ="password";

        public static HashMap<String, String> getAccessTokenParams(){
            HashMap<String,String> params = new HashMap<>();
            params.put(CLIENT_ID,"mobile_1");
            params.put(CLIENT_SECRET,"secret_1");
            params.put(GRANT_TYPE,"password");
            params.put(USER_NAME, BUILD_MODEL);
            params.put(PASSWORD, BUILD_MODEL);
            return params;
        }
    }

    /*
     * https GET
     * url: file/download
     * 获得下载地址信息
     */
    public static class VersionInfo{
        public static final String URL = "/file/download";
        public static final String ACCESS_TOKEN = "access_token";
        //传入机器人name
        public static final String ROBOT_TYPE = "robot_type";

        //更新模块如：3-v1.0.1，多个模块的话：2-v1.1.1;5-v1.2.1;6-v1.1.1
        public static final String MODEL = "model";

        //机器序列号（机器唯一标识）
        public static final String ROBOT_ID = "robot_id";

        public static HashMap<String,String> getVersionInfoParams(String accessToken,String model,String robot_id){
            HashMap<String,String> params = new HashMap<>();
            params.put(MODEL,model);
            params.put(ACCESS_TOKEN,accessToken);
            params.put(ROBOT_TYPE,BUILD_MODEL);
            params.put(ROBOT_ID,robot_id);
            return params;
        }
    }

    /*
     * HTTPS GET
     * url:file/robotInfo
     * 获得机器人的模块信息
     *
     */
    public static class RobotModuleInfo{
        public static final String URL = "/file/robotInfo";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String ROBOT_TYPE = "robot_type";

        public static HashMap<String,String> getRobotModuleInfoParams(String accessToken){
            HashMap<String,String> params = new HashMap<>();
            params.put(ACCESS_TOKEN,accessToken);
            params.put(ROBOT_TYPE,BUILD_MODEL);
            return params;
        }
    }

    /*
    * https POST
    * url:file/callback
    * 反馈给服务器状态信息
    * response {"code":"0","data":"success"}
    */
    public static class UpgradeFeedBack{
        public static final String URL = "/file/callback";
        public static final String ACCESS_TYPE = "application/json; charset=UTF-8";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String ROBOT_ID = "robot_id";
        public static final String ROBOT_TYPE = "robot_type";
        public static final String MODULE_ID = "module_id";
        public static final String FROM_VERSION = "from_version";
        public static final String TO_VERSION = "to_version";
        public static final String UPGRADE_STATUS ="upgrade_status";
        public static final String ROBOT_IP = "robot_ip";

//        public static HashMap<String,String> getUpgradeFeedBackParam(FeedbackInfo feedbackInfo){
//            HashMap<String,String> param = new HashMap<>();
//            param.put(ACCESS_TOKEN,feedbackInfo.access_token);
//            param.put(ROBOT_ID, ApkUtils.getRobotId());
//            param.put(ROBOT_TYPE,BUILD_MODEL);
//            param.put(MODULE_ID,feedbackInfo.module_id);
//            param.put(FROM_VERSION,feedbackInfo.from_version);
//            param.put(TO_VERSION,feedbackInfo.to_version);
//            param.put(UPGRADE_STATUS,feedbackInfo.upgrade_status);
//            return param;
//        }
    }

    public static class UpgradeAction{
        public static final String URL = "/alpha2-web/actionFile/list";
        public static final String REPORT_URL = "/alpha2-web/actionFile/report";
        public static final String PRODUCT_TYPE = "productType";

        public static HashMap<String,String> getActionListParam(String productType){
            HashMap<String,String> params = new HashMap<>();
            params.put(PRODUCT_TYPE,productType);
            return params;
        }

    }

    //后台响应码
    public static class ResponseCode {
        public static final int OK = 0;
        public static final int NO_ROBOT_TYPE = 101;
        public static final int NO_VERSION = 102;
        public static final int NO_PERMISSION_UPGRADE = 103;
        public static final int NO_LANGUAGE_VERSION = 104;
        public static final int SERVICE_ERROR = 500;
    }
}
