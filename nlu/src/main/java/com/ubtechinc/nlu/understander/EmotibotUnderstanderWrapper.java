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
import android.util.Log;

import com.emotibot.EmotibotApiManager;
import com.emotibot.ErrorMessage;
import com.emotibot.HttpConnectionListener;
import com.emotibot.InitListener;
import com.emotibot.RequestResult;
import com.emotibot.SDKInit;
import com.emotibot.SDKInitBuilder;
import com.ubtechinc.nlu.BuildConfig;
import com.ubtechinc.nlu.iflytekmix.MixSemantic;

import org.json.JSONObject;
import org.json.JSONTokener;

import timber.log.Timber;


/**
 * @desc: Emotibot 语义理解包装
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class EmotibotUnderstanderWrapper extends IUnderstander {

    private static final String APIKEY = "5f1a700717816af2740b487834dd73c1";

    private static final String SECRET = "";

    private EmotibotApiManager api;

    EmotibotUnderstanderWrapper(Context cxt){
        super(cxt);
    }

    static String tag(){
        return "EmotibotUnderstander";
    }

    public static void init(Context cxt){
        if (isInited) return;
        synchronized (lock) {
            if (isInited) return;
            SDKInitBuilder builder = new SDKInitBuilder(cxt.getApplicationContext())
                    .setDebug(BuildConfig.DEBUG)
                    .setEmotibotKey(APIKEY)
                    .setSecret(SECRET)
                    .setUniqueId(UNIQUEID);

            SDKInit.init(builder, new InitListener() {
                @Override
                public void onComplete() {
                    listener.onInitSucess();
                }

                @Override
                public void onFail(String error) {
                    listener.onInitFail(error);
                }
            });
        }
    }

    private static class InnerHttpConnectionListener  extends CancelableUnderstanderCallback implements HttpConnectionListener {
        private final IUnderstanderCallback cb;
        private String speechResult;
        InnerHttpConnectionListener(String speechResult,IUnderstanderCallback cb){
            this.cb = cb;
            this.speechResult = speechResult;
        }

        @Override
        public IUnderstanderCallback getCallback() {
            return cb;
        }

        @Override
        public void onError(ErrorMessage errorMessage) {
            finished();
            if (!isCanceled() && cb != null) {
                if(!TextUtils.isEmpty(errorMessage.getMessage())){
                    Log.d(tag(), errorMessage.getMessage());
                    cb.onRecognizeError(errorMessage.getMessage());
                }else{
                    cb.onRecognizeError("未知原因  请求失败!!!");
                }
            }
        }

        @Override
        public void onSuccess(RequestResult requestResult) {
            finished();
            if (!isCanceled() && requestResult != null && cb != null) {
                try {
                    String answer = requestResult.getContent();
                    if (!TextUtils.isEmpty(answer)) {
                        JSONTokener tokener = new JSONTokener(answer);
                        JSONObject joResult = new JSONObject(tokener);
                        answer = joResult.getString("text");
                        Log.i(tag(), answer);
                        int end = answer.indexOf("/:");
                        end = end < 0 ? answer.indexOf('[') : end;
                        MixSemantic mixSemantic = new MixSemantic();
                        mixSemantic.setSpeechResult(speechResult);
                        mixSemantic.setAnswerText(answer.substring(0, end > 0? end: answer.length()));
                        mixSemantic.setSemanticValid(true);
                        cb.onRecognizeSuccess(mixSemantic);
                    }else {
                        cb.onRecognizeError("识别失败：小影数据异常");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    cb.onRecognizeError("识别失败：小影数据异常"+ e.getMessage());
                }
            }
        }
    }

    @Override
    protected CancelableUnderstanderCallback textUnderstander(String text, IUnderstanderCallback cb) {
        final Context cxt = weakCxt.get();
        if (!isInited && cxt != null) init(cxt);
        if (api == null) {
            synchronized (lock) {
                if (api == null)
                    api = new EmotibotApiManager(cxt);
            }
        }
        if (cxt == null || api == null) return null;

        InnerHttpConnectionListener cancelCallback = new InnerHttpConnectionListener(text,cb);
        Timber.i("emotibot reslover...");
        api.setHttpListener(cancelCallback);
        api.requestEmotibotAPI(text);
        return cancelCallback;
    }
}
