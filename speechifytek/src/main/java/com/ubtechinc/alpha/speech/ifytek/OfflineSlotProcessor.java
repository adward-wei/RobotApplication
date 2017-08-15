package com.ubtechinc.alpha.speech.ifytek;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.event.OfflineSlotEvent;
import com.ubtechinc.alpha.model.speech.SlotValue;
import com.ubtechinc.alpha.strategy.IOfflineSlotProcessor;

/**
 * @desc : 科大讯飞离线命令词处理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/15
 * @modifier:
 * @modify_time:
 */

public final class OfflineSlotProcessor implements IOfflineSlotProcessor{

    @Override
    public void processOfflineSlot(String slot) {
        @SlotValue.Type int type = SlotValue.valueToType(slot);
        OfflineSlotEvent event = new OfflineSlotEvent();
        event.slot = type;
        NotificationCenter.defaultCenter().publish(event);
    }
}
