package com.ubtechinc.alpha.serverlibutil.aidl;
import com.ubtechinc.alpha.serverlibutil.aidl.LedInfo;

interface IRemoteLedListResultListener{
    void onGetLedList(int nOpId, int nErr, in List<LedInfo> oArrLed);
}