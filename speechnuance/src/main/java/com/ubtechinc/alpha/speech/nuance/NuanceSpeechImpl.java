/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.nuance;

import android.content.Context;

import com.nuance.dragon.toolkit.vocalizer.VocalizerVoice;
import com.ubtechinc.alpha.serverlibutil.aidl.SpeechVoice;
import com.ubtechinc.alpha.speech.NuanceUtil;
import com.ubtechinc.alpha.speech.SpeechContext;
import com.ubtechinc.alpha.speech.SpeechContextBase;
import com.ubtechinc.alpha.speech.utils.IRecUtil;
import com.ubtechinc.alpha.speech.utils.NuanceRecUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2016/12/27
 * @author logic.peng
 * @Description Nuance语音引擎逻辑处理类
 * @modifier  logic.peng
 * @modify_time 2017/4/7 增加唤醒回调api
 * @modify_time 2017/7/31 重构
 */

public class NuanceSpeechImpl extends SpeechContextBase implements SpeechContext {
	public static final String TAG = "NuanceSpeechImpl";
	private List<SpeechVoice> mSpeechVoices = null;

	public NuanceSpeechImpl(Context context) {
		super(context,new NuanceUtil(context));
	}

	@Override
	public List<SpeechVoice> getSpeechVoices() {
		if (mSpeechVoices == null){
			mSpeechVoices = new ArrayList<>();
			SpeechVoice v;
			for (VocalizerVoice voice: NuanceConstants.SUPPORT_VOICES) {
				v = new SpeechVoice();
				v.setName(voice.name);
				v.setLanguage(voice.language.name);
				v.setAdult(1);
				v.setSex(1);
				mSpeechVoices.add(v);
			}
		}
		return mSpeechVoices;
	}

	@Override
	protected IRecUtil getRecUtil() {
		return new NuanceRecUtil(this);
	}
}
