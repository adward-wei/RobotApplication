// ISpeechWakeUpListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

// Declare any non-default types here with import statements

interface ISpeechWakeUpListener {
    /**
	 * @Description 通知第三方应用进入唤醒模式成功的回调，
	 * @param packageName
	 * @return
	 * @throws
	 */
     void onSuccess();
    /**
	 * @Description 通知第三方应用进入唤醒模式失败的回调
	 * @param errCode
	 * @param errDes
	 * @return
	 * @throws
	 */
     void onError(int errCode, String errDes);
}
