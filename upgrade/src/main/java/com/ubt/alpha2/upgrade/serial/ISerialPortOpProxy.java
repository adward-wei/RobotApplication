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

package com.ubt.alpha2.upgrade.serial;

import java.io.IOException;

/**
 * @desc : 串口命令接口类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
 interface ISerialPortOpProxy {

    void init() throws IOException;

    void clean();

    void sendCommand(byte sessionId, byte cmd, byte index, byte[] data, ISerialCommandCallback callback);

    boolean undoCommand(byte sessionId, byte cmd, byte index);

}
