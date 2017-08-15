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

import com.ubtech.utilcode.utils.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import android_serialport_api.SerialPort;
import timber.log.Timber;

/**
 * @desc : alpha2串口文件读写
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */

class Alpha2SerialPortUtil implements ISerialPortUtil {

    private SerialPort file;
    private String mFileName;
    private final int mBandrate ;

     Alpha2SerialPortUtil(String defaultFileName, int bandrate){
        mFileName = defaultFileName;
        mBandrate = bandrate;
    }


    @Override
    public void open() throws IOException, SecurityException {
        if (file == null) {
            this.file = new SerialPort(new File(mFileName), mBandrate, 0);
        }
    }

    @Override
    public void close() {
        if (file != null){
            file.close();
            file = null;
        }
    }

    @Override
    public void write(ByteBuffer out) throws IOException {
        if (file == null)
            return;
        if (out == null || !out.hasRemaining())
            return;
        OutputStream outStream = file.getOutputStream();

        if (outStream != null) {
            byte[] bytes = new byte[out.remaining()];
            out.get(bytes);
            outStream.write(bytes);
            Timber.d("write bytes:%s", ConvertUtils.bytes2HexString(bytes));
            outStream.flush();
        }
    }

    @Override
    public int read(ByteBuffer in) throws IOException {
        if (in == null || file == null)
            return 0;
        InputStream inStream = file.getInputStream();

        if (inStream != null){
            int available = inStream.available();
            if (available > 0) {
                byte[] bytes = new byte[available];
                int read = inStream.read(bytes);
                Timber.d("read bytes:%s,length: %d",ConvertUtils.bytes2HexString(bytes),read);
                if (read <= 0) return 0;
                in.put(bytes);
                in.position(0);
                in.limit(read);
            }
            return available;
        }
        return 0;
    }
}
