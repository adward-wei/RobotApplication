package com.ubtechinc.alpha.im;

/**
 * Created by Administrator on 2017/5/29.
 * 手机与机器人通信的命令字
 * 命名规范： IM_XXX_REQUEST,IM_XXX_RESPONSE
 */

public class IMCmdId {
    //request
    public static final int RESPONSE_BASE = 1000;

/***************************************客户端请求************************************************/
    /**
     * 删除动作文件
     */
    static public final int IM_DELETE_ACTIONFILE_REQUEST = 4;
    /**
     * 执行动作表
     */
    static public final int IM_PLAY_ACTION_REQUEST = 5;
    /**
     * 获取舵机角度值
     */
    static public final int IM_GET_MOTORANGLE_REQUEST = 10;
    /**
     * 停止执行动作
     */
    static public final int IM_STOP_PLAY_REQUEST = 11;
    /** 启动第三方app **/
    public static final int IM_START_APP_REQUEST = 30;
    /** 退出第三方app **/
    public static final int IM_STOP_APP_REQUEST = 31;
    /** 获取全部第三方app **/
    public static final int IM_GET_ALLAPPS_REQUEST  = 35;
    /** 设置调试模式 **/
    public static final int IM_SET_DEBUGMODE_REQUEST = 36;
    /** 设置所有角度 **/
    public static final int IM_SET_ALLMOTOANGLE_REQUEST = 37;
    /** 安装第三方 **/
    public static final int IM_INSTALL_PACKAGES_REQUEST = 38;
    /** 卸载第三方 **/
    public static final int IM_UNINSTALL_PACKAGES_REQUEST = 39;
    /** 更新第三方 **/
    public static final int IM_UPDATE_PACKAGES_REQUEST = 40;
    /** 获取第三方应用配置信息 **/
    public static final int IM_GET_APPCONFIG_REQUEST = 41;
    /** 保存第三方配置信息 **/
    public static final int IM_SAVE_APPCONFIG_REQUEST = 42;
    /** 获取当前APP模式 **/
    public static final int IM_GET_TOP_APP_REQUEST = 44;
    /** 请求获取app 按钮事件 **/
    public static final int IM_GET_APP_BUTTONEVENT_REQUEST = 45;
    /** 请求执行app 按钮 **/
    public static final int IM_CLICK_APP_BUTTON_REQUEST = 46;
    /** 动作下载 **/
    public static final int IM_DOWNLOAD_ACTIONFILE_REQUEST = 47;
    /** 闹钟功能请求 **/
    public static final int IM_DESKCLOCK_MANAGE_REQUEST = 49;
    /** 获取有效的闹钟列表请求 **/
    public static final int IM_DESKCLOCK_ACTIVIE_LIST_REQUEST = 50;
    /** 传送照片 **/
    public static final int IM_TRANSFER_PHOTO_REQUEST = 53;
    /** 恢复出厂设置 **/
    public static final int IM_MASTER_CLEAR_REQUEST = 58;
    /** 设置RTC时间 **/
    public static final int IM_SET_RTC_TIME_REQUEST = 59;
    /** 边充边玩 **/
    public static final int IM_SET_CHARGE_AND_PLAY_REQUEST = 60;
    /** 找手机 **/
    public static final int IM_FIND_MOBILEPHONE_REQUEST = 62;

    /**批量删除闹钟*/
    public static final int IM_DELETE_FORMER_CLOCK_REQUEST=63;
    /**获取所有闹钟*/
    public static final int IM_GET_FORMER_CLOCK_REQUEST=64;
    /**请求所有的缩略图*/
    public static final int IM_GET_ALL_THUMBNAIL_REQUEST =66;
    /**查询机器人电量*/
    public static final int IM_QUERY_ROBOT_POWER_REQUEST=68;
    /**获取机器人软硬件版本号*/
    public static final int IM_QUERY_HARD_SOFT_WARE_VERSION_REQUEST=69;
    /**获取动作列表*/
    public static final int IM_GET_NEW_ACTION_LIST_REQUEST =70;
    /**获取机器人初始化参数 状态信息  与72合并*/
    public static final int IM_GET_ROBOT_INIT_STATUS_REQUEST =72;
    /**管理机器人蓝牙 待确认*/
    public static final int IM_MANAGER_ROBOT_BLUETOOTH_REQUEST=74;
    /**闲聊tts动作 &呼吸动作开关*/
    public static final int IM_TTS_BREATH_ACTION_ON_OFF_REQUEST=90;
    /**语音合成指令*/
    public static final int IM_SYN_SPEECH_REQUEST=91;
    /**机器人错误日志开关*/
    public static final int IM_CLOSE_ROBOT_ERROR_LOG_REQUEST =92;
    /**查询机器人空间 app列表*/
    public static final int IM_QUERY_ROBOT_STORAGE_APP_LIST_REQUEST =93;
    /**批量卸载apps*/
    public static final int IM_UNINSTALL_BATCH_APPS_REQUEST=94;
    /**重新播放答案内容*/
    public static final int IM_RETRY_PLAY_ANSWER_REQUEST =95;
    /**连接机器人 control*/
    public static final int IM_CONNECT_ROBOT_REQUEST = 97;
    /**断开机器人 control*/
    public static final int IM_DISCONNECT_ROBOT_REQUEST = 98;
    /**设置主人名称*/
    public static final int IM_SET_MASTER_NAME_REQUEST = 99;
    /**获取闲聊tts动作 &呼吸动作开关*/
    public static final int IM_GET_TTS_BREATH_ACTION_ON_OFF_REQUEST=100;
    /** 获取边充边玩 **/
    public static final int IM_GET_CHARGE_AND_PLAY_REQUEST = 101;
    /**PC通信灯控制**/
    public static final int IM_GET_EMULATING_LED_REQUEST=102;
    public static final int IM_CONFIRM_ONLINE_REQUEST=106; //手机端发起的Ping包协议，用来判断机器人是否在线


    /**************************************后台推送********************************************/
    static public final int IM_OFFLINE_FROM_SERVER_RESPONSE = 2001;//后台推送IM状态变更
    /**************************************主服务推送，预留100个********************************************/
    static public final int IM_APP_INSTALL_STATE_RESPONSE = 1901;
    static public final int IM_ACTIONFILE_DOWNLOAD_STATE_RESPONSE = 1902;

    /***************************************返回************************************************/
    static public final int IM_CONNECT_ROBOT_RESPONSE = RESPONSE_BASE + IM_CONNECT_ROBOT_REQUEST;

    static public final int IM_DISCONNECT_ROBOT_RESPONSE = RESPONSE_BASE + IM_DISCONNECT_ROBOT_REQUEST;


    static public final int IM_DELETE_ACTIONFILE_RESPONSE = RESPONSE_BASE + IM_DELETE_ACTIONFILE_REQUEST;
    static public final int IM_PLAY_ACTION_RESPONSE = RESPONSE_BASE + IM_PLAY_ACTION_REQUEST;
    static public final int IM_GET_MOTORANGLE_RESPONSE = RESPONSE_BASE + IM_GET_MOTORANGLE_REQUEST;
    static public final int IM_STOP_PLAY_RESPONSE = RESPONSE_BASE + IM_STOP_PLAY_REQUEST;
    static public final int IM_START_APP_RESPONSE = RESPONSE_BASE + IM_START_APP_REQUEST;
    static public final int IM_STOP_APP_RESPONSE = RESPONSE_BASE + IM_STOP_APP_REQUEST;

    static public final int IM_GET_ALLAPPS_RESPONSE = RESPONSE_BASE + IM_GET_ALLAPPS_REQUEST;
    static public final int IM_SET_DEBUGMODE_RESPONSE = RESPONSE_BASE + IM_SET_DEBUGMODE_REQUEST;
    static public final int IM_SET_ALLMOTOANGLE_RESPONSE = RESPONSE_BASE + IM_SET_ALLMOTOANGLE_REQUEST;
    static public final int IM_INSTALL_PACKAGES_RESPONSE = RESPONSE_BASE + IM_INSTALL_PACKAGES_REQUEST;
    static public final int IM_UNINSTALL_PACKAGES_RESPONSE = RESPONSE_BASE + IM_UNINSTALL_PACKAGES_REQUEST;
    static public final int IM_UPDATE_PACKAGES_RESPONSE = RESPONSE_BASE + IM_UPDATE_PACKAGES_REQUEST;

    public static final int IM_GET_FORMER_CLOCK_RESPONSE = RESPONSE_BASE +IM_GET_FORMER_CLOCK_REQUEST;
    public static final int IM_DELETE_FORMER_CLOCK_RESPONSE = RESPONSE_BASE +IM_DELETE_FORMER_CLOCK_REQUEST;
    public static final int IM_QUERY_ROBOT_POWER_RESPONSE = RESPONSE_BASE + IM_QUERY_ROBOT_POWER_REQUEST;
    public static final int IM_QUERY_HARD_SOFT_WARE_VERSION_RESPONSE =RESPONSE_BASE +IM_QUERY_HARD_SOFT_WARE_VERSION_REQUEST;
    public static final int IM_MANAGER_ROBOT_BLUETOOTH_RESPONSE = RESPONSE_BASE+IM_MANAGER_ROBOT_BLUETOOTH_REQUEST;
    public static final int IM_TTS_BREATH_ACTION_ON_OFF_RESPONSE =RESPONSE_BASE +IM_TTS_BREATH_ACTION_ON_OFF_REQUEST;
    public static final int IM_GET_TTS_BREATH_ACTION_ON_OFF_RESPONSE =RESPONSE_BASE +IM_GET_TTS_BREATH_ACTION_ON_OFF_REQUEST;

    public static final int IM_SYN_SPEECH_RESPONSE =RESPONSE_BASE +IM_SYN_SPEECH_REQUEST;
    public static final int IM_CLOSE_ROBOT_ERROR_LOG_RESPONSE =RESPONSE_BASE +IM_CLOSE_ROBOT_ERROR_LOG_REQUEST;
    public static final int IM_CLOSE_ROBOT_ERROR_LOG_REQPONSE=RESPONSE_BASE +IM_CLOSE_ROBOT_ERROR_LOG_REQUEST;
    public static final int IM_UNINSTALL_BATCH_APPS_RESPONSE =RESPONSE_BASE + IM_UNINSTALL_BATCH_APPS_REQUEST;
    public static final int IM_RETRY_PLAY_ANSWER_RESPONSE =RESPONSE_BASE + IM_RETRY_PLAY_ANSWER_REQUEST;
    public static final int IM_QUERY_ROBOT_STORAGE_APP_LIST_RESPONSE =RESPONSE_BASE + IM_QUERY_ROBOT_STORAGE_APP_LIST_REQUEST;

    static public final int IM_GET_APPCONFIG_RESPONSE = RESPONSE_BASE + IM_GET_APPCONFIG_REQUEST;
    static public final int IM_SAVE_APPCONFIG_RESPONSE = RESPONSE_BASE + IM_SAVE_APPCONFIG_REQUEST;
    static public final int IM_GET_TOP_APP_RESPONSE = RESPONSE_BASE + IM_GET_TOP_APP_REQUEST;

    static public final int IM_GET_APP_BUTTONEVENT_RESPONSE = RESPONSE_BASE + IM_GET_APP_BUTTONEVENT_REQUEST;
    static public final int IM_CLICK_APP_BUTTON_RESPONSE = RESPONSE_BASE + IM_CLICK_APP_BUTTON_REQUEST;
    static public final int IM_DOWNLOAD_ACTIONFILE_RESPONSE = RESPONSE_BASE + IM_DOWNLOAD_ACTIONFILE_REQUEST;
    static public final int IM_DESKCLOCK_RESPONSE = RESPONSE_BASE + IM_DESKCLOCK_MANAGE_REQUEST;


    static public final int IM_DESKCLOCKLIST_RESPONSE = RESPONSE_BASE + IM_DESKCLOCK_ACTIVIE_LIST_REQUEST;

    static public final int IM_TRANSFER_PHOTO_RESPONSE = RESPONSE_BASE + IM_TRANSFER_PHOTO_REQUEST;
    static public final int IM_MASTER_CLEAR_RESPONSE = RESPONSE_BASE + IM_MASTER_CLEAR_REQUEST;
    static public final int IM_SET_RTC_TIME_RESPONSE = RESPONSE_BASE + IM_SET_RTC_TIME_REQUEST;
    static public final int IM_CHARGE_AND_PLAY_RESPONSE = RESPONSE_BASE + IM_SET_CHARGE_AND_PLAY_REQUEST;
    static public final int IM_FIND_MOBILEPHONE_RESPONSE = RESPONSE_BASE + IM_FIND_MOBILEPHONE_REQUEST;
    static public final int IM_GET_ROBOT_INIT_STATUS_RESPONSE =RESPONSE_BASE +IM_GET_ROBOT_INIT_STATUS_REQUEST ;
    static public final int IM_GET_ALL_THUMBNAIL_RESPONSE = RESPONSE_BASE + IM_GET_ALL_THUMBNAIL_REQUEST;
    static public final int IM_GET_NEW_ACTION_LIST_RESPONSE = RESPONSE_BASE + IM_GET_NEW_ACTION_LIST_REQUEST;
    static public final int IM_SET_MASTER_NAME_RESPONSE = RESPONSE_BASE + IM_SET_MASTER_NAME_REQUEST;
    static public final int IM_GET_CHARGE_AND_PLAY_RESPONSE = RESPONSE_BASE + IM_GET_CHARGE_AND_PLAY_REQUEST;
    static public final int IM_GET_EMULATING_LED_RESPONSE = RESPONSE_BASE+ IM_GET_EMULATING_LED_REQUEST ;
    static public final int IM_CONFIRM_ONLINE_RESPONSE = RESPONSE_BASE+ IM_CONFIRM_ONLINE_REQUEST ;

}
