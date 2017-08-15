package com.ubtechinc.alpha.affairdispatch;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.app.AlphaApplication;

import com.ubtechinc.alpha.event.ChestUpgradeEvent;
import com.ubtechinc.alpha.event.HeadUpgradeEvent;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;

import com.ubtechinc.alpha.ops.ActionServiceProxy;

import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;

import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;


/**
 * @desc : 事务执行
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public class AffairExecute implements  AffairCallback {
    private final Handler mHandler;
    private BaseAffair currentAffair;
    private IAffairStateListener stateListener;
    Runnable timeOutRunnable = null;

    public AffairExecute() {

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {
                if(message.what == 0) {
                    BaseAffair affair = (BaseAffair) message.obj;
                    executeOp(affair);
                }
            }
        };
    }

    public void execute(BaseAffair affair, int delayTime) {
        if(delayTime > 0) {
            Message msg = mHandler.obtainMessage(0);
            msg.obj = affair;
            mHandler.sendMessageDelayed(msg, delayTime);
        } else {
            executeOp(affair);
        }
    }

    private void executeOp(BaseAffair affair) {
        if(affair != null) {
            currentAffair = affair;
            currentAffair.execute(this);
            long timeOutTime = 0;
            timeOutTime = currentAffair.calcuteExecuteTime(); //当前只支持tts,不支持动作
            if (timeOutTime > 0) {
                HandlerUtils.getMainHandler().postDelayed(timeOutRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (stateListener != null) {
                            stateListener.onAffairTimeOut(currentAffair);
                        }
                    }
                },timeOutTime);
            }
        }
    }

    public void setAffairStateCallback(IAffairStateListener stateListener) {
        this.stateListener = stateListener;
    }

    @Override
    public void onComplete() {
        if (timeOutRunnable != null) {
            HandlerUtils.getMainHandler().removeCallbacks(timeOutRunnable);
        }

        if (stateListener != null) {
            stateListener.onAffairComplete(currentAffair);
        }
    }
}
