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

package com.ubtechinc.alpha.cmds;

import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 串口命令：小于0x80命令
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
public abstract class SerialCommand implements Command {

    private SerialCommandReceiver receiver;
    protected byte[] param;
    protected SerialCmdResult result;

    public SerialCommand(SerialCommandReceiver receiver , byte[] param){
        this.receiver = receiver;
        this.param = param;
    }

    @Override
    public void execute() {
        result = receiver.executeCommandSync(getSerialType(), getCmdId(), param);
    }

    public abstract byte getCmdId();

    abstract protected @SerialConstants.SerialType int getSerialType();

    @Override
    public void undo() {
        receiver.undoCommand(getSerialType(),getCmdId());
    }

    public SerialCmdResult getResult() {
        return result;
    }
}
