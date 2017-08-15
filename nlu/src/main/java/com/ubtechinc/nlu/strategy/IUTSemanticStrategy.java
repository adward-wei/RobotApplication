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

import com.ubtechinc.nlu.iflytekmix.MixSemantic;
import com.ubtechinc.nlu.understander.IUnderstander;
import com.ubtechinc.nlu.understander.IUnderstanderCallback;
import com.ubtechinc.nlu.understander.IUnderstanderFactory;

import java.util.concurrent.Semaphore;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * @desc: 科飞(I)->UBT(U)->Emotibot(E)
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

class IUTSemanticStrategy extends IUSemanticStrategy {
    private final Semaphore semaphore = new Semaphore(0);
    private final IUnderstander tunling;

    IUTSemanticStrategy(Context cxt) {
        super(cxt);
        tunling = IUnderstanderFactory.createTunling(cxt);
    }

    @DebugLog
    @Override
    public boolean speechGrammarProcess(String semStr) {
        boolean success = super.speechGrammarProcess(semStr);
        if (success) {
            return true;
        }
        success = tunling.handleRequest(mixSemantic.getSpeechResult(), new IUnderstanderCallback() {
            @Override
            public void onRecognizeSuccess(MixSemantic result) {
                mixSemantic = result;
                mixSemantic.setFrom(MixSemantic.FROM_TUNLING);
                mixSemantic.setSemanticValid(true);
                semaphore.release(1);
            }

            @Override
            public void onRecognizeError(String errorMessage) {
                errorMsg = errorMessage;
                Timber.w("Tunling resolver error: " + errorMessage);
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
                Timber.v(e.getMessage());
            }
        }
        tunling.clear();
        return mixSemantic != null && mixSemantic.isSemanticValid();
    }
}
