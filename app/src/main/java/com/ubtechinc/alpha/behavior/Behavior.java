package com.ubtechinc.alpha.behavior;

import android.support.annotation.NonNull;

import com.ubtech.utilcode.utils.thread.HandlerUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public abstract class Behavior implements Comparable<Behavior>{
    protected static final String TAG = "Behavior";
    protected int order;
    protected int delayTime = 0;
    private AtomicInteger repeat = new AtomicInteger(0);

    int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
    }

    void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    void setRepeat(int repeat) {
        this.repeat.set(repeat);
    }

    @Override
    public int compareTo(@NonNull Behavior behavior) {
        return getOrder() - behavior.getOrder();
    }

    public void start(){
        if (delayTime > 0) {
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    if (!onStart() && listener != null) {
                        listener.onCompleted();
                    }
                }
            }, delayTime);
            delayTime = 0;
        }else {
            if (!onStart() && listener != null) {
                listener.onCompleted();
            }
        }
    }

    protected abstract boolean onStart();

    protected BehaviorResultCallback resultCallback = new BehaviorResultCallback() {
        @Override
        public void onBehaviorResult(boolean result) {
            if (repeat.decrementAndGet() > 0 && result){
                start();
            }else {
                if (listener != null)
                    listener.onCompleted();
            }
        }
    };
    protected interface BehaviorResultCallback {
        void onBehaviorResult(boolean result);
    }

    private BehaviorListener listener;
    public interface BehaviorListener{
        void onCompleted();
    }

    public void setBehaviorListener(BehaviorListener listener) {
        this.listener = listener;
    }
}
