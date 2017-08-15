package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtechinc.alpha.jni.LedControl;

import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_FORM_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_FORM_KEY_LED_ON;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_KEY_LED_ON;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_FORM_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_FORM_KEY_LED_ON;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class HeadKeyLed extends BaseLed {
    private static final int LEDCONTROL_HEAD_FORM_KEY_ON=0x0e;
    private static final int LEDCONTROL_HEAD_FORM_KEY_OFF=0x0f;
    private static final int LEDCONTROL_HEAD_BACK_KEY_ON=0x10;
    private static final int LEDCONTROL_HEAD_BACK_KEY_OFF=0x11;
    private int ledOnOff;
    public HeadKeyLed(int priority, LedType ledType, int ledOnOff){
        super(priority,ledType);
        this.ledOnOff=ledOnOff;
    }
    @Override
    void operationLed() {
        if(HEAD_FORM_KEY_LED_ON == ledOnOff){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_FORM_KEY_ON);
            LedControl.close();
        }else if(ledOnOff == HEAD_FORM_KEY_LED_OFF){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_FORM_KEY_OFF);
            LedControl.close();
        }else if(ledOnOff == HEAD_BACK_KEY_LED_ON){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_BACK_KEY_ON);
            LedControl.close();
        }else if(ledOnOff == HEAD_BACK_KEY_LED_OFF){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_BACK_KEY_OFF);
            LedControl.close();
        }else if(ledOnOff == HEAD_BACK_FORM_KEY_LED_ON){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_FORM_KEY_ON);
            LedControl.ledSetOn(LEDCONTROL_HEAD_BACK_KEY_ON);
            LedControl.close();
        }else if(ledOnOff == HEAD_BACK_FORM_KEY_LED_OFF){
            LedControl.open();
            LedControl.ledSetOn(LEDCONTROL_HEAD_FORM_KEY_OFF);
            LedControl.ledSetOn(LEDCONTROL_HEAD_BACK_KEY_OFF);
            LedControl.close();
        }
    }
}
