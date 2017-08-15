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

import android.support.annotation.IntDef;

import com.ubtechinc.alpha.serial.SerialCommandReceiver;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/24
 * @modifier:
 * @modify_time:
 */

public interface ISerialCmdOp<T> {

    public static final int PREPARED = 1;
    public static final int RUNNING = 2;
    public static final int STOPPED = 3;
    @IntDef(value = {PREPARED, RUNNING, STOPPED})
    public @interface State{}

    public static final int MAX_PRIORITY = 0;
    public static final int NOR_PRIORITY = 1;
    public static final int MIN_PRIORITY = 2;
    @IntDef(value = {MAX_PRIORITY, NOR_PRIORITY, MIN_PRIORITY})
    public @interface Priority{}

    static final int TYPE_ACTION = 0;// 流程图动作操作
    static final int TYPE_MOTOR = 1;//舵机操作
    static final int TYPE_SYS_SET = 2;//系统设置操作
    static final int TYPE_UPGRADE = 3;//升级
    static final int TYPE_LED = 4;//灯控操作
    @IntDef(value = {TYPE_ACTION,TYPE_MOTOR,TYPE_SYS_SET,TYPE_UPGRADE, TYPE_LED})
    @interface OpType{}

    void prepare();

    OpResult<T> start(SerialCommandReceiver receiver);

    boolean stop(SerialCommandReceiver receiver);

    boolean running();

    boolean stopped();

    @ISerialCmdOp.OpType int getOpType();
}
