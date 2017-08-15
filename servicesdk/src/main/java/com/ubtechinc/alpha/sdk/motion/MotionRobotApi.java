package com.ubtechinc.alpha.sdk.motion;

import android.content.Context;

import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorReadAngleListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorSetAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;
import com.ubtechinc.alpha.serverlibutil.service.ActionServiceUtil;
import com.ubtechinc.alpha.serverlibutil.service.MotorServiceUtil;
import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * <pre>
 *   @author: Logic
 *   @email : logic.peng@ubtech.com
 *   @time  : 2017/4/6
 *   @desc  : 机器人动作控制api
 * </pre>
 */
public final class MotionRobotApi {
    private ActionServiceUtil mActionServiceUtil;
    private MotorServiceUtil mMotorServiceUtil;
    private static volatile MotionRobotApi mMotionRobotApi;

    public static MotionRobotApi get(){
        if (null == mMotionRobotApi){
            synchronized (MotionRobotApi.class){
                mMotionRobotApi = new MotionRobotApi();
            }
        }
        return mMotionRobotApi;
    }

    public synchronized MotionRobotApi initializ(Context context) {
        if (mActionServiceUtil == null) {
            mActionServiceUtil = new ActionServiceUtil(context);
        }
        if (mMotorServiceUtil == null) {
            mMotorServiceUtil = new MotorServiceUtil(context);
        }
        return this;
    }

    public synchronized void destroy() {
        if (mActionServiceUtil != null) {
            mActionServiceUtil.clear();
            mActionServiceUtil = null;
        }
        mMotorServiceUtil = null;
    }

    /**
     * 获取动作列表
     * @param listener
     * @return err > 0表示此次操作id：<=0: 参考UtilsConstant.ErrorCode
     */
    public int getActionList(ActionListResultListener listener){
        int ret;
        if (mActionServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mActionServiceUtil.getActionList(listener);
        return ret;
    }

    /**
     * 执行一个动作
     * @param actionName 动作明
     * @param listener
     * @return err > 0表示此次操作id：<=0: 参考UtilsConstant.ErrorCode
     */
    public int playAction(String actionName, ActionResultListener listener){
        int ret;
        if (mActionServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mActionServiceUtil.playAction(actionName, listener);
        return ret;
    }

    /**
     * 停止当前动作
     * @param listener
     * @return errCode: 参考UtilsConstant.ErrorCode
     */
    public int stopAction(StopActonResultListener listener){
        int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
        if (mActionServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mActionServiceUtil.stopAction(listener);
        return ret;
    }

    /**
     * 获取舵机列表
     * @param listener
     * @return err > 0表示此次操作id：<=0: 参考UtilsConstant.ErrorCode
     */
    public int getMotorList(MotorListResultListener listener){
        int ret = SdkConstants.ErrorCode.RESULT_FAIL;
        if (mMotorServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mMotorServiceUtil.getMotorList(listener);
        return ret;
    }

    /**
     *  指定舵机移动到绝对角度
     *
     * @param nMotorId 舵机ID，alpha2上：1～20
     * @param nRadian 舵机相对原点的绝对角度
     * @param duration 时间
     * @param onListener
     * @return err > 0表示此次操作id：<=0: 参考UtilsConstant.ErrorCode
     */
    public int moveToAbsoluteAngle(int nMotorId, int nRadian, short duration, MotorMoveAngleResultListener onListener){
        int ret = SdkConstants.ErrorCode.RESULT_FAIL;
        if (mMotorServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mMotorServiceUtil.moveToAbsoluteAngle(nMotorId, nRadian, duration, onListener);
        return ret;
    }

    /**
     *
     * @param nMotorId 舵机ID，alpha2上：1～20
     * @param nRadian 舵机相对原点的绝对角度
     *                @param duration 时间
     * @param onListener
     * @return err > 0表示此次操作id：<=0: 参考UtilsConstant.ErrorCode
     */
    public int moveRefAngle(int nMotorId, int nRadian, short duration, MotorMoveAngleResultListener onListener){
        int ret = SdkConstants.ErrorCode.RESULT_FAIL;
        if (mMotorServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mMotorServiceUtil.moveRefAngle(nMotorId, nRadian, duration, onListener);
        return ret;
    }

    /**
     * 读取舵机角度
     * @param nMotorId
     * @param acdump 是否掉电
     * @param listener
     * @return
     */
    public int readAbsoluteAngle(int nMotorId, boolean acdump, MotorReadAngleListener listener) {
        int ret = SdkConstants.ErrorCode.RESULT_FAIL;
        if (mMotorServiceUtil == null){
            ret = SdkConstants.ErrorCode.RESULT_FAIL;
            return ret;
        }
        ret = mMotorServiceUtil.readAbsAngle(nMotorId, acdump, listener);
        return ret;
    }

    /**
     * 指定一组舵机移动到指定角度，需满足舵机参数限制
     * @param motors 一组舵机
     * @param duration 时间
     * @param onListener 回调
     * @return
     */
    public int setMotorAbsoluteAngles(MotorAngle[] motors, short duration, final MotorSetAngleResultListener onListener){
        if (mMotorServiceUtil == null){
            return SdkConstants.ErrorCode.RESULT_FAIL;
        }
        return mMotorServiceUtil.SetAllMotorAbsoluteAngle(motors, duration, onListener);
    }


    /**
     * 设置舵机省电状态
     * @param enable
     */
    public void setPowerSaveMode(boolean enable){
        if (mMotorServiceUtil == null) return;
        mMotorServiceUtil.setPowerSaveMode(enable);
    }
}
