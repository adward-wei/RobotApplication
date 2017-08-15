package com.ubtechinc.alpha.serverlibutil.interfaces;

import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;

import java.util.List;

/**
 * @desc : 获取机器人Action列表信息结果回调
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public interface ActionListResultListener {

    /**
     * @Description 获取机器人动作列表信息结果
     * @param nOpId getActionList返回的操作id,
     * @param nErr 此次操作成功或者失败返回值，0表示成功，1表示失败
     * @param onArrAction 返回的Action信息列表
     * @return
     * @throws
     */
    void onGetActionList(int nOpId, int nErr, List<ActionInfo> onArrAction);
}
