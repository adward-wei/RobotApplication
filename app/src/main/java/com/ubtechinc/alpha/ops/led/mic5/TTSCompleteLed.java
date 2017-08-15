package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.jni.LedControl;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class TTSCompleteLed extends BaseLed {

    public TTSCompleteLed(int priority, LedType ledType){
        super(priority,ledType);
    }
    @Override
    void operationLed() {
        //头灯灯灭 ，眨眼睛
        LedControl.open();
        LedControl.ledSetHead(1,9,0,0,0,0,0,0);
        LedControl.ledSetOn(1);
        LedControl.close();
    }
}
