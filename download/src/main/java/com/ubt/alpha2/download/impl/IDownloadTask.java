package com.ubt.alpha2.download.impl;

import com.ubt.alpha2.download.DownloadException;

/**
 * @author: liwushu
 * @description: Task for download
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
*/
public interface IDownloadTask extends Runnable {

    interface OnDownloadListener {
        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadRetry();

        void onDownloadFailed(DownloadException de);
    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
