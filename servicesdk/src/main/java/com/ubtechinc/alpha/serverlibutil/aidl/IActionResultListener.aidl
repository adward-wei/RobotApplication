// IActionResultListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

// Declare any non-default types here with import statements
/**
* 动作回调接口
*/
interface IActionResultListener {

    /**
     * @Description 执行一个动作回调方法（异步）
     * @param nOpId playAction返回的opid, 代表对应的一次playAction
     * @param nErr {@link UtilsConstant.OpResult}
     * @return
     * @throws
     */
    void onPlayActionResult(int nOpId, int nErr);

    /**
     * @Description 停止一个动作回调方法（异步）
     * @param nErr {@link UtilsConstant.OpResult}
     * @return
     * @throws
     */
    void onStopActionResult(int nErr);
}
