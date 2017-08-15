package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.SetWifiStatusEvent;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.net.SetWifiStatsOp;

/**
 * @desc : wifi状态设置
 * @author: wzt
 * @time : 2017/5/25
 * @modifier:
 * @modify_time:
 */

public class ProxyWifiStatusImpl extends AbstractProxyService {
    private final Context mContext;

    public ProxyWifiStatusImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    private Subscriber<SetWifiStatusEvent> setWifiStatusEventSubscriber = new Subscriber<SetWifiStatusEvent>() {
        @Override
        public void onEvent(SetWifiStatusEvent event) {
            SetWifiStatsOp setWifiStatsOp = new SetWifiStatsOp(event.param);
            RobotOpsManager.get(mContext).executeOp(setWifiStatsOp);
        }
    };

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(SetWifiStatusEvent.class, setWifiStatusEventSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(SetWifiStatusEvent.class, setWifiStatusEventSubscriber);
    }
}
