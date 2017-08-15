/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.event;

/**
 * @desc : 按键上报事件
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public final class KeyPressEvent extends BaseEvent{
    //值取1-6
    /**
     * 1: mute key 短按
     * 2:
     * 3:
     * 4:音量加
     * 5:音量减
     * 6:复位
     * 7:音量-键长按
     */
    public byte keyType;
}
