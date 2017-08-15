/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.sys;

import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @date 2017/2/22
 * @author paul.zhang@ubtrobot.com
 * @Description mac地址方式生成唯一标识
 * @modifier
 * @modify_time
 */

public class MacGeneratorImpl implements IDGenerator{

	public static final String ROBOT_CODE = "Cruzr";
	public static final String ROBOT_GENERATOR_TYPE_MAC = "01";
	public static final String ROBOT_GENERATOR_TYPE_UUID = "02";

	@Override
	public String generate() {
		return ROBOT_CODE + "." + ROBOT_GENERATOR_TYPE_MAC + "." + SysUtils.getMac();
	}
}
