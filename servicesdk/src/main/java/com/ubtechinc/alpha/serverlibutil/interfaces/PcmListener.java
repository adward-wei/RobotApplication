package com.ubtechinc.alpha.serverlibutil.interfaces;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/28
 * @modifier:
 * @modify_time:
 */

public interface PcmListener {
    void onPcmData(byte[] data, int dataLen);
}
