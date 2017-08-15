/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.OfflineSlotEvent;
import com.ubtechinc.alpha.model.speech.SlotValue;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.serverlibutil.aidl.IPcmListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechAsrListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechGrammarInitListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechInterface;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechWakeUpListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.speechioc.SpeechApiInjector;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;
import com.ubtechinc.alpha.utils.ActionUtil;
import com.ubtechinc.alpha.utils.SharedPreferenceUtil;
import com.ubtechinc.alpha2services.BuildConfig;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/12/27
 * @Description 语音引擎对外接口实现
 * @modifier  logic.peng
 * @modify_time 2017/4/7 增加唤醒回调api
 */

public class SpeechServicesImpl implements ISpeechInterface {
	private static final String TAG="SpeechServicesImpl";
	SpeechContext mSpeechCoreProxy;
	private  Context mContext;
	private Lock mAccesslock = new ReentrantLock();
	private String mLanguage ;

	@Override
	public IBinder asBinder() {
		return null;
	}

	/**不允许package外面的类调用*/
	SpeechServicesImpl() {
		mContext = AlphaApplication.getContext();
		if (TextUtils.isEmpty(BuildConfig.SpeechEngine)) {
			loadCore();
		}else {
			mSpeechCoreProxy = new SpeechApiInjector(mContext).provideSpeechApi();
			mSpeechCoreProxy.onCreate();
		}
	}


	private void loadCore() {
		mLanguage ="cn";//默认设置
		loadCore(mLanguage);
	}

	private void loadCore(String language) {
		switch (language) {
			case "en":
				mSpeechCoreProxy = new SpeechApiInjector(mContext).provideSpeechApi("com.ubtechinc.alpha.speech.nuance.NuanceSpeechImpl");
				break;
			case "cn":
				mSpeechCoreProxy = new SpeechApiInjector(mContext).provideSpeechApi("com.ubtechinc.alpha.speech.ifytek.IfytekSpeechImpl");
				break;
		}

	}

	@Override
	public void switchSpeechCore(String language) {
		if (TextUtils.isEmpty(BuildConfig.SpeechEngine)) {
			if (language == null || !(language.equals("cn") || language.equals("en"))) {
				return;
			}

			if (language.equals(mLanguage)) {
				return;
			}
			if (mSpeechCoreProxy != null) {
				mSpeechCoreProxy.onDestroy();
			}
			mLanguage = language;
			loadCore(language);
		}
	}

	@Override
	public int registerPcmListener(final String packageName, final IPcmListener callBack) throws RemoteException {
		int ret = 0;
		mAccesslock.lock();
		try {
			ret = mSpeechCoreProxy.registerPcmListener(packageName, new com.ubtechinc.alpha.speech.recoder.IPcmListener() {
				@Override
				public void onPcmData(byte[] data, int length) {
					if (callBack == null) return;
					try {
						callBack.onPcmData(data, length);
					} catch (RemoteException e) {
						e.printStackTrace();
						mSpeechCoreProxy.unregisterPcmListener(packageName);
					}
				}
			});
		}finally {
			mAccesslock.unlock();
		}
		return ret;
	}

	@Override
	public int unregisterPcmListener(String packageName) throws RemoteException {
		int ret = 0;
		mAccesslock.lock();
		try {
			ret = mSpeechCoreProxy.unregisterPcmListener(packageName);
		}finally {
			mAccesslock.unlock();
		}
		return ret;
	}

	@Override
	public int registerWakeUpCallbackListener(final String packageName, final ISpeechWakeUpListener callback) {
		int nRet = 0;
		mAccesslock.lock();
		try {
			nRet = mSpeechCoreProxy.registerWakeUpCallbackListener(packageName, new IWakeuperListener() {
				@Override
				public void onWakeup(String resultStr, int soundAngle) {
					if (callback == null) return;
					try {
						callback.onSuccess();
					} catch (RemoteException e) {
						e.printStackTrace();
						mSpeechCoreProxy.unregisterWakeUpCallbackListener(packageName);
					}
				}

				@Override
				public void onError(int errCode) {
					if (callback == null) return;
					try {
						callback.onError(errCode, "");
					} catch (RemoteException e) {
						e.printStackTrace();
						mSpeechCoreProxy.unregisterWakeUpCallbackListener(packageName);
					}
				}

				@Override
				public void onAudio(byte[] data, int dataLen, int param1, int param2) {

				}
			});
		}finally {
			mAccesslock.unlock();
		}
		return nRet;
	}

	@Override
	public int unregisterWakeUpCallbackListener(String packageName) throws RemoteException {
		int ret = 0;
		mAccesslock.lock();
		try {
			ret = mSpeechCoreProxy.unregisterWakeUpCallbackListener(packageName);
		}finally {
			mAccesslock.unlock();
		}
		return ret;
	}

	@Override
	public int onPlayCallback(String packageName, String text, final ITtsCallBackListener callback) throws RemoteException {
		int ret = 0;
		mAccesslock.lock();
		try {
			boolean isOpenTTSAction=SharedPreferenceUtil.readBoolean(AlphaApplication.getContext(),"is_open_TTS_action");
			if(isOpenTTSAction && !packageName.equals(mContext.getPackageName())){
				RobotOpsManager.get(mContext).playAction(ActionUtil.getRandomAction(text), null);
			}
			ret = mSpeechCoreProxy.onPlay(packageName, text, new ITTSListener() {
				@Override
				public void onTtsBegin() {

				}

				@Override
				public void onTtsCompleted(int errCode) {
					if (callback == null) return;
					try {
						callback.onEnd();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
		} finally {
			mAccesslock.unlock();
		}
		return ret;
	}


	@Override
	public void onStopPlay(){
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.onStopPlay();
		} finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public void setVoiceName(String strVoiceName)  {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.setVoiceName(strVoiceName);
		} finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public void setTtsSpeed(String speed)  {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.setTtsSpeed(speed);
		} finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public String getTtsSpeed() throws RemoteException {
		return mSpeechCoreProxy.getTtsSpeed();
	}

	@Override
	public void setTtsVolume(String volume)  {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.setTtsVolume(volume);
		} finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public String getTtsVolume() throws RemoteException {
		return mSpeechCoreProxy.getTtsVolume();
	}

	@Override
	public void startSpeechAsr(String packageName, int appId, final ISpeechAsrListener callBack) throws RemoteException {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.startSpeechAsr(packageName,appId, new IRecognizerListener() {
				@Override
				public void onBegin() {
					if (callBack == null) return;
					try {
						callBack.onBegin();
					} catch (RemoteException e) {
						e.printStackTrace();
						handleAsrRemotedException();
					}
				}

				@Override
				public void onEnd() {
					if (callBack == null) return;
					try {
						callBack.onEnd();
					} catch (RemoteException e) {
						e.printStackTrace();
						handleAsrRemotedException();
					}
				}

				@Override
				public void onResult(String result, boolean b) {
					if (callBack == null) return;
					try {
						callBack.onResult(result);
					} catch (RemoteException e) {
						e.printStackTrace();
						handleAsrRemotedException();
					}
				}

				@Override
				public void onError(int errCode) {
					if (callBack == null) return;
					try {
						callBack.onError(errCode);
					} catch (RemoteException e) {
						e.printStackTrace();
						handleAsrRemotedException();
					}
				}
			});
		} finally {
			mAccesslock.unlock();
		}
	}

	private final void handleAsrRemotedException() {
		if (mSpeechCoreProxy instanceof SpeechProxy){
            ((SpeechProxy) mSpeechCoreProxy).removeAsrListener();
        }else if (mSpeechCoreProxy instanceof SpeechContextBase){
			((SpeechContextBase)mSpeechCoreProxy).getRecUtil().setListener(null);
		}
	}

	@Override
	public void stopSpeechAsr() throws RemoteException {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.stopSpeechAsr();
		} finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public List getSpeechVoices()  {
		List ret = null;
		mAccesslock.lock();
		try {
			ret = mSpeechCoreProxy.getSpeechVoices();
		} finally {
			mAccesslock.unlock();
		}
		return ret;
	}

	@Override
	public SpeechVoice getCurSpeechVoices()  {
		SpeechVoice ret = null;
		mAccesslock.lock();
		try {
			ret = mSpeechCoreProxy.getCurSpeechVoices();
		} finally {
			mAccesslock.unlock();
		}
		return ret;
	}

	@Override
	public void initSpeechGrammar(String strGrammar, final ISpeechGrammarInitListener listener) {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.initSpeechGrammar(strGrammar, new IGrammarListener() {
				@Override
				public void onBuildFinish(String grammarId, int errCode) {
					if (listener == null) return;
					try {
						listener.speechGrammarInitCallback(grammarId, errCode);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
		}finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public void switchWakeup(boolean enable) {
		mAccesslock.lock();
		try{
			mSpeechCoreProxy.switchWakeup(enable);
		}finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public void startLocalFunction(String function) {
		mAccesslock.lock();
		try {
			final String slot = "<"+function+">";
			@SlotValue.Type int type = SlotValue.valueToType(function);
			OfflineSlotEvent event = new OfflineSlotEvent();
			event.slot = type;
			NotificationCenter.defaultCenter().publish(event);
		}finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public boolean isSpeechGrammar() throws RemoteException {
		mAccesslock.lock();
		try {
			return mSpeechCoreProxy.isSpeechGrammar();
		}finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public boolean isSpeechIat() throws RemoteException {
		mAccesslock.lock();
		try {
			return mSpeechCoreProxy.isSpeechIat();
		}finally {
			mAccesslock.unlock();
		}
	}

	@Override
	public void setSpeechMode(int mode) throws RemoteException {
		mAccesslock.lock();
		try {
			mSpeechCoreProxy.setSpeechMode(AbstractSpeech.SpeechMode.valueOf(mode));
		}finally {
			mAccesslock.unlock();
		}
	}
}