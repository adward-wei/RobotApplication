package com.ubtechinc.alpha2ctrlapp.constants;

/**
 * @author：tanghongyu
 * @date：4/6/2017 2:35 PM
 * @modifier：tanghongyu
 * @modify_date：4/6/2017 2:35 PM
 * [A brief description]
 * version
 */

public class ActionConstants {

    /** 蓝牙没有配对 **/
    public static final String BOND_NONE = "BLUETOOTH_BOND_NONE";
    /** 开始连接Socket **/
    public static final String START_UP_CONNECTING = "startUpSocketConnected";
    /** Socket连接成功 **/
    public static final String CONNECT_SUCCESS = "startUpSocketConnected_success";
    /** Socket连接异常 **/
    public static final String CONNECTED_FAILURE = "startUpSocketConnected_failure";
    /** 登录成功 **/
    public static final String LOGIN_SUCCESS = "connectSuccess";
    /** 登录失败 **/
    public static final String LOGIN_FAILURE = "loginFailure";
    /** 被占用 **/
    public static final String LOGIN_HAS_OWNER = "has_owner";
    /** �?始切换连接方�? **/
    public static final String STARTCHANGCONNECTTYPE = "startChangConnectType";
    /** 切换 -- 断开�?有连接结�? **/
    public static final String ENDCHANGCONNECTTYPE = "endChangConnectType";
    /** 掉线 **/
    public static final String LOST_CONNECTED = "lostConnected";
}
