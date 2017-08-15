package com.ubtechinc.alpha.sdk.led;

/**
 * @desc : copy from 主服务
 * （红）+（绿）=（黄）（蓝）+（绿）=（青）（红）+（蓝）=（品红）（绿）+（蓝）+（红）=（白）
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/11
 * @modifier:
 * @modify_time:
 */

public enum LedColor {
    NULL(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    YELLOW(4),
    MAGENTA(5),//品红
    CYAN(6),//青色
    WHITE(7),
    BLACK(8);

    public final int value;
    LedColor(int value) {
        this.value = value;
    }

    public static LedColor valueOf(int value){
        switch (value){
            case 1:
                return RED;
            case 2:
                return GREEN;
            case 3:
                return BLUE;
            case 4:
                return YELLOW;
            case 5:
                return MAGENTA;
            case 6:
                return CYAN;
            case 7:
                return WHITE;
            case 8:
                return BLACK;
            default:
                return null;
        }
    }
}