package com.ubtechinc.nets.im.util.parser;

/**
 * @author：adward
 * @date：12/14/2016 2:17 PM
 * @modifier：tanghongyu
 * @modify_date：12/14/2016 2:17 PM
 * [A brief description]
 * version
 */

public class UbtMessageConstants {
    //状态变化(绑定状态，上，下线状态)
    public static final String TYPE_STATE_CHANGE = "stateChange";
    //命令
    public static final String TYPE_COMMAND = "command";
    //命令
    public static final String TYPE_SKILL = "skill";
    //命令
    public static final String TYPE_CONTROL = "control";

    public static final String TYPE_RELATION = "relation";
    //ping类型
    public static final String TYPE_PING = "ping";
    //种类 在线状态
    public static final String CATEGORY_ONLINE_SATE = "onlineState";
    //种类 解绑
    public static final String CATEGORY_BIND_STATE = "bindState";
    //种类 发送消息
    public static final String CATEGORY_COMMAND = "command";
    //种类 接收消息
    public static final String CATEGORY_COMMAND_RECV = "command_recv";
    //种类 发送第三方消息
    public static final String CATEGORY_CUSTOM_CMD = "custom_cmd";
    //种类 接收第三方消息
    public static final String CATEGORY_CUSTOM_RECV = "custom_recv";
    //种类 图片
    public static final String CATEGORY_PHOTO = "photo";
    //种类 舞蹈
    public static final String CATEGORY_DANCE = "dance";
    //种类 锻炼
    public static final String CATEGORY_WORKOUT = "workout";

    public static final String COMPANY_NAME = "UBT";

    public static final String PROJECT = "ALEXA";

    public static final String CLIENT_VERSION ="1.1.2.6";

    public static final String CLIENT_TYPE ="2";

    public static final String PROTOCOL_VERSION ="V1.0";

    public static final String BIND_STATE = "1";

    public static final String UNBIND_STATE = "0";

    public static final String BIND = "bind";

    public static final String UNBIND = "unbind";

    public static final int HEART_MESSAGE_TIME = 10 * 1000;

    public static final int INTERVAL_GETROBOT_INFO = 5 * 1000;

    private static final int BASENUMBER = 1000;

    public static final int QUERYROBOTID_MESSAGE_ID = BASENUMBER + 1;

    public static final int SENDHEART_MESSAGE_ID = BASENUMBER + 2;

    public static final int GETOFFLINE_MESSAGE_ID = BASENUMBER + 3;


}
