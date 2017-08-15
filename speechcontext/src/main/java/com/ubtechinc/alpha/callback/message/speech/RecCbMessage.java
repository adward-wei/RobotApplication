/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.callback.message.speech;

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.callback.DispatchMessage;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author logic.peng@ubtrobot.com
 * @date 2017/8/9
 * @Description 语音识别回调
 * @modifier
 * @modify_time
 */

public class RecCbMessage implements DispatchMessage.Callback {

	public static final int CALLBACK_STATE_BEGIN_SPEECH = 0;
	public static final int CALLBACK_STATE_END_SPEECH = 1;
	public static final int CALLBACK_STATE_RESULT_SPEECH = 2;
	public static final int CALLBACK_STATE_ERROR_SPEECH = 3;
	@Retention(RetentionPolicy.SOURCE)
	@IntDef(value = {CALLBACK_STATE_BEGIN_SPEECH, CALLBACK_STATE_END_SPEECH,
			CALLBACK_STATE_RESULT_SPEECH,CALLBACK_STATE_ERROR_SPEECH})
	public @interface IatCbState{}

	private final AbstractSpeech mSpeechContext;
	private final String mPackageName;
	private final String mContent;
	private final int mState;
	private final IRecognizerListener listener;

	public RecCbMessage(AbstractSpeech speech, String packageName, String content, @IatCbState int state, IRecognizerListener listener) {
		this.mSpeechContext = speech;
		this.mPackageName = packageName;
		this.mContent = content;
		this.mState = state;
		this.listener = listener;
	}

	@Override
	public void handleMessage() {
		if (listener == null) return;

		switch (mState){
            case CALLBACK_STATE_BEGIN_SPEECH:
                if (listener != null) {
                    listener.onBegin();
                }
                break;
            case CALLBACK_STATE_END_SPEECH:
                if (listener != null) {
                    listener.onEnd();
                }
                break;
            case CALLBACK_STATE_RESULT_SPEECH:

                if (listener != null) {
                    listener.onResult(mContent, true);
                }
                break;
            case CALLBACK_STATE_ERROR_SPEECH:
                if (listener != null) {
                    listener.onError(0);
                }
                break;
			default:
				break;
        }
	}
}
