package com.ubtechinc.alpha.serverlibutil.aidl;

interface ISpeechAsrListener{
    void onBegin();
    void onEnd();
    void onResult(String text);
    void onError(int code);
}  