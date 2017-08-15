package com.ubt.alpha2.download.impl;

/**
 * @author: liwushu
 * @description: interface for downloader
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
*/
public interface IDownloader {

    public interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, IDownloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

    boolean isOnlyWifi();

}
