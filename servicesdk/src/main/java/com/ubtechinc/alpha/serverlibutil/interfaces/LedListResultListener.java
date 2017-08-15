package com.ubtechinc.alpha.serverlibutil.interfaces;


import com.ubtechinc.alpha.serverlibutil.aidl.LedInfo;

import java.util.List;

/**
 * @Author  adward.wei
 */
public interface LedListResultListener {
    void onGetLedList(int nOpId, int nErr, List<LedInfo> oArrLed);
}

