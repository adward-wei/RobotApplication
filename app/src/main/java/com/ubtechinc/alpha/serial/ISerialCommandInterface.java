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

/**
 * @desc : 串口命令接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/21
 * @modifier:
 * @modify_time:
 */

public interface ISerialCommandInterface {

    SerialCmdResult executeCommandSync(@SerialConstants.SerialType int type, byte cmd, byte[] param);

    void undoCommand(@SerialConstants.SerialType int type, byte cmd);

    boolean ready();
}
