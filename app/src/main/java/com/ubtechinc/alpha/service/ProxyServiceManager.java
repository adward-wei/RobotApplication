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

package com.ubtechinc.alpha.service;

import android.content.Context;

import com.ubtechinc.alpha.serial.ProxySerialCommandImpl;
import com.ubtechinc.alpha.task.AbstractProxyService;
import com.ubtechinc.alpha.task.ProxyAppManageImpl;
import com.ubtechinc.alpha.task.ProxyErrorTaskImpl;
import com.ubtechinc.alpha.task.ProxyIfytekOfflineSlotProcessImpl;
import com.ubtechinc.alpha.task.ProxyKeyProcessorImpl;
import com.ubtechinc.alpha.task.ProxyPowerMangerImpl;
import com.ubtechinc.alpha.task.ProxyPowerOffTaskImpl;
import com.ubtechinc.alpha.task.ProxySerialReadBackImpl;
import com.ubtechinc.alpha.task.ProxyServerRobotPhoneCommuniteImpl;
import com.ubtechinc.alpha.task.ProxyService;
import com.ubtechinc.alpha.task.ProxySoundDirectionImpl;
import com.ubtechinc.alpha.task.ProxySpeechDispatchImpl;
import com.ubtechinc.alpha.task.ProxyUpgradeTaskImpl;
import com.ubtechinc.alpha.task.ProxyWakupTaskImpl;
import com.ubtechinc.alpha.task.ProxyWifiStatusImpl;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @desc : ProxyServic的管理器，在主服务中启动
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */

public class ProxyServiceManager extends AbstractProxyService {

    private static ProxyServiceManager serviceManager;
    private final Context mContext;

    public static final String SERVICE_SPEECH_DISPATH = "speech_dispath";
    public static final String SERVICE_SPEECH_WAKUP = "speech_wakup";
    public static final String SERVICE_ERROR = "task_error";
    public static final String SERVICE_CALL = "speech_call";
    public static final String SERVICE_SERIAL_READBACK = "serial_readback";
    public static final String SERVICE_SERIAL_COMMAND = "serial_command";
//    public static final String SERVICE_ALPHA_BOOT = "alpha_boot";
    public static final String SERVICE_POWER_MANAGER = "alpha_power_manager";
    public static final String SERVICE_KEY_PROCESSOR = "alpha_key_processor";
    public static final String SERVICE_SOUND_DIRECTION = "alpha_sound_dir";
    public static final String SERVICE_POWER_OFF = "alpha_power_off";
    public static final String SERVICE_UPGRADE = "alpha_upgrade";
    public static final String SERVICE_WIFI_STATUS = "alpha_wifi_status";
    public static final String SERVICE_SERVER_ROBOT_PHONE = "alpha_server_robot_phone";
    public static final String SERVICE_APP_MANAGE = "alpha_app_manage";
    public static final String SERVICE_IFYTEK_OFFLINE_SLOT = "alpha_ifytek_offline_slot";

    private final HashMap<String, ProxyService> mServices = new HashMap<>();

    private ProxyServiceManager(Context cxt) {
        this.mContext = cxt;
    }

    public static ProxyServiceManager get(Context cxt){
        if (serviceManager == null){
            synchronized (ProxyServiceManager.class){
                if (serviceManager == null)
                    serviceManager = new ProxyServiceManager(cxt);
            }
        }
        return serviceManager;
    }

    @Override
    public void onCreate() {
        initProxyService();
    }

    public void initProxyService() {
        initService(SERVICE_SERIAL_READBACK);
        initService(SERVICE_SERIAL_COMMAND);//串口先启动
//        initService(SERVICE_ALPHA_BOOT);//
        initService(SERVICE_SPEECH_DISPATH);
        initService(SERVICE_SPEECH_WAKUP);
        initService(SERVICE_ERROR);
        initService(SERVICE_CALL);
        initService(SERVICE_POWER_MANAGER);
        initService(SERVICE_KEY_PROCESSOR);
        initService(SERVICE_SOUND_DIRECTION);
        initService(SERVICE_POWER_OFF);
        initService(SERVICE_UPGRADE);
        initService(SERVICE_WIFI_STATUS);
        initService(SERVICE_SERVER_ROBOT_PHONE);
        initService(SERVICE_APP_MANAGE);
        initService(SERVICE_IFYTEK_OFFLINE_SLOT);
    }

    @Override
    public void onDestroy() {
        destroyService();
    }

    private void destroyService() {
        Iterator<String> iterator =  mServices.keySet().iterator();
        while (iterator.hasNext()){
            mServices.get(iterator.next()).onDestroy();
            mServices.remove(iterator.next());
        }
    }

    private ProxyService initService(String id) {
        if (mServices.containsKey(id)) {
            return mServices.get(id);
        }
        ProxyService p = null;
        switch (id) {
            case SERVICE_SPEECH_DISPATH:
                p = new ProxySpeechDispatchImpl(mContext);
                break;
            case SERVICE_SPEECH_WAKUP:
                p = new ProxyWakupTaskImpl(mContext);
                break;
            case SERVICE_ERROR:
                p = new ProxyErrorTaskImpl(mContext);
                break;

            case SERVICE_SERIAL_READBACK:
                p = new ProxySerialReadBackImpl(mContext);
                break;
            case SERVICE_SERIAL_COMMAND:
                p = new ProxySerialCommandImpl(mContext);
                break;
//            case SERVICE_ALPHA_BOOT:
//                p = new ProxyAlphaBootImpl(mContext);
//                break;
            case SERVICE_POWER_MANAGER:
                p = new ProxyPowerMangerImpl(mContext);
                break;
            case SERVICE_KEY_PROCESSOR:
                p = new ProxyKeyProcessorImpl(mContext);
                break;
            case SERVICE_SOUND_DIRECTION:
                p = new ProxySoundDirectionImpl(mContext);
                break;
            case SERVICE_POWER_OFF:
                p = new ProxyPowerOffTaskImpl(mContext);
                break;
            case SERVICE_UPGRADE:
                p = new ProxyUpgradeTaskImpl(mContext);
                break;
            case SERVICE_WIFI_STATUS:
                p = new ProxyWifiStatusImpl(mContext);
                break;
            case SERVICE_SERVER_ROBOT_PHONE:
                p = new ProxyServerRobotPhoneCommuniteImpl(mContext);
                break;
            case SERVICE_APP_MANAGE:
                p = new ProxyAppManageImpl(mContext);
                break;
            case SERVICE_IFYTEK_OFFLINE_SLOT:
                p = new ProxyIfytekOfflineSlotProcessImpl(mContext);
                break;
            default:
                break;
        }
        if (p == null) return null;
        mServices.put(id, p);
        p.onCreate();
        return p;
    }

    public ProxySerialCommandImpl getSerialCommandProxy(){
        return (ProxySerialCommandImpl) mServices.get(SERVICE_SERIAL_COMMAND);
    }

    public void restartSerialCommandProxy(){
        ProxySerialCommandImpl impl = getSerialCommandProxy();
        if (impl!= null && impl.ready()){
            impl.onDestroy();
        }
        impl = new ProxySerialCommandImpl(mContext);
        mServices.put(SERVICE_SERIAL_COMMAND, impl);
        impl.onCreate();
    }
}
