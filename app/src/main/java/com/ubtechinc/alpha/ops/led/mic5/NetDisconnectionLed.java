package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.jni.LedControl;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class NetDisconnectionLed extends BaseLed {

    public NetDisconnectionLed(int priority,LedType ledType){
        super(priority,ledType);
    }
    @Override
    void operationLed() {
        //眼睛和头顶灯黄灯闪烁,wifi灯红色常亮
        LedControl.open();
        LedControl.ledSetOn(13);
        LedControl.ledSetEye(LedConstant.LED_RED_GREEN,9,255,255,400,100,Integer.MAX_VALUE,0);
        LedControl.ledSetHead(LedConstant.LED_RED_GREEN,9,255,255,400,100,Integer.MAX_VALUE,0);
        LedControl.close();
    }
}
