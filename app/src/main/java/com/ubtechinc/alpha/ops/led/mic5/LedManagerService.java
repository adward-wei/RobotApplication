package com.ubtechinc.alpha.ops.led.mic5;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.wificonnect.Alpha2Connection;
import com.ubtechinc.alpha.wificonnect.AlphaWifiState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LedManagerService {
    private static final String TAG ="LedManagerService";

    private BaseLed currentLed,previousLed;
    private volatile boolean  isTTSCompleted =false ;

    private Timer mTimer =new Timer("led_manager");
    private static volatile LedManagerService ledManagerService;
    private Alpha2Connection alpha2Connection = Alpha2Connection.getInstance(AlphaApplication.getContext());
    private LedManagerService(){}
    public static LedManagerService getLedManager(){

        if(ledManagerService == null){
            synchronized (LedManagerService.class){
                if(ledManagerService ==null){
                    ledManagerService =new LedManagerService();
                }
            }
        }
        return ledManagerService;
    }

    public synchronized void operationLed(BaseLed baseLed){
        if(baseLed !=null){
            LogUtils.i(TAG,"baseLed type:"+baseLed.ledType+"baseLed priority:"+baseLed.priority+" in operationLed");
            if(baseLed.ledType == LedType.VOLUME_DOWN || baseLed.ledType == LedType.VOLUME_UP){
                baseLed.operationLed();
                startVolumeTimer();
            }else if(currentLed != null  ){
                LogUtils.i(TAG,"currentLed type:"+currentLed.ledType +"currentLed type priority:"+currentLed.priority);
                if(baseLed.priority > currentLed.priority || baseLed.priority == currentLed.priority && alpha2Connection.getWifiState()!= AlphaWifiState.DISCONNECTED){
                    baseLed.operationLed();
                    previousLed =currentLed;
                    currentLed =baseLed;
                }
            }else if(currentLed == null ){
                LogUtils.i(TAG,"currentLed == null...");
                if(alpha2Connection.getWifiState()!= AlphaWifiState.DISCONNECTED){
                    baseLed.operationLed();
                    currentLed =baseLed;
                }
            }
        }else{
            LogUtils.i(TAG,"baseLed is null!!!");
        }
    }

    public void operationLedRightNow(BaseLed baseLed){
        if(baseLed != null){
            baseLed.operationLed();
        }
    }

    //音量灯效之后需要计时开
    public void startVolumeTimer(){
        LogUtils.i(TAG,"startVolumeTimer");
        mTimer.cancel();
        mTimer = new Timer("led_manager");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                recoveryPreviousLed();
            }
        },LedConstant.VOLUME_LED_OPERATION_TIME );
    }

    public void recoveryPreviousLed(){
        if(alpha2Connection.getWifiState()== AlphaWifiState.DISCONNECTED){
            operationLedRightNow(new NetDisconnectionLed(LedPriority.PRIORITY_MAX, LedType.NETWORK_DISCONNECTION));
        }else if(previousLed !=null){
            LogUtils.i(TAG," previousled != null"+" previousledType:"+previousLed.ledType+"previousLed priority:"+previousLed.priority);
             if( previousLed.ledType == LedType.TTSING && !isTTSCompleted){
                 operationLed(previousLed);
            }
        }else{
            LogUtils.i(TAG," previousled == null in recoveryPreviousLed.");
            if(currentLed!=null){
                LogUtils.i(TAG,"recoveryPreviousLed currentLed !=null"+" currentLedType:"+currentLed.ledType);
                operationLed(currentLed);
            }else{
                LogUtils.i(TAG," currentled == null !!!!! &&  previousLed == null");
            }
        }

    }

    public synchronized void notifyRobotStatus(RobotStatus status){
        switch(status){
            case START_TTS:
                isTTSCompleted=false;
                break;
            case TTS_COMPLETED:
                isTTSCompleted=true;
                stopLedTTS();
                break;

        }
    }

    private void stopLedTTS(){
        operationLed(new TTSCompleteLed(LedPriority.PRIORITY_NORMAL, LedType.TTS_COMPLETE));
    }










}
