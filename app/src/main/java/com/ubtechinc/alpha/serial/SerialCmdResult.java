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

import com.ubtech.utilcode.utils.ConvertUtils;

/**
 * @desc : 串口命令结果封装
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */
public final class SerialCmdResult {
    private   @SerialConstants.ErrorCode int error;
    private   byte[] result;
    private   byte cmd;

    public SerialCmdResult(byte cmdId, @SerialConstants.ErrorCode int err) {
        this.error = err;
        this.cmd = cmdId;
    }

    public SerialCmdResult(){}

    void setError(@SerialConstants.ErrorCode int error) {
        this.error = error;
    }

    void setResult(byte[] result) {
        this.result = result;
    }

    void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public @SerialConstants.ErrorCode int getError() {
        return error;
    }

    public byte[] getResult() {
        return result;
    }

    public byte getCmd() {
        return cmd;
    }

    @Override
    public String toString() {
        return String.format("SerialCmdResult[cmd=%s,resultCode=%d]", ConvertUtils.byte2HexString(cmd),error);
    }

}
