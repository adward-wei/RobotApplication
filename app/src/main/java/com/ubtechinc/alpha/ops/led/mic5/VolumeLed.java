package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.utils.SoundVolumesUtils;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class VolumeLed extends BaseLed{


    private int operationTime;
    public VolumeLed(int priority, LedType ledType, int operationTime){
        super(priority,ledType);
        this.operationTime=operationTime;
    }

    @Override
    void operationLed() {
        int volumeLevel= SoundVolumesUtils.get(AlphaApplication.getContext()).getVolumeLevel();
        LedControl.open();
        LedControl.ledSetHead(LedConstant.LED_GREEN,9,(int)(LedConstant.rightHeadLights[volumeLevel]),(int)(LedConstant.leftHeadLights[volumeLevel]),this.operationTime,0,this.operationTime,0);
        LedControl.close();
    }
}
