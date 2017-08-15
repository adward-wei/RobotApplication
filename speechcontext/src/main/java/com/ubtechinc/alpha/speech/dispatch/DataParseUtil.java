/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据解析util类
 *
 * @author Administrator
 */
public class DataParseUtil {
	private DataParseUtil(){}
	/**
	 * 把字符串的数字解析出来
	 *
	 * @param slot
	 * @return appId
	 */
	public static int getAppFromSlot(String slot) {
		int appId = 0;
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(slot);
		if (m.find()) {
			appId = Integer.parseInt(m.group(0));
		}
		return appId;
	}

	/**
	 * 判断是否是正确格式的bnf指令行  正确格式如:<app+数字>:中文1|中文2;
	 *
	 * @return
	 */
	public static boolean isRightFormatCmdLine(String cmdLine) {
		boolean rst = false;
		Pattern p = Pattern.compile("^<app[0-9]{0,}>:.*$");
		Matcher m = p.matcher(cmdLine);
		rst = m.find();
		return rst;
	}

	/**
	 * 获取slotName
	 * @param slotName
	 * @return
	 */
	public static  String  getSlotType(String slotName)
	{
		String slotType="";
		Pattern p = Pattern.compile("[^0-9]{0,}");
		Matcher m = p.matcher(slotName);
		if(m.find()) {
			slotType=m.group(0);
			slotType=slotType.replace("<","").replace(">","");
		}
		return slotType;
	}
}
