package com.ubtechinc.alpha.serverlibutil.aidl;


interface IMotorSetAllAngleResultListener {
    /**
     * @Description 所有舵机移动到各自的绝对角度回调
     * @param nOpId SetAllMotorAbsoluteAngles的返回值，标识哪一次SetAllMotorAbsoluteAngles
     * @param nErr 此次操作成功或者失败返回值，0表示成功，1表示失败
     * @return void
     * @throws
     */
    void onSetAllAngle(int nOpId, int nErr);
}
