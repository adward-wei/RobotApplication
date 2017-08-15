package com.ubtechinc.alpha.behavior;

import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.ISerialCmdOp;
import com.ubtechinc.alpha.ops.OpResult;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.SerialOpResultListener;
import com.ubtechinc.alpha.ops.led.SetEyeBlinkOp2;
import com.ubtechinc.alpha.ops.led.SetEyeFlashOp;
import com.ubtechinc.alpha.ops.led.SetEyeMarqueeOp;
import com.ubtechinc.alpha.ops.led.SetHeadBreathOp;
import com.ubtechinc.alpha.ops.led.SetHeadFlashOp;
import com.ubtechinc.alpha.ops.led.SetHeadMarqueeOp;
import com.ubtechinc.alpha.sdk.led.Led;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedEffect;
import com.ubtechinc.alpha.serial.SerialConstants;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public class LedBehavior extends Behavior implements SerialOpResultListener {
    private Led led = Led.EYE;
    private LedEffect effect = LedEffect.FLASH;
    private int onTime = 2000;
    private int runTime = 4000;
    private LedColor color = LedColor.WHITE;
    private LedColor secondColor = LedColor.NULL;
    private LedBright bright = LedBright.NINE;
    private int number = 0XFF;//灯编号

    void setNumber(int number) {
        this.number = number;
    }

    void setLed(Led led) {
        this.led = led;
    }

    void setEffect(LedEffect effect) {
        this.effect = effect;
    }

    void setOnTime(int onTime) {
        this.onTime = onTime;
    }

    void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    void setColor(LedColor color) {
        this.color = color;
    }

    void setSecondColor(LedColor secondColor) {
        this.secondColor = secondColor;
    }

    void setBright(LedBright bright) {
        this.bright = bright;
    }

    @Override
    protected boolean onStart() {
        ISerialCmdOp op = null;
        if (led == Led.EYE){
            switch (effect){
                case BLINK:
                    op = new SetEyeBlinkOp2(color, bright, number, onTime, onTime, runTime);
                    break;
                case FLASH:
                    op = new SetEyeFlashOp(color, bright, number, onTime, runTime);
                    break;
                case MARQUEE:
                    if (secondColor != LedColor.NULL && secondColor != color){
                        op = new SetEyeMarqueeOp(color, secondColor, bright, number, onTime ,runTime);
                    }else {
                        op = new SetEyeMarqueeOp(color, bright, number, onTime, runTime);
                    }
                    break;
            }
        }else if (led == Led.HEAD) {
            switch (effect) {
                case BREATH:
                    if (secondColor != LedColor.NULL && secondColor != color){
                        op = new SetHeadBreathOp(color, secondColor, bright, number, onTime, runTime);
                    }else {
                        op = new SetHeadBreathOp(color, bright, number, onTime, runTime);
                    }
                    break;
                case FLASH:
                    op = new SetHeadFlashOp(color, bright, number, onTime, runTime);
                    break;
                case MARQUEE:
                    if (secondColor != LedColor.NULL && secondColor != color){
                        op = new SetHeadMarqueeOp(color, secondColor, bright, number, onTime, runTime);
                    }else {
                        op = new SetHeadMarqueeOp(color, bright, number, onTime, runTime);
                    }
                    break;
            }
        }
        if (op != null)
            RobotOpsManager.get(AlphaApplication.getContext()).executeOp(op, this);
        Timber.d(TAG, "LedBehavior start: op= %s", op!=null);
        return op != null;
    }

    @Override
    public void onRecvOpResult(final OpResult result) {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                resultCallback.onBehaviorResult(result.errorCode == SerialConstants.ERR_OK);
            }
        }, runTime);
    }
}
