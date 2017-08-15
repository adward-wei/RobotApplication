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
 * @desc : emotibot->iflytek
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/29
 * @modifier:
 * @modify_time:
 */

class EISemanticStrategy extends DefaultSemanticStrategy {
    private final Semaphore semaphore = new Semaphore(0);
    private final IUnderstander emotibot;

    EISemanticStrategy(Context cxt) {
        super(cxt);
        emotibot = IUnderstanderFactory.createEmotibot(cxt);
    }

    @DebugLog
    @Override
    public boolean speechGrammarProcess(String semStr) {
        boolean success = super.speechGrammarProcess(semStr);
        final MixSemantic defaultSemantic = success ? mixSemantic : null;
        assert mixSemantic != null;
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
                Timber.w("Emotibot resolver error: " + errorMessage);
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
        if ((mixSemantic == null || !mixSemantic.isSemanticValid())&& defaultSemantic != null){
           mixSemantic = defaultSemantic;
        }
        emotibot.clear();
        return mixSemantic != null && mixSemantic.isSemanticValid();
    }
}
