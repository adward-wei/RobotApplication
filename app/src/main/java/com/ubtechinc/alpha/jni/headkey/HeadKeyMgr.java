package com.ubtechinc.alpha.jni.headkey;

import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.event.KeyPressEvent;
import com.ubtechinc.alpha.jni.headkey.lynx.HeadKeyState;
import com.ubtechinc.alpha.jni.headkey.lynx.HeadkeyManager;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc : 5mic头部按键
 * @author: logic.peng
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class HeadKeyMgr {
    private static final String TAG = "nativeClass";

    /**
     * 5麦机器前按键短按
     **/
    public static final int MIC5_KEY_FORMOR_SHORT = 0x3c;
    /**
     * 5麦机器前按键长按
     **/
    public static final int MIC5_KEY_FORMOR_LONG = 0x40;
    /**
     * 5麦机器后按键短按
     **/
    public static final int MIC5_KEY_BACK_SHORT = 0x3d;
    /**
     * 5麦机器后按键短按
     **/
    public static final int MIC5_KEY_BACK_LONG = 0x41;
    /**
     * 5麦机器拍头
     **/
    public static final int MIC5_KEY_INTERRUPT = 0x42;// 66
    //////////////////////////////////////////////////////////////////////
    /** lynx前面按键按下 **/
    public static final int MIC5_KEY_FORMOR_PRESS = 0x5a; //90
    /** lynx前面按键抬起 **/
    public static final int MIC5_KEY_FORMOR_LIFT = 0x5b;  //91
    /** lynx后面按键按下 **/
    public static final int MIC5_KEY_BACK_PRESS = 0x5c;  //92
    /** lynx后面按键抬起 **/
    public static final int MIC5_KEY_BACK_LIFT = 0x5d;  //93
    /** lynx前后按键抬起 **/
    public static final int MIC5_KEY_TWOBUTTON_PRESS = 0x5e; //94
    /** lynx前后按键抬起 **/
    public static final int MIC5_KEY_TWOBUTTON_LIFT = 0x5f; //95

    private int callbackCount = 0;

    public native boolean Init();

    public native int Add(int x, int y);

    public native void nativeInit();

    public native void nativeThreadStart();

    public native void nativeThreadStop();

    public HeadKeyMgr() {
    }

    public void onNativeCallback(int var) {
        callbackCount = callbackCount + 1;
        Log.d("native", "callbackCount=" + callbackCount);
        Log.d("native", "onNativeCallback var=" + var);

        if(!SysUtils.isLynxSystem()){
            byte[] param = convertHeadKeyEvent(var);
            publishEvent(param[0]);
        }else{
            newConvertHeadKeyEvent(var);
        }
    }

    private void publishEvent(byte param) {
        KeyPressEvent event = new KeyPressEvent();
        event.keyType = param;
        NotificationCenter.defaultCenter().publish(event);
    }

    private byte[] convertHeadKeyEvent(int event) {
        LogUtils.i( "key event=" + event);
        byte[] param = new byte[1];
        int down_up = (event & 0xff);
        if (down_up == 0) {
            param[0] = 0;
        } else {
            int newEvent = (event & 0xff00) >> 8;
            if (MIC5_KEY_FORMOR_SHORT == newEvent) {
                param[0] = 5;
            } else if (MIC5_KEY_BACK_SHORT == newEvent) {
                param[0] = 4;
            } else if (MIC5_KEY_INTERRUPT == newEvent) {
                param[0] = 6;
            } else if (MIC5_KEY_FORMOR_LONG == newEvent) {
                param[0] = 7;
            }
        }
        return param;
    }

    private void newConvertHeadKeyEvent(int event){
        byte[] param = convertHeadKeyEvent(event);//兼容
        LogUtils.d(TAG, "param[0]: "+param[0]+"in convertHeadKeyEvent.");
        if (param[0] == 0){
            int newEvent = (event & 0xff00) >> 8;
            if (MIC5_KEY_FORMOR_PRESS == newEvent){
                LogUtils.d(TAG, "formor button press");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.FORM_KEY_DOWN);
            }else if (MIC5_KEY_FORMOR_LIFT == newEvent){
                LogUtils.d(TAG, "formor button lift");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.FORM_KEY_UP);
            }else if (MIC5_KEY_BACK_PRESS == newEvent){
                LogUtils.d(TAG, "back button press");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.BACK_KEY_DOWN);
            }else if (MIC5_KEY_BACK_LIFT == newEvent){
                LogUtils.d(TAG, "back button lift");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.BACK_KEY_UP);
            }else if (MIC5_KEY_TWOBUTTON_PRESS == newEvent){
                LogUtils.d(TAG, "two button press");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.BACK_FORM_KEY_DOWN);
            }else if(MIC5_KEY_TWOBUTTON_LIFT == newEvent){
                LogUtils.d(TAG, "two button lift");
                HeadkeyManager.getHeadkeyManager().notifyHeadKeyState(HeadKeyState.BACK_FORM_KEY_UP);
            }
        }
    }


    static {
        if (SysUtils.is5Mic())
            System.loadLibrary("head_key_mgr");
    }
}
