package com.alpha2.camera.arcsoft.dam;

/**
 * 虹软 jni
 * @author zengdengyi
 *
 */
public class DamEngine {

	public static native int setModelEnable(int mode);

	public static native int getInfo(int index, byte[] infodata);

	public static native int process(byte[] indata, int format, int width,
			int height, int index,ArcCallBack arcCallBack);

	static {
		System.loadLibrary("arcsoft_handsigns");
		System.loadLibrary("dam");
		System.loadLibrary("mpbase");
		System.loadLibrary("share_vsign");
		System.loadLibrary("shareface_tracking");
	}
}
