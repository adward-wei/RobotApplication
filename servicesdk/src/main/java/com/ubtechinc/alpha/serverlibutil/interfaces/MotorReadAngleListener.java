package com.ubtechinc.alpha.serverlibutil.interfaces;

/**
 * @desc : 读取舵机角度客户端回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public interface MotorReadAngleListener {
    void onReadMotorAngle(int nOpId, int nErr, int angle);
}
