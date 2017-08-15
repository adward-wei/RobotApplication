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

package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.SoundDirectionEvent;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.motor.MoveAbsAngleOp;
import com.ubtechinc.alpha.ops.motor.ReadAngleOp;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc : 声音方向处理:2mic
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public class ProxySoundDirectionImpl extends AbstractProxyService {
    private final Context mContext;
    private long mLastAdjustTime = 0;
    private byte mLastAngle = 0;

    private Subscriber<SoundDirectionEvent> mSoundDireSubscriber = new Subscriber<SoundDirectionEvent>() {
        @Override
        public void onEvent(SoundDirectionEvent event) {
            OpResult<Short> motorAngle = RobotOpsManager.get(mContext).executeOpSync(new ReadAngleOp((byte) 19, false));
            byte newAngle = 0;
            if (motorAngle == null) return;
            if(SysUtils.is2Mic()) {
                long curTime = System.currentTimeMillis();
                long time = curTime - mLastAdjustTime;
                if ((time > 1000 || time < -1000)) {
                    byte angle = (byte) event.angle;
                    byte angleDiff = (byte) (mLastAngle - angle);
                    if (angleDiff > 5 || angleDiff < -5) {
                        RobotOpsManager.get(mContext).executeOp(new ReadAngleOp((byte) 19, false));
                        mLastAngle = angle;
                    }

                    byte diff = 0;
                    int mCurrentAngle = 0;
                    mCurrentAngle = angle << 8;
                    mCurrentAngle = mCurrentAngle >> 8;
                    LogUtils.i( "!!!!! mCurrentAngle=" + mCurrentAngle);
                    diff = (byte) (mCurrentAngle - 90);
                    LogUtils.i( "!!!!! diff=" + diff);
                    newAngle = (byte) (diff + motorAngle.data);
                }
                if (newAngle > 0)
                    RobotOpsManager.get(mContext).executeOp(new MoveAbsAngleOp((byte)19, newAngle, (short)500));
            } else if (SysUtils.is5Mic()){
                int angle = event.angle;
                int mCurrentAngle = 0;
                if(angle > 270) { /** 机器人的左边 **/
                    mCurrentAngle = (byte)(angle - 360);
                } else if(angle < 90) {/** 机器人的右边 **/
                    mCurrentAngle = (byte)(angle);
                }
                newAngle = (byte) (motorAngle.data + mCurrentAngle);
                if (newAngle > 0)
                    RobotOpsManager.get(mContext).executeOp(new MoveAbsAngleOp((byte)19, newAngle, (short)500));
            }
            LogUtils.i( "!!!!! newAngle=" + newAngle);
        }
    };

    public ProxySoundDirectionImpl(Context cxt) {
        this.mContext = cxt;
    }

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(SoundDirectionEvent.class, mSoundDireSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(SoundDirectionEvent.class, mSoundDireSubscriber);
    }
}
