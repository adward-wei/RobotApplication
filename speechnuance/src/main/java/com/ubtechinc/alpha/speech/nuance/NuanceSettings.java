/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.nuance;
/**
 * @date 2017/2/25
 * @author paul.zhang@ubtrobot.com
 * @Description nuance引擎参数设置
 * @modifier
 * @modify_time
 */

public class NuanceSettings {

	public final static String HOST = "nmsp.dev.nuance.com";
	public final static int PORT = 443;
	public final static String APPID = "NMDPTRIAL_sw015_ubtrobot_com20160108012526";
	public final static byte[] APPKEY = { (byte) 0xdc, (byte) 0x1a, (byte) 0x95, (byte) 0x5e, (byte) 0x20,
			(byte) 0xaf, (byte) 0xc3, (byte) 0xb2, (byte) 0x2c, (byte) 0x5a, (byte) 0xe6, (byte) 0x4e, (byte) 0xb1,
			(byte) 0x00, (byte) 0xb2, (byte) 0x76, (byte) 0x5f, (byte) 0xcd, (byte) 0xf5, (byte) 0xb4, (byte) 0xfc,
			(byte) 0xff, (byte) 0x16, (byte) 0x80, (byte) 0x04, (byte) 0x04, (byte) 0xbf, (byte) 0xa8, (byte) 0xd8,
			(byte) 0x58, (byte) 0x61, (byte) 0xb4, (byte) 0x72, (byte) 0xf6, (byte) 0x13, (byte) 0x93, (byte) 0xa5,
			(byte) 0xd6, (byte) 0x45, (byte) 0xd1, (byte) 0x30, (byte) 0x2c, (byte) 0x1f, (byte) 0xe1, (byte) 0x2c,
			(byte) 0xd1, (byte) 0x58, (byte) 0x8b, (byte) 0x9e, (byte) 0x4d, (byte) 0x3f, (byte) 0x9a, (byte) 0x4c,
			(byte) 0x56, (byte) 0x1a, (byte) 0x50, (byte) 0x5a, (byte) 0xda, (byte) 0xf8, (byte) 0x32, (byte) 0x32,
			(byte) 0x59, (byte) 0xe7, (byte) 0xa5 };

	public final static String CMD = "NDSP_ASR_APP_CMD";
	public final static String CONTEXT_TAG = "M3564_A565";//M1966_A565
}