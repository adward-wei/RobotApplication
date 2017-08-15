package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/4.
 */

public interface IAlpha2SerialPortService {

    //串口发命令控制
    boolean sendCommand(byte nSessionID, byte nCmd, byte[] nParam, int nLen);
    boolean sendRawData(byte[] data, int nLen);
}
