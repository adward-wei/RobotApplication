package com.ubt.alpha2.download.core;

import android.support.annotation.IntDef;

import com.ubt.alpha2.download.CallBack;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;

/**
 * @author: liwushu
 * @description: status for download
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
 */
public class DownloadStatus {
    //启动下载
    public static final int STATUS_STARTED = 101;
    //连接服务器
    public static final int STATUS_CONNECTING = 0x100;
    //服务器连接成功
    public static final int STATUS_CONNECTED = 0x101;
    //正在下载
    public static final int STATUS_PROGRESS = 0x102;
    //下载结束
    public static final int STATUS_COMPLETED = 0x103;
    //下载暂停
    public static final int STATUS_PAUSED = 0x104;
    //下载取消
    public static final int STATUS_CANCELED = 0x105;
    //下载失败
    public static final int STATUS_FAILED = 0x106;
    //等待重试
    public static final int STATUS_WAIT_FOR_RETRY = 0x107;

    private int status;
    private long time;
    private long length;
    private long finished;
    private int percent;
    private boolean acceptRanges;
    private DownloadException exception;
    private DownloadInfo mDownloadInfo;

    private CallBack callBack;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(DownloadException exception) {
        this.exception = exception;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DownloadInfo getDownloadInfo() {
        return mDownloadInfo;
    }

    public void setDownloadInfo(DownloadInfo mDownloadInfo) {
        this.mDownloadInfo = mDownloadInfo;
    }
}
