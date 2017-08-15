package com.ubtechinc.alpha.model.upgrade;

/**
 * 升级方面的一些常数
 *
 * @author wangzhengtian
 * @Date 2017-03-09
 */

public class UpdateConstant {
    /** 升级apk发给主服务的广播 **/
    public static final String UPDATE_ACTION_MASTER = "com.ubtechinc.function.master";
    /** 主服务发给升级apk的广播 **/
    public static final String UPDATE_ACTION_SERVANT = "com.ubtechinc.function.servant";

    public static final String GET_COMMAND = "get_command";
    public static final String GET_PARAMETER = "get_parameter";

    /** 升级通信命令：询问升级状态 **/
    public static final int UPDATE_CMD_REQUEST_STATUS = 0;
    /** 升级通信命令：请求软件版本号 **/
    public static final int UPDATE_CMD_REQUEST_VERSION = 1;
    /** 升级通信命令：升级路径信息 **/
    public static final int UPDATE_CMD_INFO = 2;
    /** 升级通信命令：电量信息 **/
    public static final int UPDATE_CMD_POWERINFO = 3;
    /** 升级通信命令：语音提示信息 **/
    public static final int UPDATE_CMD_TTSCONTENT = 4;
    /** 升级通信命令：电池包升级结果 **/
    public static final int UPDATE_CMD_BATTERY_UPDATE_RESULT = 5;
    /** 升级通信命令：关机保护设置 **/
    public static final int UPDATE_CMD_SHUTDOWN_SETTING = 6;

    /** 嵌入式胸版升级 **/
    public static final int UPDATE_TYPE_EMBEDDED_CHEST = 0;
    /** 嵌入式头版升级 **/
    public static final int UPDATE_TYPE_EMBEDDED_HEAD = 1;
    /** 嵌入式舵机版升级 **/
    public static final int UPDATE_TYPE_EMBEDDED_MOTOR = 2;
    /** 嵌入式电池板升级 **/
    public static final int UPDATE_TYPE_EMBEDDED_BATTERY = 3;
    /** 动作文件升级 **/
    public static final int UPDATE_TYPE_ACTIONFILE = 4;
    /** 安卓系统升级 **/
    public static final int UPDATE_TYPE_SYSTEM = 5;
    /** 主服务、应用升级 **/
    public static final int UPDATE_TYPE_MAINSERVICE= 6;



    /** 升级包类型：差分包 **/
    public static final int UPDATE_FILE_PATCH = 0;
    /** 升级包类型：全量包 **/
    public static final int UPDATE_FILE_FULL = 1;

    /** 升级的状态：没有安装systemUpdate **/
    public static final int UPDATE_STATUS_NO_SYSTEMUPDATE = -1;
    /** 升级的状态：还没开始 **/
    public static final int UPDATE_STATUS_NOT_START = 0;
    /** 升级的状态：正在升级 **/
    public static final int UPDATE_STATUS_WORKING = 1;
    /** 升级的状态：升级完成 **/
    public static final int UPDATE_STATUS_FINISH = 2;


    /** 升级成功 **/
    public static final int UPDATE_RESULT_SUCCESS = 0;
    /** 升级失败 **/
    public static final int UPDATE_RESULT_FAIL = 1;

    /** 安卓系统升级 **/
    public static final String MODEL_ANDROID = "android";
    public static final String MODEL_ROS = "ros";
    /** 嵌入式胸版升级 **/
    public static final String MODEL_EMBEDDED_CHEST = "embedded_chest";
    /** 嵌入式头版升级 **/
    public static final String MODEL_EMBEDDED_HEAD = "embedded_head";
    /** 嵌入式舵机升级 **/
    public static final String MODEL_EMBEDDED_MOTOR = "embedded_motor";
    /** 嵌入式电池包升级 **/
    public static final String MODEL_EMBEDDED_BATTERY = "embedded_battery";
    /** 动作文件升级 **/
    public static final String MODEL_ACTION_FILE = "action_file";

    /*
     *0:无状态1:成功2:失败3:暂未更新
     */
    public static final String FEEDBACK_STATUS_NONE = "0";
    public static final String FEEDBACK_STATUS_SUCCESS = "1";
    public static final String FEEDBACK_STATUS_FAIL = "2";
    public static final String FEEDBACK_STATUS_NO_UPGRADE = "3";

    /** 升级状态：未知 **/
    public static final int UPGRADE_STATUS_UNKNOWN = 0;
    /** 升级状态：准备 **/
    public static final int UPGRADE_STATUS_PREPARE = 1;
    /** 升级状态：正在下载 **/
    public static final int UPGRADE_STATUS_DOWNLOADING = 2;
    /** 升级状态：下载完成 **/
    public static final int UPGRADE_STATUS_DOWNLOADED = 3;
    /** 升级状态：正在更新 **/
    public static final int UPGRADE_STATUS_UPDATING = 4;
    /** 升级状态：更新成功 **/
    public static final int UPGRADE_STATUS_UPDATE_SUCCESS = 5;
    /** 升级状态：更新失败 **/
    public static final int UPGRADE_STATUS_UPDATE_FAIL = 6;
    /** 升级状态：更新结束 **/
    public static final int UPGRADE_STATUS_UPDATE_FINISH = 7;
}
