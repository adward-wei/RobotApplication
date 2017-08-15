/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.dispatch;

import android.content.Context;
import android.os.Messenger;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方speech服务调度管理类
 *
 * @author Administrator
 */
public class SpeechDispatcher {
	private static final String TAG = "paul";
	private static final int MSG_RECEIVE_FROM_CLIENT = 3;
	private static Context mContext;
	/**
	 * 当前第三方应用服务的util
	 */
	private SpeechServiceUtil mCurSpeechService;
	/**
	 * 第三方服务的向主服务发送消息的公共Msger
	 */
	private Messenger mPublicMsgerSentFromClient;
	/**
	 * 第三方语音服务集合 key:第三方语音指令接听appId value:第三方语音调用服务的处理类
	 */
	private Map<Integer, SpeechServiceUtil> mThirdServiceList;
	/**
	 * 从bnf文件读取到第三方离线指令集合
	 */
	private Map<Integer, List<String>> mAppOfflineCmdMAP;

	public static SpeechDispatcher getInstance(Context context) {
		mContext = context;
		return SpeechDispatcherInstance.INSTANCE;
	}


	private static class SpeechDispatcherInstance {
		private static final SpeechDispatcher INSTANCE = new SpeechDispatcher();

		private SpeechDispatcherInstance() {
		}
	}

	/**
	 * 私有构造方法
	 */
	private SpeechDispatcher() {
		/**
		 mPublicMsgerSentFromClient = new Messenger(mIncomingHandler);*/
		mThirdServiceList = new HashMap<Integer, SpeechServiceUtil>();
	}

	/**
	 * 发送speech文本给对应appId第三方语音服务
	 *
	 * @param appId     第三方语音服务appId
	 * @param speechTxt 识别到的语音文本
	 */
	public void sendSpeechRstToClient(int appId, String speechTxt) {
		SpeechServiceUtil speechService = getSpeechService(appId);
		if (speechService != null) {
			speechService.checkBindService();
			speechService.sendMsg(speechTxt);
		}
	}

	/**
	 * 发送speech文本给对应appId第三方语音服务
	 *
	 * @param slot
	 */
	public void sendSpeechRstToClient(Slot slot) {
		String slotName = slot.getName();
		String speechTxt = slot.getContent();
		int appId = slot.getAppId();
		if (TextUtils.isEmpty(slotName)){
			LogUtils.i(TAG, "在线自定义场景语音分发appid:%d 指令内容:%s", appId, speechTxt);
			sendSpeechRstToClient(appId, speechTxt);
			return;
		}
		LogUtils.i(TAG, "slotName:%s speechTxt:%s appId:%d", slotName, speechTxt,appId);
		String slotType = DataParseUtil.getSlotType(slotName);

		switch (slotType) {
			case SpeechContants.APP:
				break;
			case SpeechContants.GLOBAL_CONTEXT:
				appId = mCurSpeechService != null ? mCurSpeechService.getAppId() : 0;
				break;
			case SpeechContants.APP_CONTEXT:
				appId = 0;
				int[] ids = slot.getIds();
				//当前appId是否在前台
				for (int i = 0; i < ids.length; i++) {
					if (checkAuthorizeAppId(ids[i])) {
						appId = ids[i];
						break;
					}
				}
				break;
		}

		if (appId > 0) {
			LogUtils.i(TAG, "离线语音分发appid:%d 指令内容:%s", appId, speechTxt);
			sendSpeechRstToClient(appId, speechTxt);
		}
	}

	/**
	 * 根据service act获取相应的service并切换调度第三方service
	 *
	 * @param appId
	 * @return
	 */
	private SpeechServiceUtil getSpeechService(int appId) {
		SpeechServiceUtil speechService = null;
		if (mCurSpeechService != null && mCurSpeechService.getAppId() == appId) {
			speechService = mCurSpeechService;
		} else {
			speechService = switchAppService(appId);
		}
		return speechService;
	}

	/**
	 * 根据appId查找并切换第三方应用服务
	 *
	 * @param appId 第三方应用appId
	 * @return 第三方语音服务util类
	 */
	private SpeechServiceUtil switchAppService(int appId) {
		/**当前第三方app speech service退到后台*/
		if (mCurSpeechService != null) {
			mCurSpeechService.callAppServiceOnStop();
		}

		SpeechServiceUtil speechService;
		speechService = queryServiceActFromPools(appId);
		if (speechService == null) {
			speechService = new SpeechServiceUtil(mContext, appId);
			mThirdServiceList.put(appId, speechService);
		}

		/**新的第三方app speech service进入前台*/
		speechService.callAppServiceOnStart();
		mCurSpeechService = speechService;
		return speechService;
	}

	/**
	 * service pools查找appId相应的serviceAction
	 *
	 * @param appId
	 * @return
	 */
	private SpeechServiceUtil queryServiceActFromPools(int appId) {
		SpeechServiceUtil speechService = mThirdServiceList.get(appId);
		return speechService;
	}

	/**
	 * 检测切入语音听写的appId是否是当前在前台的app
	 *
	 * @param appId
	 * @return true 合法     false 非法
	 */
	public boolean checkAuthorizeAppId(int appId) {
		boolean rst = false;
		if (mCurSpeechService != null) {
			rst = mCurSpeechService.getAppId() == appId;
		}
		return rst;
	}

	/**
	 * 检测app是否前台app
	 *
	 * @param pckName
	 * @return true 前台     false 非前台
	 */
	public boolean checkAppIsTopByPckName(String pckName) {
		boolean rst = false;
		if (mCurSpeechService != null) {
			rst = mCurSpeechService.getPckName().equals(pckName);
		}
		return rst;
	}

	/**
	 * 从本地离线指令bnf文件中读取第三方离线语法指令集合
	 */
	public void fetchOfflineCmdsFromBnf() {
		//mAppOfflineCmdMAP = FucUtil.writeOfflineCmdFromBNFIntoMap(mContext, "call.bnf");
	}

	/**
	 * 获取事先从bnf读取的bnf指令集
	 *
	 * @return
	 */
	public Map<Integer, List<String>> getmAppOfflineCmdMAP() {
		return mAppOfflineCmdMAP;
	}


}
