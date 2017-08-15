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
 * @desc: 科飞->UBT服务器
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

class IUSemanticStrategy extends DefaultSemanticStrategy {
    private static final String TAG = IUSemanticStrategy.class.getSimpleName();
    private final Semaphore semaphore = new Semaphore(0);
    private final IUnderstander ubt;

    IUSemanticStrategy(Context cxt) {
        super(cxt);
        ubt = IUnderstanderFactory.createUBTServerUnder(cxt);
    }

    @Override
    public boolean speechGrammarProcess(String semStr) {
        boolean success = super.speechGrammarProcess(semStr);
        if (success) {
            return true;
        }
        assert mixSemantic != null;
        semStr = mixSemantic.getSpeechResult();

        success = ubt.handleRequest(semStr, new IUnderstanderCallback() {
            @Override
            public void onRecognizeSuccess(MixSemantic result) {
                mixSemantic = result;
                mixSemantic.setFrom(MixSemantic.FROM_UBT);
                mixSemantic.setSemanticValid(true);
                semaphore.release(1);
            }

            @Override
            public void onRecognizeError(String errorMessage) {
                errorMsg = errorMessage;
                Log.w(TAG, "UBT resolver error: " + errorMessage);
                semaphore.release(1);
            }

            @Override
            public void onWaitForReturn(@IUnderstanderCallback.WaitState int state) {
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
        ubt.clear();
        return mixSemantic != null && mixSemantic.isSemanticValid();
    }
}
