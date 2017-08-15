// IMotorListResultListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

import com.ubtechinc.alpha.serverlibutil.aidl.MotorInfo;
/**
*
* 舵机信息列表回调接口
*/
interface IMotorListResultListener {

    /**
     * @Description 获取机器人上所有舵机的信息的回调接口
     * @param nOpId getMotorList的返回值，标识是哪一次getMotorList的回调
     * @param nErr {@link UtilsConstant.OpResult}
     * @param onArrMotorInfo 当nErr为0时，此Array返回的是所有舵机的信息
     * @return
     * @throws
     */
   void onGetMotorList(int nOpId, int nErr, in MotorInfo[] onArrMotorInfo);
}
