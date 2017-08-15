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

package com.ubtechinc.alpha.serial;

import android.content.Context;

import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.event.SerialStateEvent;
import com.ubtechinc.alpha.task.AbstractProxyService;
import com.ubtechinc.alpha.utils.SysUtils;

import java.io.IOException;

/**
 * @desc : 串口服务启动代理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */
public class ProxySerialCommandImpl extends AbstractProxyService implements ISerialCommandInterface {

    private final Context mContext;
    private SerialCommandExecutor chestCmdExecutor;
    private SerialCommandExecutor headerCmdExecutor;
    private boolean chestInitSuccess = false;
    private boolean headerInitSuccess = false;

    public ProxySerialCommandImpl(Context context){
        this.mContext = context;
        chestCmdExecutor = new SerialCommandExecutor(mContext, SerialConstants.TYPE_CHEST);
        headerCmdExecutor = new SerialCommandExecutor(mContext, SerialConstants.TYPE_HEADER);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            chestCmdExecutor.init();
            chestInitSuccess = true;
            if (SysUtils.is2Mic()) {
                headerCmdExecutor.init();
            }
            headerInitSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (headerInitSuccess && chestInitSuccess){
            NotificationCenter.defaultCenter().publish(new SerialStateEvent(SerialConstants.STARTUP));
        }
    }

    @Override
    public void onDestroy() {
        NotificationCenter.defaultCenter().publish(new SerialStateEvent(SerialConstants.SHUTDOWN));
        chestCmdExecutor.clean();
        chestInitSuccess = false;
        headerCmdExecutor.clean();
        headerInitSuccess = false;
        super.onDestroy();
    }

    @Override
    public SerialCmdResult executeCommandSync(@SerialConstants.SerialType int type, final byte cmd, final byte[] param) {
        if (type == SerialConstants.TYPE_CHEST && chestInitSuccess){
            return chestCmdExecutor.executeCommand(cmd, param);
        }else if (type == SerialConstants.TYPE_HEADER && headerInitSuccess){
            return headerCmdExecutor.executeCommand(cmd, param);
        }
        return null;
    }

    @Override
    public void undoCommand(@SerialConstants.SerialType int type, byte cmd) {
        if (type == SerialConstants.TYPE_CHEST && chestInitSuccess){
             chestCmdExecutor.undoCommand(cmd);
        }else if (type == SerialConstants.TYPE_HEADER && headerInitSuccess){
             headerCmdExecutor.undoCommand(cmd);
        }
    }

    @Override
    public boolean ready() {
        return headerInitSuccess && chestInitSuccess;
    }
}
