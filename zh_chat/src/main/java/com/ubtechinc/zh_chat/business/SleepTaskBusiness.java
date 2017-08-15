package com.ubtechinc.zh_chat.business;

import android.content.Context;
import android.content.Intent;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.robot.UBTSemanticRobot;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;
import com.ubtechinc.zh_chat.utils.Utils;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 定时休眠任务，总共超过5分钟（3分钟超时提醒+2分钟超时休眠）没有应答则进入休眠
 * </pre>
 */
public class SleepTaskBusiness extends BaseBusiness {
    private static final String TAG = "SleepTaskBusiness";
    private ScheduledExecutorService timer;
    private static final int INTERVAL_TIME = 120;

    public SleepTaskBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        if (timer == null) {
            timer = Executors.newScheduledThreadPool(1);
            timer.schedule(new Runnable() {
                @Override
                public void run() {
                    //发送省电广播
                    String [] sleepText = mContext.getResources().getStringArray(R.array.sleep_tts_text);
                    String tts = sleepText[new Random(System.currentTimeMillis()).nextInt(sleepText.length)];
                    tts =  String.format(tts, Utils.getMasterName(mContext));
                    handle.start_TTS(new UBTSemanticRobot.ISpeechTTSFinishedListener() {
                        @Override
                        public void onTTSComplete() {
                            timer = null;
                            handle.enterSavePowerMode();
                        }
                    }, tts, false);
                }
            }, INTERVAL_TIME, TimeUnit.SECONDS);
        }
    }


    private void sendSavePowerBroadcast(){
        Intent intent = new Intent();
        intent.setAction("com.ubtechinc.services.POWER_SAVE");
        intent.putExtra("should_save_power", true);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {
        if (timer != null) {
            timer.shutdownNow();
        }
        timer = null;
    }
}
