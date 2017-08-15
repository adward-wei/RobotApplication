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

/**
 * @desc: 科飞语义分析策略
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class DefaultSemanticStrategy implements SpeechGrammarStrategy {
    private static final String TAG = DefaultSemanticStrategy.class.getSimpleName();
    final Context cxt;
    private IUnderstander understander;
    private final Semaphore semaphore = new Semaphore(0);

    volatile MixSemantic mixSemantic;
    String errorMsg;
    @IUnderstanderCallback.WaitState int waitState = 0;

    DefaultSemanticStrategy(Context cxt){
        this.cxt = cxt;
        this.understander = IUnderstanderFactory.createLocalUnder(cxt);
    }

    @Override
    public boolean speechGrammarProcess(final String semStr) {
        boolean ret = understander.handleRequest(semStr, new IUnderstanderCallback() {

            @Override
            public void onRecognizeSuccess(MixSemantic result) {
                mixSemantic = result;
                semaphore.release(1);
            }

            @Override
            public void onRecognizeError(String errorMessage) {
                errorMsg = errorMessage;
                mixSemantic = new MixSemantic();
                mixSemantic.setSemanticValid(false);
                mixSemantic.setSpeechResult(errorMessage);
                semaphore.release(1);
            }

            @Override
            public void onWaitForReturn(@WaitState int state) {
                waitState = state;
                semaphore.release(1);
            }
        });

        if (ret && mixSemantic == null) {
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                Log.v(TAG, e.getLocalizedMessage());
                return false;
            }
        }
        understander.clear();
        return ret && mixSemantic != null && mixSemantic.isSemanticValid();
    }

    public MixSemantic getSemantic() {
        return mixSemantic;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getWaitState() {
        return waitState;
    }

}
