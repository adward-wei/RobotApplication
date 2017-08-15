// IMotorMoveAngleResultListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

/**
* 舵机移动角度回调
*/
interface IMotorMoveAngleResultListener {

    /**
     * @Description 舵机移动绝对角度回调
     * @param nOpId moveToAbsoluteAngle的返回值，标识哪一次moveToAbsoluteAngle
     * @param nErr 此次操作成功或者失败返回值，0表示成功，1表示失败
     * @param nRadian 移动后的新舵机角度
     * @return void
     * @throws
     */
    void onMoveAngle(int nOpId, int nErr, int nRadian);

}
