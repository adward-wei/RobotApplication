package com.ubt.alpha2.download.core;

import android.os.Handler;

import com.ubt.alpha2.download.CallBack;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.impl.IDownloadStatusDelivery;

import java.util.concurrent.Executor;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */
public class DownloadStatusDeliveryImpl implements IDownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    public DownloadStatusDeliveryImpl(final Handler handler) {
        mDownloadStatusPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void post(DownloadStatus status) {
        mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));
    }

    private static class DownloadStatusDeliveryRunnable implements Runnable {
        private final DownloadStatus mDownloadStatus;
        private final CallBack mCallBack;

        public DownloadStatusDeliveryRunnable(DownloadStatus downloadStatus) {
            this.mDownloadStatus = downloadStatus;
            this.mCallBack = mDownloadStatus.getCallBack();
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTING:
                    mCallBack.onConnecting();
                    break;
                case DownloadStatus.STATUS_CONNECTED:
                    mCallBack.onConnected(mDownloadStatus.getLength(), mDownloadStatus.isAcceptRanges());
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    mCallBack.onProgress(mDownloadStatus.getFinished(), mDownloadStatus.getLength(),
                            mDownloadStatus.getPercent());
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    mCallBack.onCompleted(mDownloadStatus.getDownloadInfo());
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    mCallBack.onDownloadPaused();
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    mCallBack.onDownloadCanceled();
                    break;
                case DownloadStatus.STATUS_WAIT_FOR_RETRY:
                    mCallBack.onDownloadRetry();
                    break;
                case DownloadStatus.STATUS_FAILED:
                    mCallBack.onFailed((DownloadException) mDownloadStatus.getException());
                    break;
            }
        }
    }
}
