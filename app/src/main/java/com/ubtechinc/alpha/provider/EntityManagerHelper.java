/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */
package com.ubtechinc.alpha.provider;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description 数据库管理基本数据定义
 * @modifier logic.peng
 * @modify_time 2017/5/4
 */

public class EntityManagerHelper {

	public static final int DB_VERSION = 5;
	public static final String DB_ACCOUNT = "coreservice";

	public static final int DB_ACTION_INFO_VERSION = 2;
	public static final String DB_ACTION_INFO_TABLE = "action_info_table";

	public static final int DB_ALARM_INFO_VERSION = 2;
	public static final String DB_ALARM_INFO_TABLE = "alarm_info_table";

	public static final int DB_APP_INFO_VERSION = 3;
	public static final String DB_APP_INFO_TABLE = "app_info_table";

	public static final int DB_PHOTO_INFO_VERSION = 2;
	public static final String DB_PHOTO_INFO_TABLE = "photo_info_table";
}
