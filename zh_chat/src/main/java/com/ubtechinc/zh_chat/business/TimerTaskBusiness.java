package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.NetworkUtils;
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
 *   desc  : 计时任务，超过三分钟提醒操作机器人 Alpha state == business 时候清空计时器 任务结束开启计时 tts 5后取消
 * </pre>
 */
public class TimerTaskBusiness extends BaseBusiness {

    private ScheduledExecutorService timer;
    private SleepTaskBusiness timerSleepTask;
    private String TAG = "TimerTaskBusiness";
    private String[] dances;
    private static final int INTERVAL_TIME = 180;

    public TimerTaskBusiness(Context cxt) {
        super(cxt);
        dances = mContext.getResources().getStringArray(R.array.idle_tts_text);
    }

    public TimerTaskBusiness(Context mContext, SleepTaskBusiness timerSleepTask) {
        super(mContext);
        dances = mContext.getResources().getStringArray(R.array.idle_tts_text);
        this.timerSleepTask = timerSleepTask;
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        if (!NetworkUtils.isConnected())
            return;
        if (timer == null) {
            timer = Executors.newScheduledThreadPool(1);
            timer.schedule(new Runnable() {
                @Override
                public void run() {
                    String tts = String.format(dances[new Random(System.currentTimeMillis()).nextInt(dances.length)], Utils.getMasterName(mContext));
                    handle.start_TTS(tts, false);
                    timerSleepTask.start(handle);
                    timer = null;
                }
            }, INTERVAL_TIME, TimeUnit.SECONDS);
        }
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {
        if (timer != null) {
            timer.shutdownNow();
            timer = null;
        }
        timerSleepTask.clean(handle);
    }
}
