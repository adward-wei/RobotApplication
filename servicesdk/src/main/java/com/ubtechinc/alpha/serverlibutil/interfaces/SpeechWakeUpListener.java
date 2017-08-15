package com.ubtechinc.alpha.serverlibutil.interfaces;


/**
 * <pre>
 *   author: Logic
 *   email : logic.peng@ubtech.com
 *   time  : 2017/4/6
 *   desc  :
 * </pre>
 */
public interface SpeechWakeUpListener {

    void onSuccess();

    void onError(int errCode, String errDes);
}
