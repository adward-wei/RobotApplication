package com.ubtechinc.alpha2ctrlapp.base;

import android.os.Handler;

public class BaseHandler extends Handler {
	public static final int CHAT_INSERT_FACE = 1;
	public static final int CHAT_SELECT_FACE = 2;
	public static final int CHAT_SELECT_PICTURE = 3;
	public static final int CHAT_SELECT_FILE = 4;
	public static final int CHAT_SELECT_PLACE = 5;
	public static final int CHAT_SEND_MSG_TEXT = 6;
	public static final int CHAT_SEND_MSG_VOICE = 7;
	public static final int CHAT_SEND_MSG_PICTURE = 8;
	public static final int CHAT_SEND_MSG_FILE = 9;
	public static final int CHAT_SEND_MSG_PLACE = 10;
	public static final int CHAT_REQUEST_LOAD = 11;
	public static final int CHAT_NEW_MESSAGE = 12;
	public static final int CHAT_REQUEST_UPDATE = 13;
	public static final int CHAT_SELECT_CARD = 14;
	public static final int CHAT_SELECT_REMOTE = 15;
	public static final int CHAT_SELECT_VEDIO = 16;

	public static final int LOGIN_TO_HTTP_SUCCESSFULLY = 301;
	public static final int LOGIN_TO_HTTP_PASSWORD_ERROR = 302;
	public static final int LOGIN_TO_HTTP_TIME_OUT = 303;
	public static final int LOGIN_TO_HTTP_NO_REGISTERED = 304;
	public static final int LOGIN_TO_HTTP_NO_RESPONSE = 305;


	/**
	 * �?近聊天记�?
	 */
	public static final int LAST_CHAT_LOG = 1001;
	/**
	 * �?近分�?
	 */
	public static final int LAST_SHARE_LOG = 2001;
	/**
	 * 更新好友列表
	 */
	public static final int UPDATE_FRIEND_LIST = 3001;
	/**
	 * 好友验证
	 */
	public static final int ADD_FRIEND_REQUEST = 1002;
	public static final int VERFIY_FRIEND = 1003;
	
}