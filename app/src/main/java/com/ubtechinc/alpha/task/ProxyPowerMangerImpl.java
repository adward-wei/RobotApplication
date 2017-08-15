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
import com.ubt.alpha2.download.util.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.event.SetChargePlayEvent;
import com.ubtechinc.alpha.ops.ActionServiceProxy;
import com.ubtechinc.alpha.ops.sys.SetChargePlayingModeOp;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha2services.R;
import com.ubtechinc.alpha.affairdispatch.AffairConstruct;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.event.BatteryHighTemperatureEvent;
import com.ubtechinc.alpha.event.DCStateEvent;
import com.ubtechinc.alpha.event.ElectricityEvent;
import com.ubtechinc.alpha.event.ForbiddenChargePlayingEvent;
import com.ubtechinc.alpha.event.SavePowerEvent;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.sys.DCStateAnswerOp;
import com.ubtechinc.alpha.robotinfo.RobotState;

import java.util.Random;

/**
 * @desc : 电量电池管理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/25
 * @modifier:
 * @modify_time:
 */

public class ProxyPowerMangerImpl extends AbstractProxyService {
    private final Context mContext;

    //电量信息
    private Subscriber<ElectricityEvent> electricitySubscriber = new Subscriber<ElectricityEvent>() {
        @Override
        public void onEvent(ElectricityEvent electricityEvent) {
            RobotState.get().setInsertDC(electricityEvent.isCharging);
            if (!RobotState.get().isInsertDC() && electricityEvent.isLowBattery && !RobotState.get().isHasLowBatteryTTS()){
                //低电量先停止动作，不关心回调
                RobotOpsManager.get(AlphaApplication.getContext()).stopAction(null);
                RobotState.get().setHasLowBatteryTTS(true);
                String[] tts = StringUtil.getStringArray(R.array.offline_power_lower);
                String tt = tts[new Random(System.currentTimeMillis()).nextInt(tts.length)];
                SpeechServiceProxy.getInstance().speechStartTTS(String.format(tt, electricityEvent.mPercentage),null);
            }

            RobotState.get().setPowerValue(electricityEvent.mPercentage);
        }
    };

    //电池温度
    private Subscriber<BatteryHighTemperatureEvent> batteryTempSubscirber = new Subscriber<BatteryHighTemperatureEvent>() {
        @Override
        public void onEvent(BatteryHighTemperatureEvent event) {
            SpeechServiceProxy.getInstance().speechStartTTS(StringUtil.getString(R.string.tempreture_too_high),null);
        }
    };

    //dc状态
    private Subscriber<DCStateEvent> dcStateSubscriber = new Subscriber<DCStateEvent>() {
        @Override
        public void onEvent(DCStateEvent event) {
            System.out.println("event.isInserted: "+event.isInserted);
            RobotState.get().setInsertDC(event.isInserted);
            if (event.isInserted && RobotState.get().isChargePlayingOpen()){
                ActionServiceProxy.getInstance().playAction(StringUtil.getString(R.string.action_stand_up),AffairPriority.PRIORITY_HIGH,null);
            } else {
                if(RobotState.get().isChargePlayingOpen()) {
                    RobotOpsManager.get(AlphaApplication.getContext()).stopAction(null);
                }
                DCStateAnswerOp op = new DCStateAnswerOp();
                RobotOpsManager.get(mContext).executeOp(op);
            }
        }
    };

    //边充边玩上报事件接收器
    private Subscriber<ForbiddenChargePlayingEvent> chargePlayingEventSubscriber = new Subscriber<ForbiddenChargePlayingEvent>() {
        @Override
        public void onEvent(ForbiddenChargePlayingEvent event) {
            boolean isChargePlayingOpen = !event.isForbiddenChargePlaying;
            RobotState.get().setChargePlayingOpen(isChargePlayingOpen);
            if (!isChargePlayingOpen){
                SpeechServiceProxy.getInstance().speechStartTTS(StringUtil.getString(R.string.forbid_action_hint),null);
            }
        }
    };

    //设置边充边玩事件接收器
    private Subscriber<SetChargePlayEvent> setChargePlayEventSubscriber = new Subscriber<SetChargePlayEvent>() {
        @Override
        public void onEvent(SetChargePlayEvent event) {
            boolean isChargePlayingOpen = event.isChargePlayingOpen;
            RobotState.get().setChargePlayingOpen(isChargePlayingOpen);

            SetChargePlayingModeOp op = new SetChargePlayingModeOp(isChargePlayingOpen);
            RobotOpsManager.get(mContext).executeOp(op);
        }
    };

    private Subscriber<SavePowerEvent> savePowerEventSubscriber = new Subscriber<SavePowerEvent>() {
        @Override
        public void onEvent(SavePowerEvent savePowerEvent) {
            RobotTakeARest.instance().start(savePowerEvent.isSavePower);
        }
    };

    public ProxyPowerMangerImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(DCStateEvent.class, dcStateSubscriber);
        NotificationCenter.defaultCenter().subscriber(BatteryHighTemperatureEvent.class,batteryTempSubscirber);
        NotificationCenter.defaultCenter().subscriber(ElectricityEvent.class,electricitySubscriber);
        NotificationCenter.defaultCenter().subscriber(ForbiddenChargePlayingEvent.class, chargePlayingEventSubscriber);
        NotificationCenter.defaultCenter().subscriber(SavePowerEvent.class, savePowerEventSubscriber);
        NotificationCenter.defaultCenter().subscriber(SetChargePlayEvent.class, setChargePlayEventSubscriber);

    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(DCStateEvent.class, dcStateSubscriber);
        NotificationCenter.defaultCenter().unsubscribe(BatteryHighTemperatureEvent.class,batteryTempSubscirber);
        NotificationCenter.defaultCenter().unsubscribe(ElectricityEvent.class,electricitySubscriber);
        NotificationCenter.defaultCenter().unsubscribe(ForbiddenChargePlayingEvent.class, chargePlayingEventSubscriber);
        NotificationCenter.defaultCenter().unsubscribe(SavePowerEvent.class, savePowerEventSubscriber);
        NotificationCenter.defaultCenter().unsubscribe(SetChargePlayEvent.class, setChargePlayEventSubscriber);
    }
}
