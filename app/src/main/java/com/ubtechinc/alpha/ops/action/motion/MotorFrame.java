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

package com.ubtechinc.alpha.ops.action.motion;

import com.ubtech.utilcode.utils.ByteBufferList;

import static com.ubtechinc.alpha.ops.action.Constraints.MOTOR_EXTRAS_LENGTH;

/**
 * @desc : 舵机帧model
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/28
 * @modifier:
 * @modify_time:
 */

public class MotorFrame extends Frame implements Cloneable{

    private byte[] motorData = new byte[MOTOR_EXTRAS_LENGTH];

    @Override
    public final boolean analysis(ByteBufferList bbl, int length) {
        boolean ret = super.analysis(bbl, length);
        if (!ret) return false;
        if(MOTOR_EXTRAS_LENGTH != bbl.remaining()) return false;
        bbl.get(motorData);
        return true;
    }

    @Override
    protected final byte[] getExtra(){
        return motorData;
    }


    MotorFrame() {}

    @Override
    public Frame clone() throws CloneNotSupportedException {
        MotorFrame newObj = (MotorFrame) super.clone();
        newObj.motorData = new byte[motorData.length];
        System.arraycopy(motorData,0,newObj.motorData,0,motorData.length);
        return newObj;
    }

    public byte[] getMotorData() {
        return motorData;
    }
}
