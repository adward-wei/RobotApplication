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

package com.ubtechinc.alpha.ops;

import android.content.Context;

import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;
import com.ubtechinc.alpha.utils.IDGenerator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc : 流程图动作对外接口实现
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/16
 * @modifier:
 * @modify_time:
 */
public class ActionServiceImpl implements IActionServiceInner {
    private static Context mContext = AlphaApplication.getContext();
    private Lock mLock = new ReentrantLock();
    ActionServiceImpl(){}

    @Override
    public int getActionList(final ActionListResultListener onResultListener) {
        mLock.lock();
        try {
            final int id = IDGenerator.get().id();
            RobotOpsManager.get(mContext).getActionList(id, onResultListener);
            return id;
        }finally {
            mLock.unlock();
        }
    }

    @Override
    public int playAction(String strActionName, final ActionPlayListener onListener) {
        mLock.lock();
        try {
            final int id = IDGenerator.get().id();
            if (!RobotOpsManager.get(mContext).isInit()) {
                handlePlayActionError(onListener, SdkConstants.ErrorCode.RESULT_FAIL);
                return SdkConstants.ErrorCode.RESULT_FAIL;
            }
            RobotOpsManager.get(mContext).playAction(strActionName, onListener);
            return id;
        }finally {
            mLock.unlock();
        }
    }

    @Override
    public void stopAction(final StopActonResultListener onListener) {
        mLock.lock();
        try {
            if (!RobotOpsManager.get(mContext).isInit()) {
                onListener.onStopActionResult(SdkConstants.ErrorCode.DEVICE_NOT_INITED);
            } else {
                RobotOpsManager.get(mContext).stopAction(onListener);
            }
        }finally {
            mLock.unlock();
        }
    }

    private void handlePlayActionError(final ActionPlayListener onListener, final int nErr) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                if(onListener != null)
                    onListener.onActionResult(nErr);
            }
        });
    }
}
