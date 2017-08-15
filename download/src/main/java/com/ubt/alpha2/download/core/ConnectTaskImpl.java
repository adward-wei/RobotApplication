package com.ubt.alpha2.download.core;

import android.os.Process;
import android.text.TextUtils;


import com.ubt.alpha2.download.Constants;
import com.ubt.alpha2.download.DownloadConfiguration;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.impl.IConnectTask;
import com.ubt.alpha2.download.util.LogUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author: liwushu
 * @description: Concrete implement for connect task
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
*/
public class ConnectTaskImpl implements IConnectTask {
    private final String mUri;
    private final IConnectTask.OnConnectListener mOnConnectListener;

    private volatile int mStatus;

    private volatile long mStartTime;

    private DownloadConfiguration mConfig;

    public ConnectTaskImpl(String uri, OnConnectListener listener, DownloadConfiguration config) {
        this.mUri = uri;
        this.mOnConnectListener = listener;
        this.mConfig = config;
    }

    @Override
    public void pause() {
        mStatus = DownloadStatus.STATUS_PAUSED;
    }

    public void cancel() {
        mStatus = DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isConnecting() {
        return mStatus == DownloadStatus.STATUS_CONNECTING;
    }

    @Override
    public boolean isConnected() {
        return mStatus == DownloadStatus.STATUS_CONNECTED;
    }

    @Override
    public boolean isPaused() {
        return mStatus == DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isCanceled() {
        return mStatus == DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isFailed() {
        return mStatus == DownloadStatus.STATUS_FAILED;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mStatus = DownloadStatus.STATUS_CONNECTING;
        mOnConnectListener.onConnecting();
        try {
            executeConnection();
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    private void executeConnection() throws DownloadException {
        mStartTime = System.currentTimeMillis();
        HttpURLConnection httpConnection = null;
        final URL url;
        try {
            url = new URL(mUri);
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Bad url.", e);
        }
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.HTTP.GET);
            if(mConfig.isSupportMulThread)
                httpConnection.setRequestProperty("Range", "bytes=" + 0 + "-");
            httpConnection.setRequestProperty("Accept-Encoding", "identity");
            final int responseCode = httpConnection.getResponseCode();
            LogUtils.d("slive","responseCode: "+responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                parseResponse(httpConnection, false);
            } else if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                parseResponse(httpConnection, true);
            } else {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "UnSupported response code:" + responseCode);
            }
        } catch (ProtocolException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "IO error", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    private void parseResponse(HttpURLConnection httpConnection, boolean isAcceptRanges) throws DownloadException {

        final long length;
        String contentLength = httpConnection.getHeaderField("Content-Length");
        LogUtils.d("slive","contentLength: "+contentLength);
        if (TextUtils.isEmpty(contentLength) || contentLength.equals("0") || contentLength.equals("-1")) {
            length = httpConnection.getContentLength();
        } else {
            length = Long.parseLong(contentLength);
        }

        if (length <= 0) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "length <= 0");
        }

        checkCanceledOrPaused();

        // Successful
        mStatus = DownloadStatus.STATUS_CONNECTED;
        final long timeDelta = System.currentTimeMillis() - mStartTime;
        mOnConnectListener.onConnected(timeDelta, length, isAcceptRanges);
    }

    private void checkCanceledOrPaused() throws DownloadException {
        if (isCanceled()) {
            // cancel
            throw new DownloadException(DownloadStatus.STATUS_CANCELED, "Connection Canceled!");
        } else if (isPaused()) {
            // paused
            throw new DownloadException(DownloadStatus.STATUS_PAUSED, "Connection Paused!");
        }
    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_FAILED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_FAILED;
                    mOnConnectListener.onConnectFailed(e);
                }
                break;
            case DownloadStatus.STATUS_PAUSED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_PAUSED;
                    mOnConnectListener.onConnectPaused();
                }
                break;
            case DownloadStatus.STATUS_CANCELED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_CANCELED;
                    mOnConnectListener.onConnectCanceled();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
