package com.ubtechinc.alpha.sdk.speech;

/**
 * 语音上下文
 *
 * @author Administrator
 */
public interface ISpeechContext {
	/**
	 * 第三方语音服务开启到前端
	 */
	public void onStart();

	/**
	 * 第三方语音服务退到后台
	 */
	public void onStop();

	/**
	 * 获取语音指令文本结果方法
	 *
	 * @param speechTxt
	 */
	public void onResult(String speechTxt);
}
