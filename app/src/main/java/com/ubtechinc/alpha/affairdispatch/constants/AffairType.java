package com.ubtechinc.alpha.affairdispatch.constants;

/**
 * @desc : 事务类型定义
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public enum AffairType {
    //TTS播报
    EVENT_TYPE_TTS,
    //动作
    EVENT_TYPE_ACTION,

    //同时执行TTS和动作
    EVENT_TYPE_TTS_ACTION,
    //胸板更新
    EVENT_TYPE_CHEST_UPDATE,
    //头部版更新
    EVENT_TYPE_HEADER_UPDATE,
    //舵机版更新
    EVENT_TYPE_MOTOR_UPDATE
}
