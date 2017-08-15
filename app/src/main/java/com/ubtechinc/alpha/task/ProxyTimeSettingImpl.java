package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.TimeSettingEvent;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.AdjustRTCTimeOp;

/**
 * @desc : RTC时间设置
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class ProxyTimeSettingImpl extends AbstractProxyService {
    private final Context mContext;

    public ProxyTimeSettingImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    private Subscriber<TimeSettingEvent> timesettingEventSubscriber = new Subscriber<TimeSettingEvent>() {
        @Override
        public void onEvent(TimeSettingEvent event) {
            AdjustRTCTimeOp adjustSysTimeOp = new AdjustRTCTimeOp(event);
            RobotOpsManager.get(mContext).executeOp(adjustSysTimeOp);
        }
    };

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(TimeSettingEvent.class, timesettingEventSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(TimeSettingEvent.class, timesettingEventSubscriber);
    }
}
