/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.task;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.event.ChestUpgradeEvent;
import com.ubtechinc.alpha.event.HeadUpgradeEvent;
import com.ubtechinc.alpha.event.MotorUpgradeEvent;

/**
 * @desc : 固件升级
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/26
 * @modifier:
 * @modify_time:
 */

public class ProxyUpgradeTaskImpl extends AbstractProxyService {
    private final Context mContext;

    //处理升级任务

    private Subscriber<MotorUpgradeEvent> motorUpgradeEventSubscriber = new Subscriber<MotorUpgradeEvent>() {
        @Override
        public void onEvent(MotorUpgradeEvent motorUpgradeEvent) {
            //舵机升级
        }
    };

    private Subscriber<ChestUpgradeEvent> chestUpgradeEventSubscriber = new Subscriber<ChestUpgradeEvent>() {
        @Override
        public void onEvent(ChestUpgradeEvent chestUpgradeEvent) {
            //胸板升级
        }
    };

    private Subscriber<HeadUpgradeEvent> headUpgradeEventSubscriber = new Subscriber<HeadUpgradeEvent>() {
        @Override
        public void onEvent(HeadUpgradeEvent headUpgradeEvent) {
            //头板升级
        }
    };

    public ProxyUpgradeTaskImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void unregisterEvent() {
        super.unregisterEvent();
        NotificationCenter.defaultCenter().subscriber(MotorUpgradeEvent.class, motorUpgradeEventSubscriber);
        NotificationCenter.defaultCenter().subscriber(ChestUpgradeEvent.class, chestUpgradeEventSubscriber);
        NotificationCenter.defaultCenter().subscriber(HeadUpgradeEvent.class, headUpgradeEventSubscriber);
    }

    @Override
    public void registerEvent() {
        super.registerEvent();
        NotificationCenter.defaultCenter().unsubscribe(MotorUpgradeEvent.class, motorUpgradeEventSubscriber);
        NotificationCenter.defaultCenter().unsubscribe(ChestUpgradeEvent.class, chestUpgradeEventSubscriber);
        NotificationCenter.defaultCenter().unsubscribe(HeadUpgradeEvent.class, headUpgradeEventSubscriber);
    }


}
