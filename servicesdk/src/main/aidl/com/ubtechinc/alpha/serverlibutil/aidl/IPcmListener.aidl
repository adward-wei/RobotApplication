// IPcmListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

interface IPcmListener {

   /**
    *@Description pcm录音回调
    *@param data 录音数据,
    *@param dataLen 有效长度
    */
   void onPcmData(in byte[] data, int dataLen);
}
