package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.ShutdownSettingEvent;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.ShutdownSettingOp;

/**
 * @desc : 关机模式设置
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class ProxyShutdownSettingImpl extends AbstractProxyService {
    private final Context mContext;

    public ProxyShutdownSettingImpl(Context context) {
        mContext = context;
    }

    private Subscriber<ShutdownSettingEvent> shutdownSettingEventSubscriber = new Subscriber<ShutdownSettingEvent>() {
        @Override
        public void onEvent(ShutdownSettingEvent event) {
            ShutdownSettingOp shutdownSettingOp = new ShutdownSettingOp(event.isForbidShutdown ? (byte)0x01: (byte)0x00);
            RobotOpsManager.get(mContext).executeOp(shutdownSettingOp);
        }
    };

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(ShutdownSettingEvent.class, shutdownSettingEventSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(ShutdownSettingEvent.class, shutdownSettingEventSubscriber);
    }
}
