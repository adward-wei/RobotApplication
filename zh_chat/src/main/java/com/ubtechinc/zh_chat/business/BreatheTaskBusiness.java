package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *   author: Logic
 *   email : 2091843903@qq.com
 *   time  : 2017/3/24
 *   desc  : 计时任务 Alpha state == business 时候清空计时器
 *           任务结束开启计时 tts 5后取消
 * </pre>
 */

public class BreatheTaskBusiness extends BaseBusiness {

    private static final int INTERVAL_TIME = 30;
    private static final String TAG = "BreatheTaskBusiness";
    private ScheduledExecutorService timer;

    public BreatheTaskBusiness(Context cxt) {
        super(cxt);
    }

    @Override
    public void start(final UBTSemanticRootProxy handle) {
        if (timer == null) {
            timer = Executors.newScheduledThreadPool(1);
            final WeakReference<UBTSemanticRootProxy> weakRoot = new WeakReference<UBTSemanticRootProxy>(handle);
            timer.schedule(new Runnable() {
                @Override
                public void run() {
                    UBTSemanticRootProxy robot = weakRoot.get();
                    if (robot != null && !robot.isActioning() && !robot.isTTSing()){
                        robot.start_Action(mContext.getResources().getString(R.string.breathe_action_name));
                    }
                    timer.schedule(this, INTERVAL_TIME, TimeUnit.SECONDS);
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
    }
}
