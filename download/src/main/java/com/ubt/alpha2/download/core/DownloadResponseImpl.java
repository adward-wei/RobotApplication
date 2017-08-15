package com.ubt.alpha2.download.core;


import com.ubt.alpha2.download.CallBack;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.impl.IDownloadResponse;
import com.ubt.alpha2.download.impl.IDownloadStatusDelivery;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */
public class DownloadResponseImpl implements IDownloadResponse {
    private IDownloadStatusDelivery mDelivery;
    private DownloadStatus mDownloadStatus;

    public DownloadResponseImpl(IDownloadStatusDelivery delivery, CallBack callBack) {
        mDelivery = delivery;
        mDownloadStatus = new DownloadStatus();
        mDownloadStatus.setCallBack(callBack);
    }

    @Override
    public void onStarted() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_STARTED);
        mDownloadStatus.getCallBack().onStarted();
    }

    @Override
    public void onConnecting() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTING);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnected(long time, long length, boolean acceptRanges) {
        mDownloadStatus.setTime(time);
        mDownloadStatus.setAcceptRanges(acceptRanges);
        mDownloadStatus.setLength(length);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnectFailed(DownloadException e) {
        mDownloadStatus.setException(e);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnectCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadProgress(long finished, long length, int percent) {
        mDownloadStatus.setFinished(finished);
        mDownloadStatus.setLength(length);
        mDownloadStatus.setPercent(percent);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PROGRESS);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadCompleted(DownloadInfo downloadInfo) {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_COMPLETED);
        mDownloadStatus.setDownloadInfo(downloadInfo);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadPaused() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PAUSED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadRetry() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_WAIT_FOR_RETRY);
        mDelivery.post(mDownloadStatus);
    }


    @Override
    public void onDownloadFailed(DownloadException e) {
        mDownloadStatus.setException(e);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDelivery.post(mDownloadStatus);
    }
}
