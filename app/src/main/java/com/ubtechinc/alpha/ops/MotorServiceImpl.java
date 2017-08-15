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
import android.os.RemoteException;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.ops.motor.MoveAbsAngleOp;
import com.ubtechinc.alpha.ops.motor.MoveRefAngleOp;
import com.ubtechinc.alpha.ops.motor.ReadAngleOp;
import com.ubtechinc.alpha.ops.motor.SendMotorDataOp;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorInterface;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorSetAllAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorInfo;
import com.ubtechinc.alpha.utils.AngleCheckUtils;
import com.ubtechinc.alpha.utils.IDGenerator;
import com.ubtechinc.alpha.utils.SysUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc : 舵机服务对外接口实现
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/16
 * @modifier:
 * @modify_time:
 */

public class MotorServiceImpl extends IMotorInterface.Stub {
    private static Context mContext;
    private Lock mAccessLock = new ReentrantLock();

    private MotorServiceImpl(){}

    private static class InstanceHolder {
        public static MotorServiceImpl instance = new MotorServiceImpl();
    }

    public static MotorServiceImpl get(Context context){
        mContext= context.getApplicationContext();
        return InstanceHolder.instance;
    }

    @Override
    public int getMotorList(final IMotorListResultListener onListener) throws RemoteException {
        final int id = IDGenerator.get().id();
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                MotorInfo[] infos = new MotorInfo[20];
                for (int i= 1 ; i <= 20 ; i++){
                    infos[i-1] = new MotorInfo(i, AngleCheckUtils.getUpperLimitAngle(i),
                            AngleCheckUtils.getLowerLimitAngle(i),
                            -1, -1);
                }
                try {
                    onListener.onGetMotorList(id, SdkConstants.ErrorCode.RESULT_SUCCESS, infos);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    @Override
    public int moveToAbsoluteAngle(int nMotorId, int nRadian, long duration, final IMotorMoveAngleResultListener onListener) throws RemoteException {
        final int id = IDGenerator.get().id();
        if (!checkParametersValid(nMotorId)) {
            handMoveAngleError(onListener,id, SdkConstants.ErrorCode.PARAMETER_ERROR);
            return id;
        }
        if (RobotState.get().isInPowerSave()|| RobotState.get().isUpgradeModeOpened()){
            handMoveAngleError(onListener,id, SdkConstants.ErrorCode.ROBOT_STATE_NOT_SUPPORTED);
            return id;
        }
        final int newAngle = AngleCheckUtils.limitAngle(nMotorId, nRadian);
        RobotOpsManager.get(mContext).executeOp(new MoveAbsAngleOp((byte)nMotorId, newAngle, (short)duration), new SerialOpResultListener() {
            @Override
            public void onRecvOpResult(OpResult result) {
                try {
                    onListener.onMoveAngle(id, result.errorCode == SerialConstants.ERR_OK?
                            SdkConstants.ErrorCode.RESULT_SUCCESS : result.errorCode, newAngle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    private void handMoveAngleError(final IMotorMoveAngleResultListener onListener, final int id, final int nErr) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                try {
                    onListener.onMoveAngle(id, nErr, -1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int moveRefAngle(int nMotorId, int nRadian,long duration, final IMotorMoveAngleResultListener onListener) throws RemoteException {
        final int id = IDGenerator.get().id();
        if (!checkParametersValid(nMotorId)) {
            handMoveAngleError(onListener,id, SdkConstants.ErrorCode.PARAMETER_ERROR);
            return id;
        }
        if (RobotState.get().isInPowerSave()|| RobotState.get().isUpgradeModeOpened()){
            handMoveAngleError(onListener,id, SdkConstants.ErrorCode.ROBOT_STATE_NOT_SUPPORTED);
            return id;
        }
        final int newAngle = AngleCheckUtils.limitAngle( nMotorId, nRadian);
        RobotOpsManager.get(mContext).executeOp(new MoveRefAngleOp((byte) nMotorId, newAngle, (short)duration), new SerialOpResultListener() {
            @Override
            public void onRecvOpResult(OpResult result) {
                try {
                    onListener.onMoveAngle(id, result.errorCode == SerialConstants.ERR_OK?
                            SdkConstants.ErrorCode.RESULT_SUCCESS : result.errorCode, newAngle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    @Override
    public int readAbsoluteAngle(final int nMotorId, boolean acdump, final IMotorReadAngleListener onListener) throws RemoteException {
        final int id = IDGenerator.get().id();
        if (!checkParametersValid(nMotorId)) {
            handleReadMotorAngle(onListener,id,SdkConstants.ErrorCode.PARAMETER_ERROR);
            return id;
        }
        if (RobotState.get().isInPowerSave()|| RobotState.get().isUpgradeModeOpened() && acdump){
            handleReadMotorAngle(onListener,id,SdkConstants.ErrorCode.ROBOT_STATE_NOT_SUPPORTED);
            return id;
        }
        RobotOpsManager.get(mContext).executeOp(new ReadAngleOp((byte) nMotorId, acdump), new SerialOpResultListener<Short>() {
            @Override
            public void onRecvOpResult(OpResult<Short> result) {
                try {
                    if(result == null || result.data == null)
                        return ;
                    onListener.onReadMotorAngle(id, result.errorCode == SerialConstants.ERR_OK ?
                            SdkConstants.ErrorCode.RESULT_SUCCESS : result.errorCode, result.errorCode == SerialConstants.ERR_OK?result.data:-1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    private void handleReadMotorAngle(final IMotorReadAngleListener onListener, final int id, final int nErr) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                try {
                    onListener.onReadMotorAngle(id, nErr ,-1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int SetAllMotorAbsoluteAngle(MotorAngle[] motors, long duration, final IMotorSetAllAngleResultListener onListener) throws RemoteException{
        final int id = IDGenerator.get().id();
        if (!checkParametersValid(motors)) {
            handleSetAllAngleError(onListener, id, SdkConstants.ErrorCode.PARAMETER_ERROR);
            return id;
        }
        if (RobotState.get().isInPowerSave()|| RobotState.get().isUpgradeModeOpened()){
            handleSetAllAngleError(onListener, id, SdkConstants.ErrorCode.ROBOT_STATE_NOT_SUPPORTED);
            return id;
        }
        RobotOpsManager.get(mContext).executeOp(new SendMotorDataOp(assembleMotorAngle(motors), (short) duration), new SerialOpResultListener() {
            @Override
            public void onRecvOpResult(OpResult result) {
                try {
                    onListener.onSetAllAngle(id, result.errorCode == SerialConstants.ERR_OK
                            ? SdkConstants.ErrorCode.RESULT_SUCCESS
                            : result.errorCode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return id;
    }

    private void handleSetAllAngleError(final IMotorSetAllAngleResultListener onListener, final int id, final int nErr) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                try {
                    onListener.onSetAllAngle(id, nErr);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setPowerSaveMode(final boolean enable) throws RemoteException {
        mAccessLock.lock();
        try {
            if ((!RobotState.get().isInPowerSave() && enable) ||
                    (!enable && RobotState.get().isInPowerSave())) {
                handlePowerSaveMode(enable);
            }
        }finally {
            mAccessLock.unlock();
        }
    }

    private void handlePowerSaveMode(final boolean enable) {
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                RobotTakeARest.instance().start(enable);
            }
        });
    }

    private boolean checkParametersValid(int nMotorId) {
        if (nMotorId < 1 || nMotorId > 20) return false;
        if (!RobotOpsManager.get(mContext).isInit()) return false;
        return true;
    }

    private boolean checkParametersValid(MotorAngle[] motors) {
        if (motors == null || motors.length == 0) return false;
        for (int i = 0 ; i < motors.length; i++){
            if (!checkParametersValid(motors[i].getId())
                    || AngleCheckUtils.checkAngle(motors[i].getId(), motors[i].getAngle()))
                return false;
        }
        return true;
    }

    private byte[] assembleMotorAngle(MotorAngle[] motorAngles){
        byte[] data = new byte[20];
        for (int i = 0; i < data.length; i++){
            data[i] = ConvertUtils.h_int2Byte(250)[3];
        }
        for (int i = 0; i < motorAngles.length; i++){
            data[motorAngles[i].getId() - 1] = ConvertUtils.h_int2Byte(motorAngles[i].getAngle())[3];
        }
        if (SysUtils.is2Mic()){
            data[18] = 0;
            data[19] = 0;
        }
        return data;
    }
}
