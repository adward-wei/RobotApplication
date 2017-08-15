package com.ubtechinc.alpha.serverlibutil.aidl;


interface IRemoteLedOperationResultListener{
    void onLedOpResult(int nOpId, int error);
}  