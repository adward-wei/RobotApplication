package com.ubt.alpha2.download.impl;


import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;

/**
 * @author: liwushu
 * @description: interface for download response
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
*/
public interface IDownloadResponse {

    void onStarted();

    void onConnecting();

    void onConnected(long time, long length, boolean acceptRanges);

    void onConnectFailed(DownloadException e);

    void onConnectCanceled();

    void onDownloadProgress(long finished, long length, int percent);

    void onDownloadCompleted(DownloadInfo downloadInfo);

    void onDownloadPaused();

    void onDownloadCanceled();

    void onDownloadRetry();

    void onDownloadFailed(DownloadException e);

}
