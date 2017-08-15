package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.jni.LedControl;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class NetConnectedLed extends BaseLed {

    public NetConnectedLed(int priority, LedType ledType){
        super(priority,ledType);
    }
    @Override
    void operationLed() {
        //头顶灯黄灯关闭,wifi灯蓝灯常亮,开始眨眼睛
        LedControl.open();
        LedControl.ledSetOn(12);
        LedControl.ledSetOn(1);
        LedControl.ledSetHead(LedConstant.LED_RED_GREEN,9,0,0,0,0,0,0);
        LedControl.close();
    }
}
