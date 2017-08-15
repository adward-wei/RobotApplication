package com.ubtechinc.alpha.ops;

import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;


/**
 * description: 动作服务接口，在IActionService的基础上进行接口扩展给主服务内部调用
 * IActionService：是SDK提供给外部调用的
 * autour: bob.xu
 * date: 2017/6/30 16:05
 * update: 2017/6/30
 * version: a
*/
public interface IActionServiceInner{
    /**
     * @Description 获取机器人上已有Action的信息列表（异步）
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int getActionList(ActionListResultListener onResultListener);


    /**
     * @Description 让机器人做指定的动作（异步）
     * @param strActionName 动作名称
     * @param onListener 动作结果回调
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int playAction(String strActionName, ActionPlayListener onListener);

    /**
     * @Description 停止当前动作回调方法（异步）
     * @param onListener 停止动作结果回调
     * @return
     * @throws
     */
    void stopAction(StopActonResultListener onListener);
}
