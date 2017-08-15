package com.ubtechinc.alpha.ops.led.mic5;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class LedConstant {
    /**颜色定义：
     *  1 R, 2 G, 3 B,4 RG,5 RB,6 GB,7 RGB **/
    public static final int LED_RED = 1;
    public static final int LED_GREEN = 2;
    public static final int LED_BLUE = 3;
    public static final int LED_RED_GREEN = 4;
    public static final int LED_RED_BLUE = 5;
    public static final int LED_GREEN_BLUE = 6;
    public static final int LED_RED_GREEN_BLUE = 7;

    //左边五个灯对应的bit值
    public static final byte LED_HEAD_R0 = 0x00;
    public static final byte LED_HEAD_R1 = 0x10;
    public static final byte LED_HEAD_R2 = 0x08;
    public static final byte LED_HEAD_R3 = 0x04;
    public static final byte LED_HEAD_R4 = 0x02;
    public static final byte LED_HEAD_R5 = 0x01;

    //右边五个灯对应的bit值
    public static final byte LED_HEAD_L0 = 0x00;
    public static final byte LED_HEAD_L1 = 0x01;
    public static final byte LED_HEAD_L2 = 0x02;
    public static final byte LED_HEAD_L3 = 0x04;
    public static final byte LED_HEAD_L4 = 0x08;
    public static final byte LED_HEAD_L5 = 0x10;

    public static final byte[] leftHeadLights={LED_HEAD_L0,LED_HEAD_L1, LED_HEAD_L1 | LED_HEAD_L2, LED_HEAD_L1 | LED_HEAD_L2 | LED_HEAD_L3,LED_HEAD_L1 | LED_HEAD_L2 | LED_HEAD_L3 | LED_HEAD_L4,LED_HEAD_L1 | LED_HEAD_L2 | LED_HEAD_L3 | LED_HEAD_L4 | LED_HEAD_L5};
    public static final byte[] rightHeadLights={LED_HEAD_R0,LED_HEAD_R1,LED_HEAD_R1 | LED_HEAD_R2,LED_HEAD_R1 | LED_HEAD_R2 | LED_HEAD_R3,LED_HEAD_R1 | LED_HEAD_R2 | LED_HEAD_R3 | LED_HEAD_R4,LED_HEAD_R1 | LED_HEAD_R2 | LED_HEAD_R3 | LED_HEAD_R4 | LED_HEAD_R5};

    //声音按键持续的时间
    public static final int VOLUME_LED_OPERATION_TIME=2000;

    //前后按键的分类
    public static final int HEAD_FORM_KEY=1;
    public static final int HEAD_BACK_KEY=2;
    //前后按键的on off
    public static final int HEAD_FORM_KEY_LED_ON=1;
    public static final int HEAD_FORM_KEY_LED_OFF=2;
    public static final int HEAD_BACK_KEY_LED_ON=3;
    public static final int HEAD_BACK_KEY_LED_OFF=4;
    public static final int HEAD_BACK_FORM_KEY_LED_ON=5;
    public static final int HEAD_BACK_FORM_KEY_LED_OFF=6;
}
