package com.ubtechinc.alpha.ops.led.mic5;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class WakeupLed extends BaseLed{

    public WakeupLed(int priority,LedType ledType){
        super(priority,ledType);
    }
    @Override
    void operationLed() {
        //头灯和眼睛灯常亮
    }
}
