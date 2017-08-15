package com.ubtechinc.alpha.affairdispatch.constants;

/**
 * @desc : 事务状态定义
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public enum AffairState {
    // 准备
    STATE_PREPARE,
    // 开始
    STATE_START,
    // 阻塞
    STATE_BLOCK,
    // 中断
    STATE_PAUSE,
    // 恢复
    STATE_RESUME,
    // 停止
    STATE_STOP
}
