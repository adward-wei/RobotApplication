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

import com.ubtechinc.alpha.service.ProxyServiceManager;

import hugo.weaving.DebugLog;

/**
 * @desc : 串口命令接收器
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
 public class SerialCommandReceiver implements ISerialCommandInterface {
    private final Context mContext;

    public SerialCommandReceiver(Context context) {
        this.mContext = context;
    }

    /**
     * 同步方式执行一个命令
     *
     * @param cmd 串口命令码
     * @param param 命令数据
     */
    @Override
    @DebugLog
    public SerialCmdResult executeCommandSync(@SerialConstants.SerialType int type, byte cmd, byte[] param) {
        return ProxyServiceManager.get(mContext).getSerialCommandProxy().executeCommandSync(type,cmd, param);
    }

    /**
     * 撤销一个命令
     * @param type 串口命令类型
     * @param cmd  命令码
     */
    @Override
    public void undoCommand(@SerialConstants.SerialType int type, byte cmd) {
         ProxyServiceManager.get(mContext).getSerialCommandProxy().undoCommand(type, cmd);
    }

    @Override
    public boolean ready() {
        return ProxyServiceManager.get(mContext).getSerialCommandProxy().ready();
    }


}
