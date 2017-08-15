/*
    Launch Android Client, KeyConstant
    Copyright (c) 2014 LAUNCH Tech Company Limited
    http:www.cnlaunch.com
 */

package com.ubtechinc.alpha2ctrlapp.constants;

/**
 * [请求路径key常量类]
 *
 * @author nxy
 * @version 1.0
 * @date 2015-9-6
 **/
public class NetWorkConstant {
    /*******************
     * 网络请求的action
     *******************/
    //登录
    public static final String login = "user/login";
    //注册
    public static final String register = "user/register";
    public static final String user_find = "user/find";
    //登出
    public static final String loginout = "user/loginOut";
    //重置密码
    public static final String reset_psw = "user/rpasswordPhone";
    //绑定设备
    public static final String bind = "relation/add";
    public static final String authorize = "relation/auto";
    public static final String find = "relation/find";
    //更新设备
    public static final String accept = "relation/update";
    //删除设备
    public static final String delete = "relation/delete";
    //自己删除授权设备
    public static final String deleteMyDevice = "relation/deleteMyself";
    public static final String verifycode = "system/verify";
    public static final String getMsgcode = "system/getSmsCode";
    public static final String verifymsgcode = "system/verifySms";
    public static final String check = "relation/check";
    public static final String release = "relation/remove";
    public static final String getapplstByPage = "app/accessGetListByPage";
    //    public static final String checkappupdate ="app/accessversion";
    public static final String find_app_detail = "app/find";
    public static final String find_psw = "user/fopassword";
    public static final String register_robot = "user/registerRobot";
    public static final String update_friend = "relation/updateFriend";
    public static final String app_detail = "app/accessAppDetail";
    public static final String app_search = "app/accessfilter";
    public static final String app_search_new = "app/fillter";

    //查询消息，目前只包含授权和系统消息
    public static final String notice_find = "notice/find";
    //删除消息
    public static final String notice_delete = "notice/delete";
    //查询消息，目前只包含授权和系统消息
    public static final String notice_img_find = "system/findUserImage";


    public static final String request_key_value = "UBTech832%1293*6";
    public static final String check_front_action_page = "recommend/find";
    public static final String check_last_action = "action/last";
    public static final String check_ation_byType = "action/getListByPage";
    public static final String check_action_type = "system/dictParam";
    public static final String check_action_detail = "action/detail";
    public static final String check_action_comment = "comment/getListByPage";
    public static final String add_action_comment = "comment/add";
    public static final String zan_action = "action/praise";
    public static final String collect = "collect/add";
    public static final String check_collect = "collect/find";
    public static final String app_update = "version/check";
    public static final String user_edit = "user/edit";
    public static final String add_app_comment = "comment/accessCommmentApp";
    //获取App评论
    public static final String find_app_comment = "comment/accessGetListByPage";
    public static final String cancel_collect = "collect/cancel";
    //获取离线消息
    public static final String get_offlinemsg = "user/getOffline";
    public static final String action_search = "action/filter";
    public static final String third_login = "user/otherUser";
    public static final String check_sn = "system/desUtil";
    public static final String get_weixin_login_info = "oauth2/access_token";
    public static final String get_weixin_user_info = "userinfo";
    public static final String get_share_url = "system/getShare";
    public static final String feed_back = "system/feedback";
    //查询绑定过的Alpha设备
    public static final String find_robot = "relation/findRobot";
    public static final String edit_robot = "user/editRobot";
    public static final String get_history = "relation/getHistory";
    public static final String get_robot_info = "user/findRobot";
    public static final String reset_robot = "relation/reset";
    public static final String getImage = "system/findRobotImage";
    public static final String deleteImage = "system/deleteRobotImage";
    public static final String getActionById = "action/detailById";
    public static final String getOpenFireIp = "system/getOpenfireIp";

    //public static final String refreshdownloadapp="app/accessdownloadapp";
    public static final String refreshdownloadapp = "app/addDownloadTime";
    public static final String thirdVerify = "sdk/verify";
    public static final String thirdGetAccessToken = "sdk/getAccessToken";
    //新开发者平台，获取App列表
    public static final String getApplist = "app/findHost";
    //新开发者平台，获取App和其关联的App（机器人端关联手机端）详情
    public static final String getAppAndLinkedAppDetail = "app/findLinkApp";

    public static final String checkappupdate = "app/findAppInfo";

    /**
     * 查询多语言接口
     **/
    public static final String getLanguage = "theme/getThemeInfo";
    /**
     * 网络响应消息结果
     **/
    public static final int RESPONSE_ISBIND = 10001;
    public static final int RESPONSE_NOT_BIND = 10002;
    public static final int RESPONSE_BINDED_SUCCESS = 10003;
    public static final int RESPONSE_BINDED_FAILED = 10004;
    /**
     * app更新响应
     **/
    public static final int RESPONSE_APPUPDATE = 10005;

    public static final int RESPONSE_SEARCH_SUCCESS = 10006;
    public static final int RESPONSE_SEARCH_FAILED = 10007;
    public static final int RESPONSE_AUTHORIZE_FAILED = 10008;
    public static final int RESPONSE_AUTUORIZE_SUCCESS = 10009;
    public static final int REQPONSE_FIND_QUTHORIZE_SUCCESS = 10010;
    public static final int RESPONSE_CHECK_AUTHORIZE = 10011;
    public static final int RESPONSE_ACCEPT_QUTHORIZE_SUCCESS = 10012;
    public static final int RESPONSE_DELETE_AUTHORIZE_SUCCEES = 10013;
    public static final int RESPONSE_NEED_AUTHOTIZE = 10014;
    public static final int RESPONSE_VERIFICODE_SUCCESS = 10015;
    public static final int RESPONSE_VERIFICODE_FAILED = 10016;
    public static final int RESPONSE_VERIFY_MSG_CODE_FAILED = 10017;
    public static final int RESPONSE_VERIFY_MSG_CODE_SUCCESS = 10018;
    public static final int RESPONSE_GET_MSG_CODE_FAILED = 10019;

    public static final int RESPONSE_GET_MSG_CODE_SUCCESS = 10020;
    public static final int RESPONSE_RELEASE_SUCCESS = 10021;
    public static final int RESPONSE_RELEASE_FAILED = 10022;

    public static final int RESPONSE_GET_APP_LIST_FAILED = 10023;
    public static final int RESPONSE_GET_APP_LIST_SUCCESS = 10024;
    public static final int RESPONSE_GET_LAST_ACTION_SUCCESS = 10025;
    public static final int RESPONSE_GET_LAST_ACION_FAILED = 10026;
    public static final int RESPONSE_GET_ACTION_FRONT_PIC_SUCCESS = 10027;
    public static final int RESPONSE_GET_ACTION_FRONT_PIC_FAILED = 10028;
    public static final int RESPONSE_GET_ACTION_TYPE_SUCCESS = 10029;
    public static final int RESPONSE_GET_ATION_LIST_BY_TYPE_SUCCESS = 10030;
    public static final int RESPONSE_GET_ACTION_DETAIL_SUCCSS = 10031;
    public static final int RESPONSE_GET_ACTION_TYPE_FAILED = 10032;
    public static final int RESPONSE_GET_ACTION_DETAIL_FAILED = 10033;
    public static final int RESPONSE_GET_ACTION_LIST_BY_TYPE_FAILED = 10034;
    public static final int RESPONSE_CHECK_ACTION_COMMENT_FAILED = 10035;
    public static final int RESPONSE_CHECK_ACTION_COMMENT_SUCCESS = 10036;
    public static final int RESPONSE_ADD_ACTION_COMMENT_FAILED = 10037;
    public static final int RESPONSE_ADD_ACTION_COMMENT_SUCCESS = 10038;
    public static final int RESPONSE_ZAN_ACTION_SUCCESS = 10039;
    public static final int RESPONSE_ZAN_ACTION_FAILED = 10040;
    public static final int RESPONSE_FIND_APP_DETAIL_SUCCESS = 10041;
    public static final int RESPONSE_FIND_APP_DETAIL_FEAILED = 10042;
    public static final int RESPONSE_COLLECT_SUCCESS = 10043;
    public static final int RESPONSE_COLLECT_FAILED = 10044;
    public static final int RESPONSE_CHECK_COLLECT_SUCCESS = 10045;
    public static final int RESPONSE_CHECK_COLLECT_FAILED = 10046;
    public static final int RESPONSE_CHECK_APP_UPDATE_SUCCESS = 10047;
    public static final int RESPONSE_CHECK_APP_UPDATE_FAILED = 10048;
    public static final int RESPONSE_EDIT_USER_SUCCESS = 10049;
    public static final int RESPONSE_EDIT_USER_FAILED = 10050;
    public static final int RESPONSE_LOGIN_SUCCESS = 10051;
    public static final int RESPONSE_LOGIN_FAILED = 10052;
    public static final int RESPONSE_APP_DETAIL_SUCCESS = 10053;
    public static final int RESPONSE_APP_DETAIL_FAILED = 10054;
    public static final int RESPONSE_ADD_APP_COMMENT_SUCCESS = 10055;
    public static final int RESPONSE_ADD_APP_COMMENT_FAILED = 10056;
    public static final int RESPONSE_GET_APP_COMMENT_SUCCESS = 10057;
    public static final int RESPONSE_GET_APP_COMMENT_FAILED = 10058;
    public static final int RESPONSE_FIND_PSW_SUCCESS = 10059;
    public static final int RESPONSE_FIND_PSW_FAILED = 10060;
    public static final int RESPONSE_RESET_PSW_SUCCESS = 10061;
    public static final int RESPONSE_RESET_PSW_FAILED = 10062;
    public static final int RESPONSE_LOGINOUT_SUCCESS = 10063;
    public static final int RESPONSE_LOGINOUT_FAILED = 10064;
    public static final int RESPONSE_APP_UPDATE_SUCCESS = 10065;
    public static final int RESPONSE_APP_UPDATE_FAILED = 10066;
    public static final int RESPONSE_COLLECT_REPEATE = 10067;
    public static final int RESPONSE_UPDATE_FRIEND_SUCCESS = 10068;
    public static final int RESPONSE_UPDATE_FRIEND_FAILED = 10069;
    public static final int RESPONSE_GET_SHARE_URL_SUCCESS = 10070;
    public static final int RESPONSE_GET_SHARE_URL_FAILED = 10071;

    /**
     * 登录提示
     */
    public static final int RESPONSE_LOGIN_XMPP_SECCESS = 10068;// xmpp 登录成功
    public static final int RESPONSE_LOGIN_XMPP_HAS_NEW_VERSION = 10069;// 发现新版本
    public static final int RESPONSE_LOGIN_XMPP_IS_NEW_VERSION = 10070;// 当前版本为最新
    public static final int RESPONSE_LOGIN_XMPP_LOGIN_ERROR_ACCOUNT_PASS = 10071;// 账号或者密码错误
    public static final int RESPONSE_LOGIN_XMPP_SERVER_UNAVAILABLE = 10072;// 无法连接到服务器
    public static final int RESPONSE_LOGIN_XMPP_LOGIN_ERROR = 10073;// 连接失败
    public static final int RESPONSE_CONTROL_ROBOT_FAIELD = 10074; // 连接机器人失败
    public static final int RESPONSE_CONTROL_ROBOT_SUCCESS = 10075; // 连接机器人成功
    public static final int RESPONSE_STOP_ROBOT_FAILED = 10076;  // 断开机器人失败
    public static final int RESPONSE_STOP_ROBOT_SUCCEE = 10077;  // 断开机器人成功
    public static final int RESPONSE_CONTROL_ROBOT = 10078;     // 控制机器人的响应
    public static final int RESONSE_ROBOT_STATUS_CHANGE = 10079; // 机器人状态改变

    public static final int RESPONSE_NO_OFFLINE_MSG = 10080; // 获取完离线消息
    public static final int RESONSE_CHECK_ROBOT = 10081; // 检测完权限
    public static final int RESPONSE_GET_OFFLINE_QUTHOTIZE_MSG = 10082;// 获取到离线授权消息
    public static final int RESPONSE_REGISTE_ROBOT_SUCCESS = 10083;// 注册机器人成功
    public static final int RESPONSE_REGISTE_ROBOT_FAILED = 10084; // 注册机器人失败
    public static final int RESPONSE_CANCEL_COLLECT_SUCCESS = 10084; //取消收藏成功
    public static final int RESPONSE_CANCEL_COLLECT_FAILED = 10085;  // 取消收藏失败
    public static final int RESPONSE_GET_OFFLINE_MSG_SUCCESS = 10086;// 获取离线消息成功
    public static final int RESONSE_GET_OFFLINE_MSG_FAILED = 10087;  //获取离线消息失败
    public static final int RESPONSE_ACTION_SEARCH_SUCCESS = 10089;// 动作搜索成功
    public static final int RESPONSE_ACITON_SEARCH_FAILED = 10090;// 动作搜索失败
    public static final int RESPONSE_APP_SEARCH_SUCCESS = 10070;// app 搜索成功
    public static final int RESPONSE_APP_SEARCH_FAILED = 10071;// app 搜索失败
    public static final int RESPONSE_THIRD_LOGIN_FAILED = 10072;// 第三方登录失败
    public static final int RESPONSE_THIRD_LOGIN_SUCCESS = 10073;// 第三方登录成功
    public static final int RESPONSE_CHECK_SN_SUCCESS = 10074;// 检测sn 成功
    public static final int RESPONSE_CHECK_SN_FAILED = 10075;// 检测sn 失败
    public static final int RESPONSE_GET_WEIXIN_INFO_SUCCESS = 10076;// 获取微信登录消息成功
    public static final int RESONSE_GET_WEIXIN_INFO_FAILED = 10077; // 获取微信登录消息失败
    public static final int RESPONSE_GET_WEIXIN_USER_INFO_SUCCESS = 10078; // 获取微信用户信息成功
    public static final int RESPONSE_GET_WEIXIN_USER_INFO_FAILED = 10079; // 获取微信用户信息失败
    public static final int RESPONSE_FEEDBACK_SUCCESS = 10080;// 用户反馈成功
    public static final int RESPONSE_FEEDBACK_FAILED = 10081;// 用户反馈失败
    public static final int RESPONSE_EDIT_ROBOT_SUCCESS = 10082;// 编辑机器人资料成功
    public static final int RESPONSE_EDIT_ROBOT_FAILED = 10083;// 编辑机器人资料失败
    public static final int RESPONSE_GET_HISTORY_SUCCESS = 10084;// 获取历史绑定记录成功
    public static final int RESPONSE_GET_HISTORY_FAILED = 10085;// 获取历史绑定记录失败
    public static final int RESPONSE_GET_ROBOT_INFO_SUCCESS = 10086;//获取机器人信息成功
    public static final int RESPONSE_GET_ROBOT_INFO_FAILED = 10087;//获取机器人信息失败
    public static final int RESPONSE_GET_IMAGE_SUCCESS = 10089;//
    public static final int RESPONSE_GET_IMAGE_FAILED = 10090;
    public static final int RESPONSE_IMAGE_DELETE_SUCCESS = 10091;
    public static final int RESPONSE_IMAGE_DELETE_FAILED = 10092;
    public static final int RESPONSE_GET_ACTION_BY_ID_SUCCESS = 10100;
    public static final int RESPONSE_GET_ACTION_BY_ID_FAILED = 10101;

    public static final int RESPONSE_GET_NOTICIMG_SUCCESS = 10201;//获取照片消息成功


    public static final int RESPONSE_GET_LANGUAGE_SETTING_SUCCESS = 10102;
    public static final int RESPONSE_GET_LANGUAGE_SETTING_FAILED = 10101;
    //查询消息中心所有消息
    public static final int RESPONSE_GET_MESSAGE_SUCCESS = 10103;
    public static final int RESPONSE_GET_MESSAGE_FAILED = 10104;
    //删除消息
    public static final int RESPONSE_DELETE_MESSAGE_SUCCESS = 10105;
    public static final int RESPONSE_DELETE_MESSAGE_FAILED = 10106;

    public static final int RESPONSE_GET_OPENFIRE_IP_SUCCESS = 10107; // 获取openfire ip 成功
    public static final int RESPONSE_GET_OPENFIRE_IP_FAILED = 10108; // 获取openfire ip 失败
    public static final int RESONSE_CHECK_AFTER_BOUND_SUCCESS = 10109; // 绑定之后刷新数据AI
    public static final int RESONSE_CHECK_AFTER_BOUND_FAILED = 10110; // 绑定之后刷新数据失败
    public static final int RESPONSE_THIRD_VERIFY_SUCCESS = 10111; // 第三方验证成功
    public static final int RESPONSE_THIRD_VERIFY_FAILED = 10112; // 第三方验证 失败
    public static final int RESPONSE_THIRD_GET_ACCESS_TOKEN_SUCCESS = 10113; // 第三方获取Token 成功
    public static final int RESPONSE_THIRD_GET_ACCESS_TOKEN_FAILED = 10114; // 第三方获取Token 失败

    /**
     * 网络请求任务
     **/
    public static final int REQUEST_LOGIN_TASK = 20001;
    public static final int REQUEST_REGIST_TASK = 20002;
    public static final int REQUEST_FIND_PSW_TASK = 20003;
    public static final int REQUEST_CHECK_BIND_TASK = 20004;
    public static final int REQUEST_DO_BIND_TASK = 20005;
    /**
     * app更新请求
     **/
    public static final int REQUEST_APP_UPDATE = 20006;

    public static final int REQUEST_SEARCH = 20007;
    public static final int REQUEST_AUTHORIZE = 20008;
    public static final int REQUEST_FIND_QUTHORIZE = 20009;
    public static final int REQUEST_CHECK_AUTHORIZE = 20010;
    public static final int REUQEST_ACCEPT_QUTHORIZE = 20011;
    public static final int REUQEST_DELETE_AUTHORIZE = 20012;

    public static final int REQUEST_VERIFI_CODE = 20013;
    public static final int REQUEST_VERIFY_MSG_CODE = 20014;
    public static final int REQUEST_GET_MSG_CODE = 20015;
    public static final int REQUEST_RELEASE_ALPHA = 20016;
    public static final int REQUEST_GET_APP_LIST = 20017;
    public static final int REQUEST_GET_LAST_ACTION = 20018;
    public static final int REQUEST_GET_ACTION_FRONT_PIC = 20019;
    public static final int REQUEST_CHECK_ACTION_TYPE = 20020;
    public static final int REQUEST_CHECK_ACTION_BY_TYPE = 20021;
    public static final int REQUEST_CHECK_ACTION_DETAIL = 20022;
    public static final int REQUEST_CHECK_ACTION_COMMENT = 20023;
    public static final int REQUEST_ADD_ACTION_COMMENT = 20024;
    public static final int REQUEST_ZAN_ACTION = 20025;
    public static final int DO_UPDATE_VIDEO_TIME = 20026;
    public static final int REQUEST_FIND_APP_DETAIL = 20027;
    public static final int REQUEST_COLLECT = 20028;
    public static final int REQUEST_CHECK_COLLECT = 20029;
    public static final int REQUEST_CHECK_APP_UPDATE = 20030;
    public static final int REQUEST_EDIT_USER = 20031;
    public static final int REQUEST_GET_APP_DETAIL = 20032;
    public static final int RESUEST_ADD_APP_COMMENT = 20033;
    public static final int REQUEST_GET_APP_COMMENT = 20034;
    public static final int REQUEST_FIND_PSW = 20035;
    public static final int REQUEST_RESET_PSW = 20036;
    public static final int REQUEST_LOGIN_OUT = 20037;
    public static final int REQUEST_LOGIN_XMPP = 20038;
    public static final int REQUEST_REGIST_ROBOT = 20039;
    public static final int REQUEST_UPDATE_FRIEND = 20040;
    public static final int REQUEST_CANCEL_COLLECT = 20041;
    //获取服务端端缓存的未读消息
    public static final int REQUEST_GET_OFFLINE_MSG = 20042;
    public static final int REQUEST_ACION_SEARCH = 20043;
    public static final int REQUEST_APP_SEARCH = 20044;
    public static final int REQUEST_THIRD_LOGIN = 20045;
    public static final int REQUEST_CHECK_SN = 20046;
    public static final int REQUEST_GET_WEIXIN_LOGIN_INFO = 20047;// 获取微信登录信息
    public static final int REQUEST_GET_WEIXIN_USER_INFO = 20048;//获取微信用户信息
    public static final int REQUEST_GET_SHARE_URL = 20049;// 获取分享地址
    public static final int REQUEST_FEEDBACK = 20050;  // 用户反馈
    public static final int REQUEST_EDIT_ROBOT = 20051; // 编辑机器人资料
    public static final int REQUEST_GET_HISTORY = 20052;// 获取历史绑定记录
    public static final int REQUEST_GET_ROBOT_INFO = 20053;// 获取机器人信息
    public static final int RESPONSE_GET_OFFLINE_QUTHOTIZE_MSG_ERROR = 20054;// 欢迎界面获取离线消息出现问题
    public static final int REQUEST_RESET_ROBOT = 20054; // 重置机器人
    public static final int RESPONSE_RESET_ROBOT_SUCCESS = 20055;//重置机器人成功
    public static final int RESPONSE_RESET_ROBOT_FAILED = 20056; // 重置机器人失败
    public static final int RESPONSE_CHECK_ACTION_BY_TYPE_SUCCESS = 20057;
    public static final int RESPONSE_CHECK_ACTION_BY_TYPE_FAILED = 20058;
    public static final int REFRESH_AUTHOURIZE = 20059; // 刷新授权用户
    public static final int REFRESH_PHOTO = 20060;
    public static final int REQUEST_GET_IMAGE = 20061;
    public static final int REQUEST_DELETE_IMAGE = 20062;
    public static final int RESPONSE_DEVICE_STATUS = 20063;
    public static final int REQUEST_GET_ACTION_BY_ID = 20064;
    public static final int REQUEST_GET_LANGUAGE_SETTING = 20065;// 请求获取多语言
    public static final int REQUEST_GET_NOTICE_MESSAGE = 20066;// 查询消息
    public static final int REQUEST_DELETE_NOTICE_MESSAGE = 20067;//删除消息
    public static final int REUQEST_DELETE_MY_AUTHORIZE = 20068;//删除我的授权
    public static final int RESPONSE_SEVERSE_NO_RESPONSE = 20069;// 服务器未响应

    public static final int REQUEST_REFRESG_DOWNLOAD = 20070;//更新下载次数

    public static final int REUQEST_THIRD_VERIFY = 20071;//第三方授权
    public static final int REUQEST_THIRD_GET_ACCESS_TOKEN = 20072;//第三方获取Token
    public static final int REQUEST_GET_NOTICE_IMG_MESSAGE = 20073;// 查询图片消息
    public static final int REQUEST_GET_JPUSH_NOTICE_IMG_MESSAGE = 20074;// 进入消息页面图片消息


}
