// IActionListResultListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
/**
* 获取机器人Action列表信息结果回调
*/
interface IActionListResultListener {

    /**
	 * @Description 获取机器人动作列表信息结果
	 * @param nOpId getActionList返回的操作id,
	 * @param nErr {@link UtilsConstant.OpResult}
	 * @param onArrAction 返回的Action信息列表
	 * @return void
	 * @throws
	 */
    void onGetActionList(int nOpId, int nErr, in ActionInfo[] onArrAction);
}
