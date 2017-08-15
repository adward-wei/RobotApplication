package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/4.
 */

public interface IAlpha2SerialPortCallback {
    void onListenSerialPortRecvData(byte[] bytes, int len);
}
