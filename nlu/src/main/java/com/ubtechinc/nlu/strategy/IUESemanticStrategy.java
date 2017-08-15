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
package com.ubtechinc.nlu.strategy;

import android.content.Context;
import android.util.Log;

import com.ubtechinc.nlu.iflytekmix.MixSemantic;
import com.ubtechinc.nlu.understander.IUnderstander;
import com.ubtechinc.nlu.understander.IUnderstanderCallback;
import com.ubtechinc.nlu.understander.IUnderstanderFactory;

import java.util.concurrent.Semaphore;

import hugo.weaving.DebugLog;

/**
 * @desc: 科飞(I)->UBT(U)->Emotibot(E)
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

class IUESemanticStrategy extends IUSemanticStrategy {
    private static final String TAG = IUESemanticStrategy.class.getSimpleName();
    private final Semaphore semaphore = new Semaphore(0);

    private final IUnderstander emotibot;

    IUESemanticStrategy(Context cxt) {
        super(cxt);
        emotibot = IUnderstanderFactory.createEmotibot(cxt);
    }

    @DebugLog
    @Override
    public boolean speechGrammarProcess(String semStr) {
        boolean success = super.speechGrammarProcess(semStr);
        if (success) {
            return true;
        }

        //Emotibot处理
        success = emotibot.handleRequest(mixSemantic.getSpeechResult(), new IUnderstanderCallback() {
            @Override
            public void onRecognizeSuccess(MixSemantic result) {
                mixSemantic = result;
                mixSemantic.setFrom(MixSemantic.FROM_EMOT);
                mixSemantic.setSemanticValid(true);
                semaphore.release(1);
            }

            @Override
            public void onRecognizeError(String errorMessage) {
                errorMsg = errorMessage;
                Log.w(TAG, "Emotibot resolver error: " + errorMessage);
                semaphore.release(1);
            }

            @Override
            public void onWaitForReturn(@WaitState int state) {
                semaphore.release(1);
            }
        });

        if (success) {
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                Log.v(TAG, e.getMessage());
            }
        }

        emotibot.clear();
        return mixSemantic != null && mixSemantic.isSemanticValid();
    }
}
