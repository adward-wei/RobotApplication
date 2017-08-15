package com.ubt.alpha2.download.core;


import android.os.Process;

import com.ubt.alpha2.download.Constants;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.db.DataBaseManager;
import com.ubt.alpha2.download.db.ThreadInfo;
import com.ubt.alpha2.download.impl.IDownloadTask;
import com.ubt.alpha2.download.util.IOCloseUtils;
import com.ubt.alpha2.download.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author：ubt
 * @description:
 * @create: 2017/6/21
 * @email：slive.shu@ubtrobot.com
 */
public abstract class DownloadTaskImpl implements IDownloadTask {

    private static final long DEFAULT_POST_SIZE= 1*1024*1024;  //默认大小1M
    private final DownloadInfo mDownloadInfo;
    protected final ThreadInfo mThreadInfo;
    private final IDownloadTask.OnDownloadListener mOnDownloadListener;

    private volatile int mStatus;

    private volatile int mCommend = 0;

    private volatile long postProgressSize=0;
    private long receiveCount= 0;

    protected DataBaseManager mDBManager;

    public DownloadTaskImpl(DownloadInfo downloadInfo, ThreadInfo threadInfo,
                            DataBaseManager dataBaseManager, OnDownloadListener listener) {
        this.mDownloadInfo = downloadInfo;
        this.mThreadInfo = threadInfo;
        this.mOnDownloadListener = listener;
        this.mDBManager = dataBaseManager;
    }

    @Override
    public void cancel() {
        mCommend = DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public void pause() {
        mCommend = DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isDownloading() {
        return mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public boolean isComplete() {
        return mStatus == DownloadStatus.STATUS_COMPLETED;
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
        insertIntoDB(mThreadInfo);
        try {
            mStatus = DownloadStatus.STATUS_PROGRESS;

            if (mThreadInfo.getOnlywifi() == 1) {
                if (!DownloadManager.isWifiConnect()) {
                    throw new DownloadException(DownloadStatus.STATUS_PAUSED, "no wifi");
                }
            }
            executeDownload();
            synchronized (mOnDownloadListener) {
                mStatus = DownloadStatus.STATUS_COMPLETED;
                mOnDownloadListener.onDownloadCompleted();
            }
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    /**
     * handler the exception
     * @param e
     */
    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_FAILED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_FAILED;
                    mOnDownloadListener.onDownloadFailed(e);
                }
                break;
            case DownloadStatus.STATUS_PAUSED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_PAUSED;
                    mOnDownloadListener.onDownloadPaused();
                }
                break;
            case DownloadStatus.STATUS_CANCELED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_CANCELED;
                    mOnDownloadListener.onDownloadCanceled();
                }
                break;
            case DownloadStatus.STATUS_WAIT_FOR_RETRY:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_WAIT_FOR_RETRY;
                    mOnDownloadListener.onDownloadRetry();
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    /**
     * 执行下载任务
     * @throws DownloadException
     */
    private void executeDownload() throws DownloadException {
        final URL url;
        try {
            url = new URL(mThreadInfo.getUri());
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Bad url.", e);
        }

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.HTTP.GET);
            httpConnection.setRequestProperty("Accept-Encoding", "identity");
            setHttpHeader(getHttpHeaders(mThreadInfo), httpConnection);
            final int responseCode = httpConnection.getResponseCode();
            LogUtils.d("slive",""+getClass().getSimpleName()+"  threadId: "+mThreadInfo.getId()+"  responseCode: "+responseCode+"  getResponseCode: "+getResponseCode()
                    +"start: "+ mThreadInfo.getStart()+"  end: "+mThreadInfo.getEnd()+"  length: "+mThreadInfo.getFileLength()+" contentLength: ");
            postProgressSize = (mThreadInfo.getEnd()-mThreadInfo.getStart())*2/100;
            receiveCount = Math.min(postProgressSize,DEFAULT_POST_SIZE);
            if (responseCode == getResponseCode()) {
                transferData(httpConnection);
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

    /**
     * 设置http的请求头
     * @param headers
     * @param connection
     */
    private void setHttpHeader(Map<String, String> headers, URLConnection connection) {
        if (headers != null) {
            LogUtils.d("slive","headers: "+headers.toString());
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }

    private void transferData(HttpURLConnection httpConnection) throws DownloadException {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            try {
                inputStream = httpConnection.getInputStream();
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "http get inputStream error", e);
            }
            // 断点续传
            final long offset = mThreadInfo.getStart() + mThreadInfo.getFinished();
            try {
                raf = getFile(mDownloadInfo.getDir(), mDownloadInfo.getName(), offset);
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "File error", e);
            }
            transferData(inputStream, raf);
        } finally {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求网络数据
     * @param inputStream
     * @param raf
     * @throws DownloadException
     */
    private void transferData(InputStream inputStream, RandomAccessFile raf) throws DownloadException {
        final byte[] buffer = new byte[1024 * 2];
        try {
            while (true) {
                int len;
                checkPausedOrCanceled();

                len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                raf.write(buffer, 0, len);
                mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                receiveCount += len;
                mDownloadInfo.setFinished(mDownloadInfo.getFinished() + len);
                if (receiveCount >= postProgressSize) {
                    receiveCount -= postProgressSize;
                    synchronized (mOnDownloadListener) {
                        mOnDownloadListener.onDownloadProgress(mDownloadInfo.getFinished(), mDownloadInfo.getLength());
                    }
                    updateDB(mThreadInfo);
                }
            }
        }catch (Exception e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, e);
        }finally {
            try{
                if(raf != null)
                    raf.close();
                if(inputStream != null)
                    inputStream.close();
                mOnDownloadListener.onDownloadProgress(mDownloadInfo.getFinished(), mDownloadInfo.getLength());
            }catch (Exception e){
                throw new DownloadException(DownloadStatus.STATUS_FAILED, e);
            }
            updateDB(mThreadInfo);
        }
    }

    /**
     * 检测下载状态并做状态保存
     * @throws DownloadException
     */
    private void checkPausedOrCanceled() throws DownloadException {
        if (mCommend == DownloadStatus.STATUS_CANCELED) {
            // cancel
            throw new DownloadException(DownloadStatus.STATUS_CANCELED, "Download canceled!");
        } else if (mCommend == DownloadStatus.STATUS_PAUSED) {
            // pause
            updateDB(mThreadInfo);
            throw new DownloadException(DownloadStatus.STATUS_PAUSED, "Download paused!");
        }
    }

    /**
     * 保存下载的线程信息
     * @param info
     */
    protected void insertIntoDB(ThreadInfo info) {
        if (!mDBManager.exists(info.getTag(), info.getId())) {
            mDBManager.insert(info);
        }
    }

    /**
     * 更新下载信息
     * @param info
     */
    protected void updateDB(ThreadInfo info) {
        mDBManager.update(info.getTag(), info.getId(), info.getFinished());
    }

    /**
     * 打开文件并设置文件的写入起始偏移
     * @param dir
     * @param name
     * @param offset
     * @return
     * @throws IOException
     */
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(offset);
        return raf;
    }

    /**
     * 获取http的响应code
     * @return
     */
    abstract protected  int getResponseCode();

    /**
     * 设置http的请求头
     * @param info
     * @return
     */
    abstract protected  Map<String, String> getHttpHeaders(ThreadInfo info);

}
