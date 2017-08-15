package com.ubt.alpha2.download.impl;


import com.ubt.alpha2.download.DownloadException;

/**
* @author liwushu
* @description
* @created 2017/6/20
* @version 1.0
* @modify liwushu
*/

public interface IConnectTask extends Runnable {

    interface OnConnectListener {
        void onConnecting();

        void onConnected(long time, long length, boolean isAcceptRanges);

        void onConnectPaused();

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void pause();

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
