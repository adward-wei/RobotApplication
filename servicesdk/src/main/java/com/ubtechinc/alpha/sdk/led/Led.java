package com.ubtechinc.alpha.sdk.led;

/**
 * @desc : copy from 主服务
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public enum Led {
    HEAD(1) ,EYE(2) ,MOUTH(3),EAR(4),CHEST(5);
    public final int value;
    Led(int value){
        this.value = value;
    }

    public static Led valueOf(int value){
        switch (value){
            case 1:
                return HEAD;
            case 2:
                return EYE;
            case 3:
                return MOUTH;
            case 4:
                return EAR;
            case 5:
                return CHEST;
            default:
                return null;
        }
    }
}
