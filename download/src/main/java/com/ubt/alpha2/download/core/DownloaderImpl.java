package com.ubt.alpha2.download.core;


import com.ubt.alpha2.download.DownloadConfiguration;
import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.db.DataBaseManager;
import com.ubt.alpha2.download.db.ThreadInfo;
import com.ubt.alpha2.download.impl.IConnectTask;
import com.ubt.alpha2.download.impl.IDownloadResponse;
import com.ubt.alpha2.download.impl.IDownloadTask;
import com.ubt.alpha2.download.impl.IDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */
public class DownloaderImpl implements IDownloader, IConnectTask.OnConnectListener, IDownloadTask.OnDownloadListener {

    private DownloadRequest mRequest;

    private IDownloadResponse mResponse;

    private Executor mExecutor;

    private DataBaseManager mDBManager;

    private String mTag;

    private DownloadConfiguration mConfig;

    private IDownloader.OnDownloaderDestroyedListener mListener;

    private int mStatus;

    private DownloadInfo mDownloadInfo;

    private IConnectTask mConnectTask;

    private List<IDownloadTask> mDownloadTasks;

    public DownloaderImpl(DownloadRequest request, IDownloadResponse response, Executor executor,
                          DataBaseManager dbManager, String key, DownloadConfiguration config,
                          OnDownloaderDestroyedListener listener) {
        mRequest = request;
        mResponse = response;
        mExecutor = executor;
        mDBManager = dbManager;
        mTag = key;
        mConfig = config;
        mListener = listener;
        init();
    }

    @Override
    public boolean isOnlyWifi() {
        if (mRequest.getNetStatus() == DownloadRequest.NetStatus.Wifi) {
            return true;
        }
        return false;
    }

    private void init() {
        int onlywifi = 0;
        if (mRequest.getNetStatus().equals(DownloadRequest.NetStatus.Wifi)) {
            onlywifi = 1;
        }

        mDownloadInfo =
                new DownloadInfo(mRequest.getName().toString(), mRequest.getUri(), mRequest.getFolder(), onlywifi);
        mDownloadTasks = new LinkedList<>();
    }

    @Override
    public boolean isRunning() {
        return mStatus == DownloadStatus.STATUS_STARTED
                || mStatus == DownloadStatus.STATUS_CONNECTING
                || mStatus == DownloadStatus.STATUS_CONNECTED
                || mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public void start() {
        mStatus = DownloadStatus.STATUS_STARTED;
        mResponse.onStarted();
        connect();
    }

    @Override
    public void pause() {
        if (mConnectTask != null) {
            mConnectTask.pause();
        }
        for (IDownloadTask task : mDownloadTasks) {
            task.pause();
        }
        if (mStatus != DownloadStatus.STATUS_PROGRESS) {
            onDownloadPaused();
        }
    }

    @Override
    public void cancel() {
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }
        for (IDownloadTask task : mDownloadTasks) {
            task.cancel();
        }
        if (mStatus != DownloadStatus.STATUS_CANCELED) {
            onDownloadCanceled();
        }
    }

    @Override
    public void onDestroy() {
        // trigger the onDestroy callback tell download manager
        mListener.onDestroyed(mTag, this);
    }

    @Override
    public void onConnecting() {
        mStatus = DownloadStatus.STATUS_CONNECTING;
        mResponse.onConnecting();
    }

    @Override
    public void onConnected(long time, long length, boolean isAcceptRanges) {
        if (mConnectTask.isCanceled()) {
            // despite connection is finished, the entire downloader is canceled
            onConnectCanceled();
        } else {
            mStatus = DownloadStatus.STATUS_CONNECTED;
            mResponse.onConnected(time, length, isAcceptRanges);

            mDownloadInfo.setAcceptRanges(isAcceptRanges);
            mDownloadInfo.setLength(length);
            download(length, isAcceptRanges);
        }
    }

    @Override
    public void onConnectPaused() {
        onDownloadPaused();
    }



    @Override
    public void onConnectCanceled() {
        deleteFromDB();
        deleteFile();
        mStatus = DownloadStatus.STATUS_CANCELED;
        mResponse.onConnectCanceled();
        onDestroy();
    }

    @Override
    public void onConnectFailed(DownloadException de) {
        if (mConnectTask.isCanceled()) {
            // despite connection is failed, the entire downloader is canceled
            onConnectCanceled();
        } else if (mConnectTask.isPaused()) {
            // despite connection is failed, the entire downloader is paused
            onDownloadPaused();
        } else {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onConnectFailed(de);
            onDestroy();
        }
    }

    @Override
    public void onDownloadConnecting() {}

    @Override
    public void onDownloadProgress(long finished, long length) {
        // calculate percent
        final int percent = (int) (finished * 100 / length);
        mResponse.onDownloadProgress(finished, length, percent);
    }

    @Override
    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_COMPLETED;
            mResponse.onDownloadCompleted(mDownloadInfo);
            onDestroy();
        }
    }

    @Override
    public void onDownloadPaused() {
        if (isAllPaused()) {
            mStatus = DownloadStatus.STATUS_PAUSED;
            mResponse.onDownloadPaused();
            onDestroy();
        }
    }

    @Override
    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            deleteFromDB();
            deleteFile();
            mStatus = DownloadStatus.STATUS_CANCELED;
            mResponse.onDownloadCanceled();
            onDestroy();
        }
    }

    @Override
    public void onDownloadRetry() {
        mStatus = DownloadStatus.STATUS_WAIT_FOR_RETRY;
        mResponse.onDownloadRetry();
        onDestroy();
    }


    @Override
    public void onDownloadFailed(DownloadException de) {
        if (isAllFailed()) {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onDownloadFailed(de);
            onDestroy();
        }
    }

    private void connect() {
        mConnectTask = new ConnectTaskImpl(mRequest.getUri(), this,mConfig);
        mExecutor.execute(mConnectTask);
    }

    private void download(long length, boolean acceptRanges) {
        mStatus = DownloadStatus.STATUS_PROGRESS;
        initFile(length);
        initDownloadTasks(length, acceptRanges);
        // start tasks
        for (IDownloadTask downloadTask : mDownloadTasks) {
            mExecutor.execute(downloadTask);
        }
    }

    private void initFile(long length){
        if(!mRequest.getFolder().exists())
            mRequest.getFolder().mkdirs();
        File file = new File(mRequest.getFolder(),mRequest.getName().toString());
        if(!file.exists()){
            try {
                // 本地文件不存在，需要删除数据库的记录
                mDBManager.delete(mTag);
                file.createNewFile();
                try {
                    RandomAccessFile raf = new RandomAccessFile(file,"rw");
                    raf.setLength(length);
                    raf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // TODO
    private void initDownloadTasks(long length, boolean acceptRanges) {
        mDownloadTasks.clear();
        if (acceptRanges) {
            List<ThreadInfo> threadInfos = getMultiThreadInfos(length);
            // init finished
            int finished = 0;
            for (ThreadInfo threadInfo : threadInfos) {
                finished += threadInfo.getFinished();
            }
            mDownloadInfo.setFinished(finished);
            for (ThreadInfo info : threadInfos) {
                if(info.getFinished() == (info.getEnd()-info.getStart()+1))
                    continue;
                if (mRequest.activeRequest == true) {
                    if (mRequest.getNetStatus().equals(DownloadRequest.NetStatus.Mobile)) {
                        info.setOnlywifi(0);
                    } else {
                        info.setOnlywifi(1);
                    }
                    mDownloadInfo.setActiveRequest(mRequest.activeRequest);
                }
                mDownloadTasks.add(new MultiDownloadTask(mDownloadInfo, info, mDBManager, this));
            }
        } else {
            ThreadInfo info = getSingleThreadInfo(length);
            info.setOnlywifi(mDownloadInfo.getOnlywifi());
            mDownloadTasks.add(new SingleDownloadTask(mDownloadInfo, info,mDBManager, this));
        }
    }

    protected List<ThreadInfo> getMultiThreadInfos(long length) {
        // init threadInfo from db
        final List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(mTag);
        if (threadInfos.isEmpty()) {
            final int threadNum = mConfig.getThreadNum();
            for (int i = 0; i < threadNum; i++) {
                // calculate average
                final long average = length / threadNum;
                final long start = average * i;
                final long end;
                if (i == threadNum - 1) {
                    end = length-1;
                } else {
                    end = start + average - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mTag, mRequest.getUri(), start, end, 0);
                threadInfo.setFileLength(length);
                threadInfos.add(threadInfo);
            }
        }
        return threadInfos;
    }

    private ThreadInfo getSingleThreadInfo(long length){
        final List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(mTag);
        if(threadInfos.isEmpty()){
            return new ThreadInfo(0,mTag,mRequest.getUri(),0,length,0);
        }
        return threadInfos.get(0);
    }

    private boolean isAllComplete() {
        boolean allFinished = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (!task.isComplete()) {
                allFinished = false;
                break;
            }
        }
        return allFinished;
    }

    private boolean isAllFailed() {
        boolean allFailed = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allFailed = false;
                break;
            }
        }
        return allFailed;
    }

    private boolean isAllPaused() {
        boolean allPaused = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allPaused = false;
                break;
            }
        }
        return allPaused;
    }

    private boolean isAllCanceled() {
        boolean allCanceled = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allCanceled = false;
                break;
            }
        }
        return allCanceled;
    }

    private void deleteFromDB() {
        mDBManager.delete(mTag);
    }

    private void deleteFile() {
        File file = new File(mDownloadInfo.getDir(), mDownloadInfo.getName());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

}
