/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.sdk;

/**
 * @date 2016/12/26
 * @author paul.zhang@ubtrobot.com
 * @Description 用于常量等基本定义
 * @modifier logic.peng
 * @modify_time 2017/4/17
 */

public class SdkConstants {
	public static final String ACTION_CHANGE_SAVEPOWER_MODE = "com.ubtechinc.services.Action.SAVEPOWER_CHANGED";
	public static final String ACTION_ALPHA_INTERRUPT_BUSINESS = "com.ubtechinc.services.Action.ROBOT_INTERRUPTED";

	public static final String ALPHA_PACKAGE_NAME = "com.ubtechinc.alpha2services";

	public static final String ACTION_BINDER_NAME="action";
	public static final String SPEECH_BINDER_NAME="speech";
	public static final String LED_BINDER_NAME="led";
	public static final String MOTOR_BINDER_NAME="motor";
	public static final String SYSINFO_BINDER_NAME = "sysinfo";

	public class ErrorCode{
		public static final int DEVICE_ERR_BUSY = -1001;//串口繁忙
		public static final int DEVICE_ERR_WRITE_FAIL = -1002;//写失败
		public static final int DEVICE_ERR_IO = -1003; //IO错误
		public static final int DEVICE_ERR_UNKNOW = -1004;//未知错误
		public static final int DEVICE_ERR_UNDO = -1005; //撤销

		public static final int ACTION_INTERRUPTED = -8;
		public static final int ROBOT_STATE_NOT_SUPPORTED = -7;
		public static final int PARAMETER_ERROR = -6;
		public static final int DEVICE_NOT_SUPPORTED = -5;
		public static final int DEVICE_NOT_INITED = -4;
		public static final int SERVICE_REMOTE_EXCEPTION = -3;
		public static final int SERVICE_UNBINDED = -2;
		public static final int RESULT_FAIL = -1;
		public static final int RESULT_SUCCESS = 0;
	}

}
