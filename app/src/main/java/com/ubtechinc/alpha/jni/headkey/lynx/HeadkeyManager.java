package com.ubtechinc.alpha.jni.headkey.lynx;

import android.content.Intent;

import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.ops.led.mic5.HeadKeyLed;
import com.ubtechinc.alpha.ops.led.mic5.LedConstant;
import com.ubtechinc.alpha.ops.led.mic5.LedManagerService;
import com.ubtechinc.alpha.ops.led.mic5.LedPriority;
import com.ubtechinc.alpha.ops.led.mic5.LedType;
import com.ubtechinc.alpha.ops.led.mic5.VolumeLed;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.utils.AlphaUtils;
import com.ubtechinc.alpha.utils.SoundVolumesUtils;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha2services.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_FORM_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_FORM_KEY_LED_ON;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_BACK_KEY_LED_ON;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_FORM_KEY_LED_OFF;
import static com.ubtechinc.alpha.ops.led.mic5.LedConstant.HEAD_FORM_KEY_LED_ON;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class HeadkeyManager {

    private static final int HEAD_KEY_LONG_CLICK_TIME_PERIOD=500;

    private boolean backKeyDown =false,backKeyUp =false;
    private boolean formKeyDown =false,formKeyUp =false;
    private boolean backFormKeyClick=false;
    private AtomicBoolean completed = new AtomicBoolean(true);
    private Timer backKeyTimer =new Timer("head_back_key");
    private Timer formKeyTimer=new Timer("head_form_key");

    private static volatile HeadkeyManager headkeyManager;
    private HeadkeyManager(){}

    public static HeadkeyManager getHeadkeyManager(){
        if(headkeyManager == null){
            synchronized (HeadkeyManager.class){
                if(headkeyManager == null){
                    headkeyManager =new HeadkeyManager();
                }
            }
        }
        return headkeyManager;
    }

    public void notifyHeadKeyState(HeadKeyState headKeyState){
        switch(headKeyState){
            case BACK_KEY_DOWN:
                backKeyDown=true;
                backKeyUp=false;
                backKeyOnDown();
                break;
            case BACK_KEY_UP:
                backKeyUp=true;
                backKeyDown=false;
                backKeyOnUp();
                break;
            case FORM_KEY_DOWN:
                formKeyDown=true;
                formKeyUp=false;
                formKeyOnDown();
                break;
            case FORM_KEY_UP:
                formKeyUp=true;
                formKeyDown=false;
                formKeyOnUp();
                break;
            case BACK_FORM_KEY_DOWN:
                backFormKeyClick=true;
                backFormKeyOnDown();
                break;
            case BACK_FORM_KEY_UP:
                backFormKeyClick=false;
                backFormKeyOnUp();
                break;
        }
    }

    private void backFormKeyOnDown(){
        //打开led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_BACK_FORM_KEY_LED_ON));
        //执行拍头打断逻辑 ,打断逻辑，事务，进入省电模式， 退出二维码扫描
        if (completed.getAndSet(false)) {
            if (RobotState.get().isInPowerSave()) {
                RobotTakeARest.instance().start(false);
            }
            AlphaUtils.interruptAlphaNoIntent(AlphaApplication.getContext());
            ActionServiceProxy.getInstance().playAction(StringUtil.getString(R.string.action_wakeup_0), AffairPriority.PRIORITY_HIGH, new ActionPlayListener() {

                @Override
                public void onActionResult(int nErr) {
                    //最后通知第三方应用
                    AlphaUtils.sendInterruptIntent(AlphaApplication.getContext());
                    AlphaApplication.getContext().sendBroadcast(new Intent(StaticValue.ALPHA_QR_CODE_CANCLE));
                    completed.set(true);
                }
            });

        }
    }

    private void backFormKeyOnUp(){
        //关闭led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_BACK_FORM_KEY_LED_OFF));
    }

    private void backKeyOnDown(){
        //打开led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_BACK_KEY_LED_ON));

        backKeyTimer.cancel();
        backKeyTimer=new Timer("head_key_manager");
        backKeyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(backKeyDown && !backKeyUp){
                    SoundVolumesUtils.get(AlphaApplication.getContext()).addVolume(1);
                    LedManagerService.getLedManager().operationLed(new VolumeLed(LedPriority.PRIORITY_NORMAL, LedType.VOLUME_UP, LedConstant.VOLUME_LED_OPERATION_TIME));
                }
            }
        },0,HEAD_KEY_LONG_CLICK_TIME_PERIOD);
    }

    private void backKeyOnUp(){
        //关闭led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_BACK_KEY_LED_OFF));
        backKeyTimer.cancel();
    }

    private void formKeyOnDown(){
        //打开led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_FORM_KEY_LED_ON));

        formKeyTimer.cancel();
        formKeyTimer=new Timer("head_key_manager");
        formKeyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(formKeyDown && !formKeyUp){
                    SoundVolumesUtils.get(AlphaApplication.getContext()).mulVolume(1);
                    LedManagerService.getLedManager().operationLed(new VolumeLed(LedPriority.PRIORITY_NORMAL, LedType.VOLUME_DOWN, LedConstant.VOLUME_LED_OPERATION_TIME));
                }
            }
        },0,HEAD_KEY_LONG_CLICK_TIME_PERIOD);
    }

    private void formKeyOnUp(){
        //关闭led灯
        LedManagerService.getLedManager().operationLedRightNow(new HeadKeyLed(LedPriority.PRIORITY_NORMAL, LedType.HEAD_KEY, HEAD_FORM_KEY_LED_OFF));
        formKeyTimer.cancel();
    }


}
