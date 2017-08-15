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

import com.ubtechinc.nlu.iflytekmix.MixSemantic;
import com.ubtechinc.nlu.iflytekmix.MixSemanticResolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @desc: 1、接收科飞grammer后的结果，该结果语义结果来自本地、网络、Excel
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class LocalUnderstanderWrapper extends IUnderstander {

    private  final MixSemanticResolver resolver;

    LocalUnderstanderWrapper(Context cxt) {
        super(cxt);
        this.parseThreadPool = Executors.newFixedThreadPool(2);
        this.resolver = new MixSemanticResolver(cxt);
    }

    public static void init(final Context cxt){}

    private ExecutorService parseThreadPool;

    private static class InnerUnderstanderCallback extends CancelableUnderstanderCallback {
        private final IUnderstanderCallback weakCb;
        private final MixSemanticResolver weakResolver;
        InnerUnderstanderCallback(MixSemanticResolver resolver , IUnderstanderCallback that){
            weakCb = that;
            weakResolver = resolver;
        }

        @Override
        public IUnderstanderCallback getCallback() {
            return weakCb;
        }
        public MixSemanticResolver getResolver() {
            return weakResolver;
        }
    }

    @Override
    protected CancelableUnderstanderCallback textUnderstander(final String grammerResult, IUnderstanderCallback cb) {
        final Context cxt = weakCxt.get();
        if (cxt == null) return null;

        final InnerUnderstanderCallback callback = new InnerUnderstanderCallback(resolver,cb);

        parseThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                IUnderstanderCallback cb = callback.getCallback();
                MixSemanticResolver resolver = callback.getResolver();
                if (cb != null && resolver != null && !callback.isCanceled()){
                    MixSemantic mixSemantic;
                    mixSemantic = resolver.resolve(grammerResult);
                    callback.finished();
                    if (mixSemantic.isSemanticValid()) {
                        cb.onRecognizeSuccess(mixSemantic);
                    }else {
                        cb.onRecognizeError(mixSemantic.getSpeechResult());
                    }
                }
            }
        });
        return callback;
    }

    @Override
    public void clear() {
        super.clear();
        parseThreadPool.shutdownNow();
        parseThreadPool = null;
    }
}
