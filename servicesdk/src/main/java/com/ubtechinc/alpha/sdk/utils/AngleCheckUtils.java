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

package com.ubtechinc.alpha.sdk.utils;

/**
 * @desc : 舵机角度设置检查
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/22
 * @modifier: logic
 * @modify_time: 2017／5／25 舵机角度限制
 */

public final class AngleCheckUtils {

    /**
     * 20个舵机
     * @param data
     */
    public static void checkData(int[] data) {
        if (data != null && data.length == 20) {
            for (int i = 1; i <= data.length ; i++){
                data[i-1] = limitAngle(i, data[i-1]);
            }
        }
    }

    /**
     * 单个舵机
     * @param motorId
     * @param angle
     * @return
     */
    public static int limitAngle(int motorId, int angle){
        if (angle == 250) return angle;
        if (angle < getLowerLimitAngle(motorId))
            return getLowerLimitAngle(motorId);
        else if (angle > getUpperLimitAngle(motorId))
            return getUpperLimitAngle(motorId);
        else
            return angle;
    }

    public static int getUpperLimitAngle(int motorId){
        return MOTOR_ANGLE_LIMIT[(motorId -1)*2 + 1];
    }

    public static int getLowerLimitAngle(int motorId){
        return MOTOR_ANGLE_LIMIT[2*(motorId -1)];
    }

    public static final int[] MOTOR_ANGLE_LIMIT = {
            5,235,//1
            50,210,//2
            55,185,//3
            5,235,//4
            30,190,//5
            55,185,//6
            100,200,//7
            20,220,//8
            35,230,//9
            35,215,//10
            100,190,//11
            40,140,//12
            20,220,//13
            10,205,//14
            25,205,//15
            50,140,//16
            95,125,//17
            95,125,//18
            75,165,//19
            105,155,//20
    };
}
