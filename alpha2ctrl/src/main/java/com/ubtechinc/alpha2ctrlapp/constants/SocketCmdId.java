package com.ubtechinc.alpha2ctrlapp.constants;

/**
 * 与PC机通信的ID
 *
 * @author chenlin
 */
public class SocketCmdId {



    /**
     * 获取舵机角度值
     */
    static public final int ALPHA_MSG_GETMOTORANGLE = 10;


    /**
     * 传送照片
     **/
    public static final int ALPHA_MSG_TRANSFER_PHOTO = 53;







    // ///////////////////////////////////////////////
    // 消息回应
    /**
     * 登陆消息回应
     */
    static public final int ALPHA_MSG_RSP_LOGIN = 1;
    /**
     * 回应下载配置文件
     */
    static public final int ALPHA_MSG_RSP_DOWNLOADCFGFILE = 2;
    /**
     * 回应获取配置动作配置文件列表
     */
    static public final int ALPHA_MSG_RSP_GTCFGFILES = 3;
    /**
     * 回应删除动作文件
     */
    static public final int ALPHA_MSG_RSP_DELETEACTIONFILE = 4;
    /**
     * 回复执行动作表
     */
    static public final int ALPHA_MSG_RSP_PALYACTION = 5;
    /**
     * 回复配置WIFI连接点、
     */
    static public final int ALPHA_MSG_RSP_CONFIGUREWIFI = 6;
    /**
     * 回复获取AP连接点
     */
    static public final int ALPHA_MSG_RSP_GETWIFILIST = 7;
    /**
     * 回应设置连接的WIFI
     */
    static public final int ALPHA_MSG_RSP_SETWIFICONNECT = 8;
    /**
     * 响应设置舵机角度
     */
    static public final int ALPHA_MSG_RSP_SETMOTOANGLE = 9;
    /**
     * 回应读取舵机角度
     */
    static public final int ALPHA_MSG_RSP_READANGLE = 10;
    /**
     * 响应停止播放
     */
    static public final int ALPHA_MSG_RSP_STOPPLAY = 11;
    /**
     * 回应下载音频文件
     */
    static public final int ALPHA_MSG_RSP_DOWNLOADAUDIO = 12;
    /**
     * 回应获取动作表的相关文件列表
     */
    static public final int ALPHA_MSG_RSP_UPLOADACTION_FILELIST = 13;
    /**
     * 回应上传文件
     */
    static public final int ALPHA_MSG_RSP_UPLOADFILE = 14;

    /**
     * 回应获取动作配置表
     **/
    public static final int ALPHA_MSG_RSP_GETACTIONPROFILE = 15;
    /**
     * 回应保存配置表
     **/
    public static final int ALPHA_MSG_RSP_SAVEACTIONPROFILE = 16;
    /**
     * 回应仿真耳朵LED数据
     */
    public static final int ALPHA_MSG_RSP_PLAYEARLEDDATA = 17;
    /**
     * 回应停止耳朵LED播放
     */
    public static final int ALPHA_MSG_RSP_STOPPLAYEARLED = 18;
    /**
     * 回应仿真眼睛数据
     */
    public static final int ALPHA_MSG_RSP_PLAYEYESLEDDATA = 19;
    /**
     * 回应停止眼睛LED播放
     */
    public static final int ALPHA_MSG_RSP_STOPPLAYEYELED = 20;

    public static final int ALPHA_MSG_RSP_UPDATE_CHES_SOFTWARE = 21;
    public static final int ALPHA_MSG_RSP_UPDATE_HEADER_SOFTWARE = 22;
    /**
     * 保存闹钟
     **/
    public static final int ALPHA_MSG_RSP_SAVE_ALARMDETAIL = 23;
    /**
     * 查找所有闹钟
     **/
    public static final int ALPHA_MSG_RSP_ALL_ALARM = 24;
    /**
     * 更新闹钟
     **/
    public static final int ALPHA_MSG_RSP_UPDATE_ALRAM = 25;
    /**
     * 删除闹钟
     **/
    public static final int ALPHA_MSG_RSP_DELETE_ALARM = 26;
    /**
     * 获取录音文件表
     **/
    public static final int ALPHA_MSG_RSP_ALL_SOUNDRECORD = 27;
    /**
     * 保存所有闹钟到胸部
     **/
    public static final int ALPHA_MSG_RSP_SAVA_ALL_ALARM = 28;
    /**
     * 校准时间
     **/
    public static final int ALPHA_MSG_RSP_AJUST_TIME = 29;
    /**
     * 启动第三方app
     **/
    public static final int ALPHA_MSG_RSP_START_APP = 30;
    /**
     * 退出第三方app
     **/
    public static final int ALPHA_MSG_RSP_STOP_APP = 31;
    /**
     * 回应传输第三方app数据
     **/
    public static final int ALPHA_MSG_RSP_TRANSFER_APPDATA = 32;
    /**
     * 读胸部版本
     **/
    public static final int ALPHA_MSG_RSP_READ_CHEST_VERSION = 33;
    /**
     * 读头部版本
     **/
    public static final int ALPHA_MSG_RSP_READ_HEAD_VERSION = 34;
    /**
     * 获取全部APP
     **/
    public static final int ALPHA_MSG_RSP_GET_APPS = 35;
    /**
     * 设置调试模式
     **/
    public static final int ALPHA_MSG_RSP_DEBUG = 36;
    /**
     * 设置所有角度
     **/
    public static final int ALPHA_MSG_RSP_SETALLMOTOANGLE = 37;

    /**
     * 卸载第三方
     **/
    public static final int ALPHA_MSG_RSP_UNINSTALL_PACKAGES = 39;
    /**
     * 更新第三方
     **/
    public static final int ALPHA_MSG_RSP_UPDATE_PACKAGES = 40;
    /**
     * 第三方应用配置信息响应
     **/
    public static final int ALPHA_MSG_RSP_GET_APPCONFIG = 41;
    /**
     * 保存第三方app配置信息响应
     **/
    public static final int ALPHA_MSG_RSP_SAVE_APPCONFIG = 42;
    /**
     * 响应请求验证码
     **/
    public static final int ALPHA_MSG_RSP_VERIFYCATION_CODE = 43;// Verification
    // code
    /**
     * 响应获取当前APP
     **/
    public static final int ALPHA_MSG_RSP_GET_TOP_APP = 44;
    /**
     * 响应app按钮事件
     **/
    public static final int ALPHA_MSG_RSP_GET_APP_BUTTONEVENT = 45;
    /**
     * 响应app click
     **/
    public static final int ALPHA_MSG_RSP_CLICK_APP_BUTTON = 46;
    /**
     * 响应动作库下载
     **/
    public static final int ALPHA_MSG_RSP_ACTIONFILE_DOWNLOAD = 47;

    /**
     * 系统更新
     **/
    public static final int ALPHA_MSG_RSP_SYSTEM_REBOOT = 48;
    /**
     * 提醒功能请求
     **/
    public static final int ALPHA_MSG_RSP_DESKCLOCK = 49;


    /**
     * 写序列号响应
     **/
    public static final int ALPHA_MSG_RSP_WRITE_SERIALNUMBER = 51;
    /**
     * 读序列号响应
     **/
    public static final int ALPHA_MSG_RSP_READ_SERIALNUMBER = 52;
    /**
     * 传输照片响应
     **/
    public static final int ALPHA_MSG_RSP_TRANSFER_PHOTO = 53;
    /**
     * 硬件测试Log信息
     **/
    public static final int ALPHA_MSG_RSP_HARDWARE_TEST = 54;
    /**
     * 启动硬件检测app
     */
    public final static int ALPHA_START_HARDWARE_RESPONSE = 55;
    /**
     * 退出硬件检测app
     */
    public final static int ALPHA_STOP_HARDWARE_RESPONSE = 56;
    /**
     * 硬件检测按钮事件响应
     */
    public final static int ALPHA_EVENT_HARDWARE_RESPONSE = 57;
    /**
     * 恢复出厂设置响应
     **/
    public static final int ALPHA_MSG_RSP_MASTER_CLEAR = 58;
    /**
     * 设置RTC时间响应
     **/
    public static final int ALPHA_MSG_RSP_SET_RTC_TIME = 59;
    /**
     * 边充边玩响应
     **/
    public static final int ALPHA_MSG_RSP_CHARGE_AND_PLAY = 60;
    /**
     * 升级软件结果响应
     **/
    public static final int ALPHA_MSG_RSP_UPDATE_SOFTWARE_RESULT = 61;
    /**
     * 找手机响应
     **/
    public static final int ALPHA_MSG_RSP_FIND_MOBILEPHONE = 62;

    /**
     * 请求大图的响应
     **/
    public static final int ALPHA_MSG_RSP_TRANSFER_BIG_PHOTO = 65;
    /**
     * 请求所有缩略图的响应
     **/
    public static final int ALPHA_MSG_RSP_TRANSFER_THUMB_PHOTO = 66;
    /**
     * 查询电量的响应
     **/
    public static final int ALPHA_MSG_RSP_QUERY_POWER = 68;
    /**
     * 查询软硬件版本号的响应
     **/
    public static final int ALPHA_MSG_RSP_QUERY_VERSION = 69;
    /**
     * 获取新的动作列表的响应
     **/
    public static final int ALPHA_MSG_RSP_GET_RECENT_ACTIONLIST = 70;
    /**
     * 获取机器人状态信息的响应
     */
    static public final int ALPHA_MSG_RSP_ROBOT_STATUS = 73;

    /**
     * alexa视频监控
     */
    static public final int ALEXA_MSG_RSP_VIDEO_SURVEILLANCE = 75;
    /**
     * 闲聊动作
     **/
    public static final int ALPHA_MSG_RSP_CHAT_AND_ACTION = 90;

    /**
     * 上传错误日志
     **/
    public static final int ALPHA_MSG_RSP_UPLOAD_LOG = 92;

    /**
     * 获取已安装App列表响应
     **/
    public static final int ALPHA_RSP_APP_LIST_AND_STORAGE = 93;
    /**
     * 批量删除响应
     **/
    public static final int ALPHA_RSP_BATCH_UNINSTALL = 94;
    /**
     * 重新播放答案响应
     **/
    public static final int ALPHA_RSP_RETRY_PLAY_ANSWER = 95;

    public static final int ALPHA_BIND_ROBOT = 97;
    public static final int ALPHA_BIND_ROBOT_RESPONSE = 1097;

}
