/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.ifytek;

import android.content.Context;

import com.ubtechinc.alpha.configure.tts.SpeechConfigParser;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.Ifytek5Util;
import com.ubtechinc.alpha.speech.SpeechContext;
import com.ubtechinc.alpha.speech.SpeechContextBase;
import com.ubtechinc.alpha.speech.utils.IRecUtil;
import com.ubtechinc.alpha.speech.utils.IfytekRecUtil;

import java.util.List;

/**
 * @author logic.peng
 * @date 2017/8/5
 * @Description 讯飞语音引擎逻辑处理类
 * @modifier
 * @modify_time
 */

public class IfytekSpeechImpl extends SpeechContextBase implements SpeechContext {
	public static final String CONF_TTS_VOICE_PATH = "conf/ifly_tts_voice.js";
	private static final String TAG = "IfytekSpeechImpl";
	private SpeechConfigParser mSpeechConfigParser;
	private List<SpeechVoice> mSpeechVoices = null;

	public IfytekSpeechImpl(Context context) {
		super(context, new Ifytek5Util(context));
	}
	
	@Override
	public List<SpeechVoice> getSpeechVoices() {
		if (mSpeechVoices == null){
			mSpeechConfigParser = new SpeechConfigParser(mContext, CONF_TTS_VOICE_PATH);
			mSpeechConfigParser.parse();
			mSpeechVoices = mSpeechConfigParser.getData();
		}
		return mSpeechVoices;
	}

	@Override
	protected IRecUtil getRecUtil() {
		return new IfytekRecUtil(this);
	}
}
