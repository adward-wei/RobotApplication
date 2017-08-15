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

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @desc : 串口文件读写接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */
 interface ISerialPortUtil {

     String ALPHA2_HEADER_SERIAL_FILE = "/dev/ttySAC1";
     String ALPHA2_CHEST_SERIAL_FILE = "/dev/ttySAC0";
     String CHEST_SYS_PROPERTY_NAME = "ro.front.serial";
     int BANDRATE = 115200;

    void open() throws IOException;

    void close();

    void write(ByteBuffer out) throws IOException;

    int read(ByteBuffer in) throws IOException;
}
