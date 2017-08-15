package com.ubtechinc.alpha2ctrlapp.constants;

/**
 *
 * Intent传递
 * @author：唐宏宇
 * @date：2016/6/23 17:24
 * @modifier：
 * @modify_date：
 * [A brief description]
 * version
 */
public class IntentConstants {

    //消息类型
    public static final String NOTICE_TYPE = "NoticeType";
    //通知消息
    public static final String NOTICE_MESSAGE = "MessageResponse";
    //是在线消息
    public static final String IS_ONLINE_MESSAGE = "isOnline";
    //离线消息
    public static final String IS_OFFLINE_MESSAGE = "isOffline";
    //机器人状态
    public static final String ROBOT_STATE = "status";
    //机器人状态
    public static final String ROBOT_ACTION_TYPE = "type";
    //机器人蓝牙状态
    public static final String ROBOT_BLUETOOTH_CONNECTION_STATE = "BULETOOTH_CONNECTION_STATE";
    //蓝牙设备信息
    public static final String ROBOT_BLUETOOTH_DEVICE = "BULETOOTH_DEVICE";
    //机器人配置信息
    public static final String ROBOT_PARAM = "ROBOT_PARAM";
    /*************************Action************************************/
    /**
     * 重连接状态acttion
     *
     */
    public static final String ACTION_RECONNECT_STATE = "action_reconnect_state";
    //第三方SDK授权验证
    public static final String ACTION_THIRD_SDK_VERIFY = "com.ubtechinc.alpha2ctrlapp.thirdsdk.verify";
    //第三方SDK发起机器人配网
    public static final String ACTION_THIRD_SDK_CONFIG_NETWORK = "com.ubtechinc.alpha2ctrlapp.thirdsdk.config.network";
    //第三方SDK发起机器人下载App
    public static final String ACTION_THIRD_SDK_DOWNLOAD_APP= "com.ubtechinc.alpha2ctrlapp.thirdsdk.downloadRobotApp.app";
    public static final String ACTION_APLPHA_CONTROL ="com.ubtechinc.alpha2ctrlapp.alpha.control";

    public static final String ACTION_ALPHA_CONNECTED_NET="alpha_online";


    /**
     * 花名册有删除的ACTION和KEY
     */
    public static final String ACTION_ROSTER_DELETED = "roster.deleted";

    /**
     * 花名册有更新的ACTION和KEY
     */
    public static final String ACTION_ROSTER_UPDATED = "roster.updated";


    /**
     * 花名册中成员状态有改变的ACTION和KEY
     */
    public static final String ACTION_ROSTER_PRESENCE_CHANGED = "roster.presence.changed";

    /**
     * 机器人状态发生改变时ACTION和KEY
     * */
    public static final String ACTION_ROBOT_STATE_CHANGED = "robot.state.changed";

    /************************************数据********************************************************************************/
    //机器控制人
    public static final String DATA_ROBOT_CONTROL_USER = "DATA_ROBOT_CONTROL_USER";
    public static final String DATA_ROBOT_CONTROL_APP = "DATA_ROBOT_CONTROL_APP";
    public static final String DATA_ROBOT_CONTROL_TIME = "DATA_ROBOT_CONTROL_TIME";
    /**
     * alpha掉线
     */
    public static final String ALPHA_AWAY = "alpha_away";
    public static final String DATA_IS_DOWNLOAD = "isDownload";
    public static final String DATA_APP_ID = "appId";
    //是否是手机注册
    public static final String DATA_IS_PHONE_REGISTER = "isPhoneRegister";
    //手机号码
    public static final String DATA_PHONE_NUMBER = "phoneNum";
    //用户ID
    public static final String DATA_USER_ID = "userId";
    //重置密码用的UUID
    public static final String DATA_UUID = "UUID";
    //是否是机器人本地数据
    public static final String DATA_IS_LOCAL_DATA = "isLocalData";
    //是否是动作搜索
    public static final String DATA_IS_ACTION_SEARCH = "isActionSearch";
    //搜索类型
    public static final String DATA_SEARCH_TYPE = "searchType";
    public static final String ALPHA_THIRD_RECEIVE_TOKEN ="alpha_third_receive_token";
    public static final String ALPHA_THIRD_RECEIVE_DEVICES ="alpha_third_receive_devices";
    public static final String ALPHA_THIRD_RECEIVE_USER_ID ="alpha_third_receive_user_id";
    public static final String ROBOT_USER_ID = "user_id";//机器人user id
    public static final String DATA_ROBOT_INFO = "robot_info";//机器人信息
    public static final String DATA_APP_LIST = "app_list";//机器人信息
}
