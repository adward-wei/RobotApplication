package com.ubtechinc.alpha.sdk.speech;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

public class SpeechUtil {
	public static final String TAG = "sdkThird_one";
	public static final String SPEECH_RST = "speech_rst";
	public static final int MSG_SEND_TO_SERVER = 3;
	//service在前台状态是start
	public static final int SERVICE_STATE_START = 1;
	//service在前台状态是stop
	public static final int SERVICE_STATE_STOP = 2;
	//第三方应用ID
	private int appId;
	//新的语音回调交互上下文
	private ISpeechContext mSpeechContext;
	private static Context mContext;


	private static class SpeechUtilInstance {
		private static final SpeechUtil INSTANCE = new SpeechUtil();

		private SpeechUtilInstance() {
		}
	}

	public static SpeechUtil getInstance(Context context) {
		mContext = context;
		return SpeechUtilInstance.INSTANCE;
	}

	private SpeechUtil() {
	}

	private Handler mInComingHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.replyTo != null) {
				/** 第三方每收到主服务发过来的一条信息就往主服务发信息进行回应 */
				Message mClientMsg = this.obtainMessage();
				try {
					mClientMsg.what = MSG_SEND_TO_SERVER;
					mClientMsg.obj = msg;
					msg.replyTo.send(mClientMsg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

			/** 第三方收到从主服务发送过来的自己对应的信息 */
			if (msg.what == appId) {
				if (mSpeechContext == null) {
					return;
				}

				switch (msg.arg1) {
					case SERVICE_STATE_START:/**前台*/
						mSpeechContext.onStart();
						break;
					case SERVICE_STATE_STOP:/**后台*/
						mSpeechContext.onStop();
						break;
					default:/**语音指令*/
						String speechRst = handleReceiveSpeechRst(msg);
						if (TextUtils.isEmpty(speechRst)) {
							Log.e(TAG, "speechRst haven't been sent here!");
							break;
						}
						mSpeechContext.onResult(speechRst);
						break;
				}
			}
		}

		;
	};

	/**
	 * 处理通过message的bundle
	 *
	 * @param msg 机器端发过来的信息
	 * @return rst    返回语音指令文本结果
	 */
	private String handleReceiveSpeechRst(Message msg) {
		String rst = null;
		Bundle bundle = msg.getData();
		if (bundle != null && bundle.containsKey(SPEECH_RST)) {
			rst = bundle.getString(SPEECH_RST);
		}
		return rst;
	}

	/**
	 * 与主服务发送消息msger
	 */
	private Messenger mMessenger = new Messenger(mInComingHandler);

	/**
	 * 通信binder
	 *
	 * @return
	 */
	public IBinder getIBinder() {
		return mMessenger.getBinder();
	}


	public void registerSpeech(ISpeechContext cb) {
		this.mSpeechContext = cb;
	}

	private ISpeechBindListener mSpeechBindListener;

	/**
	 * speech util初始化方法 bind语音回调通知监听
	 */
	public void initialize(int appId, ISpeechBindListener listener) {
		this.appId = appId;
		this.mSpeechBindListener = listener;
	}

	public ISpeechBindListener getSpeechBindListener() {
		return mSpeechBindListener;
	}

	/**
	 * 分发指令bind监听器
	 *
	 * @author Administrator
	 */
	public interface ISpeechBindListener {
		public void onBindSpeech();
	}

}
