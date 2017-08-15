package com.ubtechinc.alpha.serverlibutil.interfaces;

import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * @desc : 舵机移动角度回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public interface MotorMoveAngleResultListener {

    /**
     * @Description 舵机移动绝对角度回调
     * @param nOpId moveToAbsoluteAngle的返回值，标识哪一次moveToAbsoluteAngle
     * @param nErr {@link com.ubtechinc.alpha.sdk.SdkConstants.ErrorCode}
     * @param nRadian 移动后的新角度
     * @return void
     * @throws
     */
    void onMoveAngle(int nOpId, int nErr, int nRadian);
}
