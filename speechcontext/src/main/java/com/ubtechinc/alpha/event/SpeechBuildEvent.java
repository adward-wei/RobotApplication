/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.event;
/**
 * @date 2017/1/18
 * @author paul.zhang@ubtrobot.com
 * @Description 通知讯飞离线bnf构建事件
 * @modifier
 * @modify_time
 */

public class SpeechBuildEvent {
	public static final int BUILD_ENENT = 0;
	public String content;
	int in;

	public SpeechBuildEvent(int event, String content) {
		this.in = event;
		this.content = content;
	}
}
