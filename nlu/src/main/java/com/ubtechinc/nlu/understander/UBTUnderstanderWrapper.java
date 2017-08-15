/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */
package com.ubtechinc.nlu.understander;

import android.content.Context;
import android.text.TextUtils;

import com.ubtechinc.nets.HttpManager;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nlu.iflytekmix.MixSemantic;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * @desc: UBT的语义分析服务器包装
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class UBTUnderstanderWrapper extends IUnderstander {
    static final String url = "alpha2-web/ai.do?ask&query=";

    UBTUnderstanderWrapper(Context cxt) {
        super(cxt);
    }

    private static class InnerHttpConnectionListener  extends CancelableUnderstanderCallback implements ResponseListener<String> {
        private final IUnderstanderCallback cb;
        private final String  speechResult;
        InnerHttpConnectionListener(String speechResult, IUnderstanderCallback cb){
            this.cb = cb;
            this.speechResult = speechResult;
        }

        @Override
        public IUnderstanderCallback getCallback() {
            return cb;
        }

        @Override
        public void onError(ThrowableWrapper e) {
            finished();
            if (!isCanceled() && cb != null){
                cb.onRecognizeError("本地服务器获取语义失败");
            }
        }

        @Override
        public void onSuccess(String result) {
            finished();
            if (!isCanceled() && cb != null) {
                if (TextUtils.isEmpty(result)) {
                    cb.onRecognizeError("本地服务器没有返回结果");
                } else {
                    MixSemantic mixSemantic = new MixSemantic();
                    mixSemantic.setSpeechResult(speechResult);
                    mixSemantic.setAnswerText(result);
                    mixSemantic.setSemanticValid(true);
                    cb.onRecognizeSuccess(mixSemantic);
                }
            }
        }
    }

    @Override
    protected CancelableUnderstanderCallback textUnderstander(String recognizeResult, IUnderstanderCallback cb) {
        String encodeUrl = url+recognizeResult;
        final Context cxt = weakCxt.get();
        if (cxt == null) return null;

        InnerHttpConnectionListener cancelable = new InnerHttpConnectionListener(recognizeResult,cb);
        Timber.i("UBT Server reslover...");
        HttpManager.get(cxt).doGet(encodeUrl, null, cancelable);
        return cancelable;
    }

    public static String tag(){
        return "UBTUnderstander";
    }
}
