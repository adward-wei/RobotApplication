package com.ubtechinc.alpha.serverlibutil.interfaces;

import com.ubtechinc.alpha.serverlibutil.aidl.MotorInfo;
import com.ubtechinc.alpha.sdk.SdkConstants;

import java.util.List;

/**
 * @desc : 舵机信息列表回调接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public interface MotorListResultListener {

    /**
     * @Description 获取机器人上所有舵机的信息的回调接口
     * @param nOpId getMotorList的返回值，标识是哪一次getMotorList的回调
     * @param nErr {@link SdkConstants.OpResult}
     * @param onArrMotorInfo 当nErr为0时，此Array返回的是所有舵机的信息
     * @return
     * @throws
     */
    void onGetMotorList(int nOpId, int nErr,  List<MotorInfo> onArrMotorInfo);
}
