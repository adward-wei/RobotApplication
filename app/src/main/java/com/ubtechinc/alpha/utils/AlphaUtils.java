package com.ubtechinc.alpha.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.UserHandle;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;

/**
 * @desc : alpha util
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public final class AlphaUtils {

    public static void interruptAlphaNoIntent(Context mContext) {
        SpeechServiceProxy.getInstance().speechStopTTS();
        RobotOpsManager.get(mContext).stopAction(null);
    }

    public static void sendInterruptIntent(Context mContext){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mContext.sendBroadcastAsUser(new Intent(SdkConstants.ACTION_ALPHA_INTERRUPT_BUSINESS), UserHandle.getUserHandleForUid(-1));
        }else {
            mContext.sendBroadcast(new Intent(SdkConstants.ACTION_ALPHA_INTERRUPT_BUSINESS));
        }
    }

    public static void interruptAlpha(Context mContext) {
        SpeechServiceProxy.getInstance().speechStopTTS();
        RobotOpsManager.get(mContext).stopAction(null);
        sendInterruptIntent(mContext);
    }
}
