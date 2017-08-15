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
     * @param nErr 0表示动作正常完成，1表示动作被新动作执行时停止， 2表示动作被stopAction停止
     * @return
     * @throws
     */
    void onPlayActionResult(int nOpId, int nErr);

    /**
     * @Description 停止一个动作回调方法（异步）
     * @param nErr 0表示停止动作成功， 3表示停止动作失败
     * @return
     * @throws
     */
    void onStopActionResult(int nErr);
}
