/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.serverlibutil.service;

import android.content.Context;
import android.os.RemoteException;

import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.IPcmListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechAsrListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechGrammarInitListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechInterface;
import com.ubtechinc.alpha.serverlibutil.aidl.ISpeechWakeUpListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.serverlibutil.interfaces.PcmListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechInitGrammarListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechWakeUpListener;

import java.util.List;

/**
 * @author paul.zhang
 * @Description:语音引擎服务绑定工具类、提供绑定ROS控制服务
 * @date 2016/10/20 14:08
 * @copyright UBTECH
 * @modifier  logic.peng
 * @modify_time 2017/4/7 增加唤醒回调api
 */
public class SpeechMainServiceUtil{
	private Context mContext;
	private ISpeechInterface mService;
	private SpeechTtsCallBackListenerImp.Stub mTtsClientListener;
	private SpeechASRListenerImpl.Stub mAsrClientListener;
	private SpeechWakeUpListenerImpl.Stub mWakeClientListener;
	private SpeechTtsListener mTtsListener;
	private SpeechASRListener mAsrListener;
	private SpeechWakeUpListener mWakeUpListener;

	//初始化语法文件listener
	private SpeechInitGrammarListener mGrammarInitlistener;
	private SpeechGrammarInitListenerImpl.Stub mGrammarClientInitListener;
	private PcmListener mPcmListener;
	private IPcmListener mPcmClientListener;

	public SpeechMainServiceUtil(Context context) {
		this.mContext = context.getApplicationContext();
		mTtsClientListener = new SpeechTtsCallBackListenerImp();
		mAsrClientListener = new SpeechASRListenerImpl();
		mWakeClientListener = new SpeechWakeUpListenerImpl();
		mGrammarClientInitListener =new SpeechGrammarInitListenerImpl();
		mPcmClientListener = new PcmListenerImpl();
		getBinder();
	}

	public int registerPcmListener(PcmListener listener){
		int ret = 0 ;
		this.mPcmListener = listener;
		getBinder();
		try {
			if (mService != null){
				ret = mService.registerPcmListener(mContext.getPackageName(),mPcmClientListener);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int unregisterPcmListener(){
		int ret = 0 ;
		this.mPcmListener = null;
		getBinder();
		try {
			if (mService != null){
				ret = mService.unregisterPcmListener(mContext.getPackageName());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int registerWakeUpCallback(SpeechWakeUpListener listener){
		int ret = 0;
		this.mWakeUpListener = listener;
		getBinder();
		try {
			if (mService != null) {
				ret = mService.registerWakeUpCallbackListener(mContext.getApplicationInfo().packageName, mWakeClientListener);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		return ret;
	}

	public int unregisterWakeUpCallback() {
		int ret = 0 ;
		this.mWakeUpListener = null;
		getBinder();
		try {
			if (mService != null){
				ret = mService.unregisterWakeUpCallbackListener(mContext.getPackageName());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void getBinder() {
		if (mService == null) {
			mService = ISpeechInterface.Stub.asInterface(BinderFetchServiceUtil.get(mContext).getServiceBinder(SdkConstants.SPEECH_BINDER_NAME));
		}
	}

	public void switchSpeechCore(String language) {
		try {
			if (mService != null) {
				 mService.switchSpeechCore(language);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	public void setVoiceName(String strVoiceName) {
		getBinder();
		try {
			if (mService != null) {
				mService.setVoiceName(strVoiceName);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void setTtsSpeed(int speed){
		getBinder();
		try {
			if (mService != null) {
				mService.setTtsSpeed(speed + "");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void setTtsVolume(int volume){
		getBinder();
		try {
			if (mService != null) {
				mService.setTtsVolume(volume + "");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void onPlay(String text, SpeechTtsListener listener) {
		getBinder();
		this.mTtsListener = listener;
		try {
			if (mService != null) {
				mService.onPlayCallback(mContext.getPackageName(),text,mTtsClientListener);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void onStopPlay() {
		getBinder();
		try {
			if (mService != null) {
				mService.onStopPlay();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startSpeechAsr(int appId, SpeechASRListener listener) {
		getBinder();
		this.mAsrListener = listener;
		try {
			if (mService != null) {
				mService.startSpeechAsr(mContext.getApplicationInfo().packageName, appId, mAsrClientListener);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void stopSpeechAsr() {
		getBinder();
		try {
			if (mService != null) {
				mService.stopSpeechAsr();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void initGrammar(String grammar,SpeechInitGrammarListener listener){
		this.mGrammarInitlistener =listener;
		getBinder();
		if(mService!=null){
			try {
				mService.initSpeechGrammar(grammar,mGrammarClientInitListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void switchWakeup(boolean enable){
		getBinder();
		if(mService!=null){
			try {
				mService.switchWakeup(enable);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public List<SpeechVoice> getSpeechVoices() {
		List<SpeechVoice> list = null;
		getBinder();
		if (mService != null) {
			try {
				list = mService.getSpeechVoices();
			} catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public SpeechVoice getCurSpeechVoice() {
		SpeechVoice sv = null;
		getBinder();
		if (mService != null) {
			try {
				sv = mService.getCurSpeechVoices();
			} catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return sv;
	}

    public void startLocalFunction(String function) {
		getBinder();
		if (mService != null) {
			try {
				mService.startLocalFunction(function);
			} catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
    }

	public boolean isSpeechGrammar(){
		getBinder();
		if (mService != null){
			try {
				return mService.isSpeechGrammar();
			}catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isSpeechIat(){
		getBinder();
		if (mService != null){
			try {
				return mService.isSpeechIat();
			}catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setSpeechMode(int mode) {
		getBinder();
		if (mService != null){
			try {
				mService.setSpeechMode(mode);
			}catch (RemoteException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public class SpeechTtsCallBackListenerImp extends ITtsCallBackListener.Stub {

		@Override
		public void onBegin() throws RemoteException {

		}

		@Override
		public void onEnd() throws RemoteException {
			if (mTtsListener != null) {
				mTtsListener.onEnd();
			}
		}
	}

	public class SpeechASRListenerImpl extends ISpeechAsrListener.Stub {
		@Override
		public void onBegin() throws RemoteException {
			if (mAsrListener != null) {
				mAsrListener.onBegin();
			}
		}

		@Override
		public void onEnd() throws RemoteException {
			if (mAsrListener != null) {
				mAsrListener.onEnd();
			}
		}

		@Override
		public void onResult(String text) throws RemoteException {
			if (mAsrListener != null) {
				mAsrListener.onResult(text);
			}
		}

		@Override
		public void onError(int code) throws RemoteException {
			if (mAsrListener != null) {
				mAsrListener.onError(code);
			}
		}
	}

	class SpeechWakeUpListenerImpl extends ISpeechWakeUpListener.Stub{

		@Override
		public void onSuccess() throws RemoteException {
			if (null != mWakeUpListener){
				mWakeUpListener.onSuccess();
			}
		}

		@Override
		public void onError(int errCode, String errDes) throws RemoteException {
			if (null != mWakeUpListener){
				mWakeUpListener.onError(errCode, errDes);
			}
		}
	}

	class SpeechGrammarInitListenerImpl extends ISpeechGrammarInitListener.Stub{

		@Override
		public void speechGrammarInitCallback(String grammarID, int nErrorCode) throws RemoteException {
			if (mGrammarInitlistener == null) return;
			mGrammarInitlistener.onSpeechGrammarInitCallback(grammarID,nErrorCode);
		}
	}

	class PcmListenerImpl extends IPcmListener.Stub{

		@Override
		public void onPcmData(byte[] data, int dataLen) throws RemoteException {
			if (null != mPcmListener){
				mPcmListener.onPcmData(data,dataLen);
			}
		}
	}
}
