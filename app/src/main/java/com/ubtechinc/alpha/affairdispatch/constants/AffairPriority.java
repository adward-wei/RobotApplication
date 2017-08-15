package com.ubtechinc.alpha.affairdispatch.constants;

/**
 * @desc : 事务优先级定义
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public class AffairPriority {
    // 最高优先级，用于关机、升级等事件
    public static final int PRIORITY_MAX = 5;
    // 高优先级
    public static final int PRIORITY_HIGH = 4;
    // 标准优先级，用于一般的动作、TTS事件
    public static final int PRIORITY_NORMAL = 3;
    // 低优先级
    public static final int PRIORITY_LOW = 2;
    // 最低优先级
    public static final int PRIORITY_MIN = 1;
}
