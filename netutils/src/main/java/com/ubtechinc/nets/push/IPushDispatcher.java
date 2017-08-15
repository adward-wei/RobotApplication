package com.ubtechinc.nets.push;

/**
 * Created by bob.xu on 2017/6/28.
 */

interface IPushDispatcher {
    void onMessageReceive();
    void onNotificationClick();
}
