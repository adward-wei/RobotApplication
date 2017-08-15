package com.ubtechinc.alpha.speech.nuance;


public class NuanceServerSettings {
/*
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
	public final static String CONTEXT_TAG = "M2097_A565";
*/
	public static String HOST = "ubtech-mix-engusa-ssl.nuancemobility.net"; //"nmsp.dev.nuance.com";
	public final static int PORT = 443;
	public final static String APPID = "NMDPPRODUCTION_UBTech_Robotics_Corp_Robot_Alpha_Series_20160331102343";
	public final static byte[] APPKEY = {(byte) 0xd2, (byte) 0xda, (byte) 0xbf, (byte) 0x5d, (byte) 0x55, (byte) 0x68,
			(byte) 0xd4, (byte) 0xc5, (byte) 0xa9, (byte) 0x8d, (byte) 0xfc, (byte) 0x5a, (byte) 0x1a, (byte) 0x27,
			(byte) 0xe7, (byte) 0xe3, (byte) 0xa8, (byte) 0xc0, (byte) 0x4f, (byte) 0xb5, (byte) 0x39, (byte) 0x58,
			(byte) 0x79, (byte) 0x04, (byte) 0x77, (byte) 0xd0, (byte) 0x6a, (byte) 0xc2, (byte) 0x58, (byte) 0x1b,
			(byte) 0x4e, (byte) 0x2b, (byte) 0xf5, (byte) 0x0d, (byte) 0x88, (byte) 0x4b, (byte) 0x75, (byte) 0x5d,
			(byte) 0x10, (byte) 0x1e, (byte) 0xea, (byte) 0x0c, (byte) 0x78, (byte) 0x04, (byte) 0x33, (byte) 0x60,
			(byte) 0xe7, (byte) 0xdb, (byte) 0x20, (byte) 0x37, (byte) 0x90, (byte) 0x59, (byte) 0x8f, (byte) 0x0a,
			(byte) 0x5e, (byte) 0x06, (byte) 0xda, (byte) 0xc8, (byte) 0xc9, (byte) 0x32, (byte) 0xe3, (byte) 0x17,
			(byte) 0x5d, (byte) 0xd9};
	public final static String CMD = "NDSP_ASR_APP_CMD";
	/** Mix 2.1 **/
//	public final static String CONTEXT_TAG = "M2097_A1150";

	/** Mix 2.3 **/
	public final static String CONTEXT_TAG = "M2271_A1150";

	public final static String HOST_DEV = "nmsp.dev.nuance.com";
	public final static String HOST_BUSINESS = "ubtech-mix-engusa-ssl.nuancemobility.net";
}