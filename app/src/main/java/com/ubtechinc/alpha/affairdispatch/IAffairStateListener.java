package com.ubtechinc.alpha.affairdispatch;

/**
 * @desc : 事务状态回调
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public interface IAffairStateListener {
    void onAffairComplete(AbstractAffair event);//执行完成
    void onAffairTimeOut(AbstractAffair event); //在规定时间没收到回调
}
