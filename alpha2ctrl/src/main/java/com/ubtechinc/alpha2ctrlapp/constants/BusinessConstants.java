package com.ubtechinc.alpha2ctrlapp.constants;


/**
 * @ClassName BusinessConstants
 * @date 4/6/2017
 * @author tanghongyu
 * @Description 业务相关常量
 * @modifier
 * @modify_time
 */
public class BusinessConstants {

    //未登陆过
    public static final int LOGIN_STATE_NO_LOGIN = 0;
    //登录过期
    public static final int LOGIN_STATE_LOGIN_TIME_OUT = 2;
    //登录有效
    public static final int LOGIN_STATE_LOGIN_EFFECTIVE = 1;

    /*************************
     * 通知消息
     **************************************/
    //	public static final int NOTICE_TYPE_ADD_FRIEND = 1;// 好友请求，已停用
    public static final int NOTICE_TYPE_SYS_MSG = 2; // 系统消息
    public static final int NOTICE_TYPE_CHAT_MSG = 3;// 聊天消息，已停用
    public static final int NOTICE_TYPE_AUTHORIZE_MSG = 1; // 授权消息
    public static final int NOTICE_TYPE_CHAT_TYPE_PIC = 5; // 图片消息
    public static final int NOTICE_STATE_READ = 1;//已读
    public static final int NOTICE_STATE_UNREAD = 0;//未读
    public static final int NOTICE_STATE_All = 2;//全部
    // 1 表示授權  2 表示主动刪除授權 3表示给主人提示我同意授权 4 表示被删除授权
    public static final int AUTHORIZATION_TYPE_AUTHORIZE = 1;//授权
    public static final int AUTHORIZATION_TYPE_DELETE = 2;//主动删除授权
    public static final int AUTHORIZATION_TYPE_ACCEPT = 3;//接受授权
    public static final int AUTHORIZATION_TYPE_CANCEL = 4;//取消授权

    public static final String XMPP_MESSAGE_TYPE = "addrobotfriend";//机器人授权
    //控制机器人
    public static final String ROBOT_CONTROL_START = "start";
    //断开机器人
    public static final String ROBOT_CONTROL_STOP = "stop";
    //控制机器人成功
    public static final String ROBOT_CONTROL_RESULT_START_OK = "start ok";
    //控制机器人失败
    public static final String ROBOT_CONTROL_RESULT_START_FAIL = "start fail";
    //断开机器人成功
    public static final String ROBOT_CONTROL_RESULT_STOP_OK = "stop ok";
    //断开机器人失败
    public static final String ROBOT_CONTROL_RESULT_STOP_FAIL = "stop fail";
    //强制断开
    public static final String ROBOT_CONTROL_RESULT_FORCE_STOP = "force stop";
    //与机器人不是朋友
    public static final String ROBOT_CONTROL_RESULT_NOT_FRIEND = "not friend";// 与机器人不是好友

    public static final String ROBOT_STOP_RELEASE_AUTHOURIZE = "stop_release_authority"; // 被删除授权
    /******************************机器人状态**********************************/
    //机器人在线
    public static final int ROBOT_STATE_UNAVAILABLE = -1;
    public static final int ROBOT_STATE_OFFLINE = 0;
    public static final int ROBOT_STATE_ONLINE = 1;
    // 已连接
    public static final int ROBOT_STATE_CONNECTED = 2;

    public static final String ROBOT_IM_STATE_ONLINE = "Online";
    public static final String ROBOT_IM_STATE_OFFLINE = "Offline";
    public static final String ROBOT_IM_STATE_CHANGE_LOGIN = "Login";
    public static final String ROBOT_IM_STATE_CHANGE_LOGOUT = "Logout";
    /******************************动作类型**********************************/
    //1:动作  2：音乐舞蹈   3:寓言故事  4:儿歌 5 科普 0:全部
    //机器人动作（基本）
    public static final int ROBOT_ACTION_TYPE_BASE = 1;
    //音乐和跳舞
    public static final int ROBOT_ACTION_TYPE_MUSIC_AND_DANCE = 2;
    //故事和笑话
    public static final int ROBOT_ACTION_TYPE_STORY_AND_FABLE = 3;
    //所有动作
    public static final int ROBOT_ACTION_TYPE_TOTAL = 0;

    //扫描
    public static final int ROBOT_BLUETOOTH_CMD_SCAN = 1;
    //返回扫描列表，扫一批发一批
    public static final int ROBOT_BLUETOOTH_CMD_GET_SCAN_RESULT = 1;
    //扫描结束
    public static final int ROBOT_BLUETOOTH_CMD_GET_SCAN_STOP = 2;

    //获取配对过的设备
    public static final int ROBOT_BLUETOOTH_CMD_GET_MATCHED_DEVICE_LIST = 2;
    //.中断扫描
    public static final int ROBOT_BLUETOOTH_CMD_BREAK_OFF_SCAN = 3;
    //连接设备
    public static final int ROBOT_BLUETOOTH_CMD_CONNECT_DEVICE = 4;
    //断开连接
    public static final int ROBOT_BLUETOOTH_CMD_DISCONNECT_DEVICE = 5;
    //蓝牙管理返回码 1成功 0失败 3接收到消息开始执行
    public static final int ROBOT_BLUETOOTH_MANAGE_RETCODE_SUCCESS = 1;
    public static final int ROBOT_BLUETOOTH_MANAGE_RETCODE_FAIL = 0;
    public static final int ROBOT_BLUETOOTH_MANAGE_RETCODE_START = 3;
    //获取连接状态
    public static final int ROBOT_BLUETOOTH_CMD_GET_CONNECTION_STATUS = 6;

    public static final int ROBOT_BLUETOOTH_BOND_STATUS_BOND_NONE = 10;
    public static final int ROBOT_BLUETOOTH_BOND_STATUS_BOND_BONDING = 11;
    public static final int ROBOT_BLUETOOTH_BOND_STATUS_BOND_BONDED = 12;
    //机器人主服务的语言版本，中文
    public static final String ROBOT_SERVICE_LANGUAGE_CHINESE = "zh_cn";
    public static final String ROBOT_SERVICE_LANGUAGE_SHOW_CHINESE = "中文";
    //英文
    public static final String ROBOT_SERVICE_LANGUAGE_ENGLISH = "en_us";
    public static final String ROBOT_SERVICE_LANGUAGE_SHOW_ENGLISH = "英文";
    /*************************** APP下载状态**********************/
    //未知错误
    public static final int APP_STATE_ERROR = -1;
    //初始化
    public static final int APP_STATE_INIT = 0;
    //下载中
    public static final int APP_STATE_DOWNLOADING = 1;
    //下载成功
    public static final int APP_STATE_DOWNLOAD_SUCCESS = 2;
    //下载失败
    public static final int APP_STATE_DOWNLOAD_FAIL = 3;
    //解压成功
    public static final int APP_STATE_INSTALL_SUCCESS = 4;
    //解压失败
    public static final int APP_STATE_INSTALL_FAIL = 5;
    //不能下载
    public static final int APP_STATE_CAN_NOT_DOWNLOAD = 6;
    //有更新
    public static final int APP_STATE_ROBOT_APP_HAS_UPDATE = 7;
    //存储空间不足
    public static final int APP_STATE_INSUFFCIENT_SPACE = 8;
    //手机端App已经安装
    public static final int APP_STATE_MOBILE_APP_INSTALLED = 10;
    //机器人端已经安装，手机端App还需要安装
    public static final int APP_STATE_MOBILE_APP_NEED_INTALL = 11;
    //需要的App都已经安装
    public static final int APP_STATE_APP_ALL_INSTALLED = 12;
    //没有机器人端，只有手机端App需要安装
    public static final int APP_STATE_ONLY_MOBILE_APP_NEED_INTALL = 13;

    //手机客户端有更新
    public static final int APP_STATE_MOBILE_APP_HAS_UPDATE = 14;

    public static final int THIRD_VERIFY_FROM_SYSTEM_IOS = 1;
    public static final int THIRD_VERIFY_FROM_SYSTEM_ANDROID = 2;
    //ios客户端
    public static final String SHOP_SDK_TYPE_IOS = "1";
    //Android客户端
    public static final String SHOP_SDK_TYPE_ANDROID = "2";
    //机器人客户端
    public static final String SHOP_SDK_TYPE_ONBOARD = "3";
    //家庭娱乐
    public static final String SHOP_APP_TYPE_FAMILY_ENTERTAILMENT = "1";
    //教育教学
    public static final String SHOP_APP_TYPE_EDUCATION = "2";
    //商业演出
    public static final String SHOP_APP_TYPE_COMMERCIAL_PERFOR = "3";
    //医疗看护
    public static final String SHOP_APP_MEIDICAL_CARE = "4";
    //办公助手
    public static final String SHOP_APP_TYPE_OFFIC_ASSISTANT = "5";
    //其他
    public static final String SHOP_APP_TYPE_OTHER = "6";
    //主程序
    public static final String APP_LINK_MAIN = "0";
    //账户类型手机
    public static final int ACCOUNT_TYPE_PHONE = 1;
    //账户类型Email
    public static final int ACCOUNT_TYPE_EMAIL = 2;
    public static final int COMMENT_TYPE_COMMENT = 1;
    public static final int COMMENT_TYPE_REPLY = 2;
    public static final int APP_TYPE = 2;

    public static final String CONTENT_TYPE = "application/json";
    public static final String REQUEST_METHOD = "GET";
    public static final int SHOP_TYPE_APP = 0;
    public static final int SHOP_TYPE_ACTION = 1;

    public static final String DEFAULT_COUNTRY_CODE = "-1";

    /***************************服务端保存应用包名***************************************************/
    public static final String PACKAGENAME_VIDEO_SUPERVISION = "com.alpha2.videoSupervision";
    public static final String PACKAGENAME_ALPHA_TRANSLATION = "com.ubtechinc.alphatranslation";
    public static final String PACKAGENAME_SHOP_GALLARY = "com.ubt.image";
    public static final String PACKAGENAME_SHOP_ACTION = "com.ubtech.action";
    public static final String PACKAGENAME_SHOP_ALARM = "com.ubtech.alarm";
    public static final String PACKAGENAME_CH_CHAT = "com.ubtech.iflytekmix";
    public static final String PACKAGENAME_EN_CHAT = "com.ubtechinc.alphaenglishchat";
    /***************************自定义应用包名***************************************************/
    public static final String PACKAGENAME_TEMP_ACTION = "com.ubtech.actionmodel";
    public static final String PACKAGENAME_TEMP_ALARM = "com.ubtech.deskclock";

    public static final String APP_LANGUAGE_CN = "zh_cn";
    public static final String APP_LANGUAGE_EN = "en";

}
