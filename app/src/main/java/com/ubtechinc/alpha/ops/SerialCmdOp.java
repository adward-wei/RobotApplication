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

package com.ubtechinc.alpha.ops;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @desc : 单一串口命令操作
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */
public abstract class SerialCmdOp<T>  implements ISerialCmdOp{

    protected @Priority int priority;
    protected @State int state = 0;
    protected @OpType int opType;

    private Semaphore semaphore = new Semaphore(0);

    public SerialCmdOp(@Priority int priority){
        this.priority = priority;
    }

    @Override
    synchronized public final void prepare(){
        state = PREPARED;
    }

    @Override
    synchronized public OpResult<T> start(SerialCommandReceiver receiver){
        if (state == PREPARED) {
            state = RUNNING;
            SerialCommand cmd = createCmd(receiver);
            if (cmd == null) {
                OpResult result = new OpResult();
                result.errorCode = SerialConstants.ERR_UNKNOW;
                return result;
            }
            cmd.execute();
            if (waitTime() > 0 && cmd.getResult().getError() == SerialConstants.ERR_OK){
                try {
                    semaphore.tryAcquire(1, waitTime(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            state = STOPPED;
            if (cmd.getResult() == null) {
                OpResult result = new OpResult();
                result.errorCode = SerialConstants.ERR_UNKNOW;
                result.cmd = cmd.getCmdId();
                return result;
            }
            return parseResult(cmd.getResult());
        }
        return null;
    }

    @Override
    synchronized public boolean running(){
        return state == RUNNING;
    }

    @Override
    synchronized public boolean stopped(){
        return state == STOPPED;
    }

    protected abstract int waitTime();

    protected abstract SerialCommand createCmd(SerialCommandReceiver receiver);

    protected abstract OpResult<T> parseResult(SerialCmdResult cmdResult);

    synchronized public boolean stop(SerialCommandReceiver receiver){
        return false;
    }

    @Override
    public int getOpType() {
        return opType;
    }
}
