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

package com.ubtechinc.alpha.serverlibutil.service;

import android.content.Context;
import android.os.RemoteException;

import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorInterface;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorSetAllAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorSetAngleResultListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @desc : 舵机服务 客户端链接助手类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public final class MotorServiceUtil{
    private IMotorInterface mService;

    public MotorServiceUtil(Context context){
        mService =  IMotorInterface.Stub.asInterface(BinderFetchServiceUtil.get(context).getServiceBinder(SdkConstants.MOTOR_BINDER_NAME));
    }

    public void reset(){
    }

    public int getMotorList(final MotorListResultListener onListener) {
        int ret ;
        try {
            ret = mService.getMotorList(new IMotorListResultListener.Stub(){

                @Override
                public void onGetMotorList(int nOpId, int nErr, MotorInfo[] onArrMotorInfo) throws RemoteException {
                    if (onListener != null){
                        onListener.onGetMotorList(nOpId,nErr, new ArrayList<MotorInfo>(Arrays.asList(onArrMotorInfo)));
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        }catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int moveToAbsoluteAngle(int nMotorId, int nRadian, long duration, final MotorMoveAngleResultListener onListener) {
        int ret ;
        try {
            ret = mService.moveToAbsoluteAngle(nMotorId, nRadian, duration, new IMotorMoveAngleResultListener.Stub() {
                @Override
                public void onMoveAngle(int nOpId, int nErr, int nRadian) throws RemoteException {
                    if (onListener != null)
                        onListener.onMoveAngle(nOpId, nErr, nRadian);
                }
            });
        } catch (RemoteException e) {
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int moveRefAngle(int nMotorId, int nRadian, long duration, final MotorMoveAngleResultListener onListener){
        int ret ;
        try {
            ret = mService.moveRefAngle(nMotorId, nRadian, duration, new IMotorMoveAngleResultListener.Stub() {
                @Override
                public void onMoveAngle(int nOpId, int nErr, int nRadian) throws RemoteException {
                    if (onListener != null){
                        onListener.onMoveAngle(nOpId, nErr, nRadian);
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        } catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int readAbsAngle(int nMotorId, boolean acdump, final MotorReadAngleListener listener){
        int ret ;
        try {
            ret = mService.readAbsoluteAngle(nMotorId, acdump, new IMotorReadAngleListener.Stub(){
                @Override
                public void onReadMotorAngle(int nOpId, int nErr, int angle) throws RemoteException {
                    if (listener != null)
                        listener.onReadMotorAngle(nOpId, nErr, angle);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        } catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int SetAllMotorAbsoluteAngle(MotorAngle[] motors, long duration, final MotorSetAngleResultListener onListener){
        int ret;
        try {
            ret = mService.SetAllMotorAbsoluteAngle(motors, duration, new IMotorSetAllAngleResultListener.Stub(){

                @Override
                public void onSetAllAngle(int nOpId, int nErr) throws RemoteException {
                    if (onListener != null )
                        onListener.onSetMotorAngles(nOpId, nErr);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        } catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public void setPowerSaveMode(boolean b){
        try {
            mService.setPowerSaveMode(b);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
