package com.ubtechinc.alpha.behavior;

import android.content.Context;
import android.content.Intent;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.SetChargePlayingModeOp;
import com.ubtechinc.alpha.ops.sys.SetSavePowerModeOp;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * @desc : 机器人蹲下休息
 * @author: wzt
 * @time : 2017/5/26
 * @modifier:
 * @modify_time:
 */

public class RobotTakeARest {

    private Context mContext;
    private static RobotTakeARest instance;
    public static RobotTakeARest instance(){
        if (instance == null){
            synchronized (RobotTakeARest.class){
                if (instance == null) instance = new RobotTakeARest(AlphaApplication.getContext());
            }
        }
        return instance;
    }

    private RobotTakeARest(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized void start(boolean savePower) {
        if(savePower) {
            RobotOpsManager.get(mContext).stopAction(null);
            SpeechRobotApi.get().speechStopTTS();
            //以同步方式执行
            RobotOpsManager.get(mContext).executeOpSync(new SetSavePowerModeOp(true, true));
            RobotState.get().setInPowerSave(true);
            Intent intent = new Intent(SdkConstants.ACTION_CHANGE_SAVEPOWER_MODE);
            intent.putExtra(SdkConstants.ACTION_CHANGE_SAVEPOWER_MODE, true);
            mContext.sendBroadcast(intent);
        } else {
            if (RobotState.get().isChargePlayingOpen()) {
                RobotOpsManager.get(mContext).executeOpSync(new SetChargePlayingModeOp(true));
            }
            RobotOpsManager.get(mContext).executeOpSync(new SetSavePowerModeOp(false, false));
            RobotState.get().setInPowerSave(false);
            Intent intent = new Intent(SdkConstants.ACTION_CHANGE_SAVEPOWER_MODE);
            intent.putExtra(SdkConstants.ACTION_CHANGE_SAVEPOWER_MODE, false);
            mContext.sendBroadcast(intent);
        }
    }
}
