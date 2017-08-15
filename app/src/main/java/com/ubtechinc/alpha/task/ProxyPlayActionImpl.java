package com.ubtechinc.alpha.task;

import android.content.Context;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.affairdispatch.AffairConstruct;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.event.PlayActionEvent;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.robotinfo.RobotState;

/**
 * @desc : 执行动作
 * @author: wzt
 * @time : 2017/5/22
 * @modifier:
 * @modify_time:
 */

public class ProxyPlayActionImpl extends AbstractProxyService {
    private Context mContext;

    public ProxyPlayActionImpl(Context context) {
        mContext = context;
    }

    private Subscriber<PlayActionEvent> playActionEventSubscriber = new Subscriber<PlayActionEvent>() {
        @Override
        public void onEvent(PlayActionEvent event) {
            if(event != null && !TextUtils.isEmpty(event.fileName)) {
                if(RobotState.get().isInPowerSave()) {
                    RobotTakeARest.instance().start(false);
                }

                ActionServiceProxy.getInstance().playAction(event.fileName,null);
            }
        }
    };

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(PlayActionEvent.class, playActionEventSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(PlayActionEvent.class, playActionEventSubscriber);
    }
}
