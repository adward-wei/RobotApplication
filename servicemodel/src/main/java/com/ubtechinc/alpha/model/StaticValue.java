package com.ubtechinc.alpha.model;

/**
 * #####主服务和ubt应用之间使用的常量####
 *
 * @date 2017/07/26
 * @author wzx@ubtrobot.com
 * @Description 用于常量等基本定义
 * @modifier logic.peng@ubtrobot.com
 * @modify_time
 */
public class StaticValue {
	/** 停止二维码扫面 **/
	public static final String ALPHA_QR_CODE_CANCLE = "com.ubt.alpha2.qr_code.cancle";
	/** 拍完照后请求将照片发送到客户端 **/
	public final static String UPLOAD_PHOTO_BY_SERVICE = "com.ubtrobot.action.transfer_photo";
	/** 照片的存储路径 **/
	public final static String PHOTO_PATH_KEY = "photo_path";

	public static final String SCHEME = "ubtechinc";
	public static final String HOST = "com.ubtechinc";

	/**==============begin:ubtech 内置应用的包名====================**/
	public static final String ALPHA_PACKAGE_NAME = "com.ubtechinc.alpha2services";
	public static final String CHAT_PACKAGE_NAME = "com.ubtech.iflytekmix";
	public static final String TRANSLATION_PACKAGE_NAME = "com.ubtechinc.alphatranslation";
	public static final String SMARTCAMERA_PACKAGE_NAME = "om.ubtech.smartcamera";
	/**==============end:ubtech 内置应用的包名====================**/


	/**==============begin:内置应用间通过广播字段====================*/
	public static final String ACTION_UBT_APP_EXIT = "com.ubtechinc.action.closeapp";
	public static final String ACTION_REPLAY_BUSINESS = "com.ubtechinc.services.REPLAY_BUSINESS";
	/** app ---> server---->third party read it's app config **/
	public static final String APP_CONFIG = "appconfig";
	/** third party --->server --->app for read app config **/
	public static final String APP_CONFIG_BACK = "appconfigback";
	/** app--->server--->third party save it's app config **/
	public static final String APP_CONFIG_SAVE = "appconfigsave";
	/** server--->read third party app button event config **/
	public static final String APP_BUTTON_EVENT = "buttonevent";
	/** third party app button event--->server---> app **/
	public static final String APP_BUTOON_EVENT_BACK = "buttonback";
	/** app--->server--->third party app button click **/
	public static final String APP_BUTOON_EVENT_CLICK = "buttonclick";
	/**==============end:内置应用间通过广播字段====================*/
}
