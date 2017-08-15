package com.ubt.alpha2.upgrade.impl;


/**
 * @author: slive
 * @description: 同步任务接口
 * @create: 2017/7/20
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public interface ISyncTask extends Runnable{
    void waitForResponse();
    void notifyForExecute();
}
