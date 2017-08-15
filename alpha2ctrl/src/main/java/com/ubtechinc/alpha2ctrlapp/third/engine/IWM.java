package com.ubtechinc.alpha2ctrlapp.third.engine;

/**
 * 
 * @author jjzhuang
 * date  2014-02-10
 */
public class IWM {	
	
	public final static int MIN_INPUTBUF_WORDS = 288000;
	private static boolean _libLoaded 	= false;
	private static boolean _inited 		= false;
	
	public static int init() {
		if (!_libLoaded) {
			System.loadLibrary("IWM");
			_libLoaded = true;
		}
//		if (!_inited) {
//			int ret = JniCreate();
//			_inited = (ret == 0);
//			return ret;
//		}
		return 0;
	}
	
	/**
	 *  create instance 
	 *  by jjzhuang 2014-02-10
	 * @return if 0 success  else error 
	 */
	public native static int JniCreate();
	
	/**
	 * append audio to engine
	 * @param pData audio buffer 
	 * @param iPos  offset
	 * @param nSize audio size 
	 * @return same as  sdk
	 */
	public native static int JniAppendAudio(int handle, byte[] pData,int iPos,int nSize);
	
	/**
	 * process audio 
	 * @param pReslut  the  reslut of the code min size=45
	 * @return same as sdk
	 */
	public native static int JniRunStep(int handle, int[] pReslut);
	
	public native static int SetFreq(int handle, int freq);
	
	public native static int GenAudio(String str,int len,short[] pAudio,int[] pNAudio,int samperataC,int freqWidth);

}
