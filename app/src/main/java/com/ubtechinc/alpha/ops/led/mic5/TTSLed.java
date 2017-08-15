package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.jni.LedControl;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class TTSLed extends BaseLed{

    public TTSLed(int priority,LedType ledType){
        super(priority,ledType);
    }
    @Override
    void operationLed() {
        //头灯呼吸灯 ，眼睛灯灭
        LedControl.open();
        LedControl.ledSetHead(3,9,255,255,10,0,10,1);
        LedControl.ledSetEye(1,9,0,0,0,0,0,0);
        LedControl.close();
    }
}
