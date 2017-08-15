package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.BatteryUpgradeResultEvent;
import com.ubtechinc.alpha.model.upgrade.Alpha2UpdateResultModel;
import com.ubtechinc.alpha.model.upgrade.UpdateConstant;
import com.ubtechinc.alpha.utils.BroadcastUtils;

/**
 * @desc : 电池包升级结果
 * @author: wzt
 * @time : 2017/5/17
 * @modifier:
 * @modify_time:
 */

class ProxyBatteryUpgradeResultImpl extends AbstractProxyService {
    private final Context mContext;

    //处理升级任务

    private Subscriber<BatteryUpgradeResultEvent> batteryUpgradeResultEventSubscriber = new Subscriber<BatteryUpgradeResultEvent>() {
        @Override
        public void onEvent(BatteryUpgradeResultEvent batteryUpgradeEvent) {
            //电池包升级结果
            if(batteryUpgradeEvent != null) {
                boolean isSuccess = batteryUpgradeEvent.success;
                Alpha2UpdateResultModel alpha2UpdateResultModel = new Alpha2UpdateResultModel();
                alpha2UpdateResultModel.setUpdateType(UpdateConstant.MODEL_EMBEDDED_BATTERY);
                if(isSuccess) {
                    alpha2UpdateResultModel.setUpdateResultCode(UpdateConstant.FEEDBACK_STATUS_SUCCESS);
                } else {
                    alpha2UpdateResultModel.setUpdateResultCode(UpdateConstant.FEEDBACK_STATUS_FAIL);
                }
                sendUpdateResult(alpha2UpdateResultModel);
            }
        }
    };

    public ProxyBatteryUpgradeResultImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void unregisterEvent() {
        super.unregisterEvent();
        NotificationCenter.defaultCenter().subscriber(BatteryUpgradeResultEvent.class, batteryUpgradeResultEventSubscriber);
    }

    @Override
    public void registerEvent() {
        super.registerEvent();
        NotificationCenter.defaultCenter().unsubscribe(BatteryUpgradeResultEvent.class, batteryUpgradeResultEventSubscriber);
    }

    private void sendUpdateResult(Alpha2UpdateResultModel model) {
        String text = JsonUtils.object2Json(model);

        BroadcastUtils.sendBroadcast2SystemUpdate(mContext, UpdateConstant.UPDATE_CMD_INFO, text);
    }
}
