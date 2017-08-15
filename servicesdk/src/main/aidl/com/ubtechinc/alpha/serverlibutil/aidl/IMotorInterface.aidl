// IMotorService.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

import com.ubtechinc.alpha.serverlibutil.aidl.IMotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IMotorSetAllAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;

/**
* 舵机服务接口
*/
interface IMotorInterface {

    /**
     * @Description 获取机器人上所有舵机的信息（异步）
     * @param onListener 获取机器人上所有舵机的信息的回调
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int getMotorList(IMotorListResultListener onListener);

    /**
     * @Description 将某舵机移动到距离原点的绝对角度(单位弧度)（异步）
     * @param nMotorId --- 舵机id
     * @param nRadian --- 相对原点移动的绝对弧度，单位pi, 范围 -pi 至pi
     * @param duration -- 时间
     * @param onListener 移动角度结果监听者
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int moveToAbsoluteAngle(int nMotorId, int nRadian, long duration, IMotorMoveAngleResultListener onListener);

    /**
     * @Description 将某舵机移动到距离原点的绝对角度(单位弧度)（异步）
     * @param nMotorId --- 舵机id
     * @param nRadian --- 相对原点移动的绝对弧度，单位pi, 范围 -pi 至pi
     * @param duration --- 时间
     * @param onListener 移动角度结果监听者
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int moveRefAngle(int nMotorId, int nRadian,long duration, IMotorMoveAngleResultListener onListener);

    /**
     * @Description 读取指定舵机的绝对角度(单位弧度)（异步）
     * @param nMotorId --- 舵机id
     * @param acdump --- 是否掉电读取
     * @param onListener 读取舵机角度结果监听者
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int readAbsoluteAngle(int nMotorId, boolean acdump, IMotorReadAngleListener onListener);

    /**
     * @Description 指定所有舵机移动到绝对角度(单位弧度)（异步）
     * @param motors --- 舵机信息
     * @param duration --- 移动消耗事件
     * @param onListener 指定所有舵机移动到绝对角度结果监听者
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int SetAllMotorAbsoluteAngle(in MotorAngle[] motors, long duration, IMotorSetAllAngleResultListener onListener);

    /**
     * @Description 设置舵机省电模式
     * @param savepower --
     */
    void setPowerSaveMode(boolean savepower);
}
