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

import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;
import com.ubtechinc.nlu.R;
import com.ubtechinc.nlu.iflytekmix.MixSemantic;

import org.json.JSONObject;

import java.util.Random;

import timber.log.Timber;
import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * @desc: Tunling 语义理解包装
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class TunlingUnderstanderWrapper extends IUnderstander {

    private static final String TURING_APIKEY = "db47fe05f7a09b35598d6cb169d2ec54";

    private static final String TURING_SECRET = "6cb6de730e341fc8";

    private TuringApiManager api;

    protected TunlingUnderstanderWrapper(Context context){
        super(context);
    }

    public static void init(Context cxt){
        if (isInited) return;
        synchronized (lock) {
            if (isInited) return;
            SDKInitBuilder builder = new SDKInitBuilder(cxt.getApplicationContext())
                    .setSecret(TURING_SECRET)
                    .setTuringKey(TURING_APIKEY)
                    .setUniqueId(UNIQUEID);
            SDKInit.init(builder, new InitListener() {
                @Override
                public void onFail(String error) {
                    listener.onInitFail(error);
                }

                @Override
                public void onComplete() {
                    listener.onInitSucess();
                }
            });
        }
    }

    protected static String tag(){
        return "TunlingUnderstander";
    }

    private static class InnerHttpConnectionListener  extends CancelableUnderstanderCallback implements HttpConnectionListener {
        private final IUnderstanderCallback cb;
        private final Context weakCxt;
        private String speechResult;
        InnerHttpConnectionListener(Context cxt, String text, IUnderstanderCallback cb){
            this.cb = cb;
            this.weakCxt = cxt;
            this.speechResult = text;
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
            if (!isCanceled() && requestResult != null && cb != null)
            {
                try {
                    String content = requestResult.getContent().toString();
                    if(content != null) {
                        JSONObject result_obj = new JSONObject(content);
                        if (result_obj.has("text")) {
                            String answer = result_obj.get("text").toString();
                            Log.d(tag(), result_obj.get("text").toString());
                            String src = "图灵机器人";
                            String src1 = "赢赢";
                            String src2 = "开始播放音乐";
                            String rep = "阿尔法";
                            // Warning 2017/3/21 复制旧代码
                            if (answer.contains(src)) {
                                answer = answer.replaceAll(src, rep);
                            } else if (answer.contains(src1)) {
                                answer = answer.replaceAll(src1, rep);
                            } else if (answer.contains(src2) && weakCxt != null) {
                                String[] answer1 = weakCxt.getResources().getStringArray(R.array.no_answer_text);
                                Random random = new Random();
                                int index = random.nextInt(answer1.length);
                                answer = answer1[index];
                            }
                            MixSemantic mixSemantic = new MixSemantic();
                            mixSemantic.setSpeechResult(speechResult);
                            mixSemantic.setAnswerText(answer);
                            mixSemantic.setSemanticValid(true);
                            cb.onRecognizeSuccess(mixSemantic);
                        }else {
                            cb.onRecognizeError("识别失败：图灵数据异常");
                        }
                    }else {
                        cb.onRecognizeError("识别失败：图灵数据异常");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    cb.onRecognizeError("识别失败：图灵数据异常，"+e.getMessage());
                }
            }
        }

        @Override
        public IUnderstanderCallback getCallback() {
            return cb;
        }
    }

    @Override
    protected CancelableUnderstanderCallback textUnderstander(String text, IUnderstanderCallback cb) {
        final Context cxt = weakCxt.get();
        if (!isInited && cxt != null) init(cxt);
        if (!isInited) return null;
        if (api == null) {
            synchronized (lock) {
                if (api == null)
                    api = new TuringApiManager(cxt);
            }
        }
        if (cxt == null) return null;

        InnerHttpConnectionListener cancelCallback = new InnerHttpConnectionListener(cxt,text,cb);
        api.setHttpListener(cancelCallback);
        Timber.i("Tunling reslover...");
        api.requestTuringAPI(text);
        return cancelCallback;
    }
}
