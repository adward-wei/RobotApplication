// IActionService.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionResultListener;

/**
*  动作服务接口
*/
interface IActionService {

    /**
     * @Description 获取机器人上已有Action的信息列表（异步）
     * @param onListener 获取机器人Action列表信息结果回调
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int getActionList(IActionListResultListener onResultListener);


    /**
     * @Description 让机器人做指定的动作（异步）
     * @param strActionName 动作名称
     * @param onListener 动作结果回调
     * @return int 0 --- 操作失败， >=1 --- 代表此次操作的id
     * @throws
     */
    int playAction(String strActionName, IActionResultListener onListener);

    /**
     * @Description 停止当前动作回调方法（异步）
     * @param onListener 停止动作结果回调
     * @return
     * @throws
     */
    void stopAction(IActionResultListener onListener);
}
