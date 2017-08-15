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
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @desc: 语义理解：只封装对文本的理解
 *           每次有文本请求发出时，如果上一次请求未返回，被Cancel掉；
 *           若在5秒内未返回数据，语义理解请求被cancel掉。
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public abstract class IUnderstander {
    static final String UNIQUEID = "18038028907";
    static final int  TIMEOUT = 20;

    static volatile boolean isInited = false;
    final static byte[] lock = new byte[0];
    final WeakReference<Context> weakCxt;

    static IUnderstanderInitListener listener = new IUnderstanderInitListener() {
        @Override
        public void onInitSucess() {
            Log.v(tag(), "SpeechUnderstander init success..");
            isInited = true;
        }

        @Override
        public void onInitFail(String error) {
            Log.w(tag(), "SpeechUnderstander init fail : "+ error);
            isInited = false;
        }
    };

    public static void init(Context cxt){}

    private IUnderstander(){
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static String tag() {
        return IUnderstander.class.getSimpleName();
    }

    protected IUnderstander (Context cxt){
        this.weakCxt = new WeakReference<Context>(cxt);
    }

    private CancelableUnderstanderCallback lastUnderstanderCB = null;


    public boolean handleRequest(String text, IUnderstanderCallback cb){
        boolean needWaitFor = false;
        synchronized (this) {
            if (lastUnderstanderCB != null && !lastUnderstanderCB.isCanceled() && !lastUnderstanderCB.isFinished()) {
                lastUnderstanderCB.canceled();
                needWaitFor = true;
            }
        }

        if (needWaitFor) {
            cb.onWaitForReturn(IUnderstanderCallback.WAITMOMENT);
        }

        lastUnderstanderCB = textUnderstander(text, cb);
        timer.schedule(new Runnable() {
            @Override
            public void run() {
                IUnderstanderCallback cb = lastUnderstanderCB.getCallback();
                if (cb != null)
                    cb.onWaitForReturn(IUnderstanderCallback.TIMEOUT);
                synchronized (IUnderstander.this) {
                    lastUnderstanderCB.canceled();
                    lastUnderstanderCB = null;
                }
            }
        }, TIMEOUT, TimeUnit.SECONDS);
        return lastUnderstanderCB != null;
    }

    private  ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

    protected abstract CancelableUnderstanderCallback textUnderstander(String text, IUnderstanderCallback cb);

    public void clear(){
        timer.shutdownNow();
        timer = null;
        lastUnderstanderCB = null;
    }
}
