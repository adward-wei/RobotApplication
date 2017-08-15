package com.ubt.alpha2.download;

/**
 * @author: liwushu
 * @description:  configuration for download
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */
public class DownloadConfiguration {

    // 线程池中最大的线程数，支持两个任务同时下载，一个任务最大线程数为2,
    public static final int DEFAULT_MAX_THREAD_NUMBER = 5;

    public static final int DEFAULT_THREAD_NUMBER = 1;

    //默认支持两个任务并发下载
    public static final int DEFAULT_CONCURRENT_TASK = 2;

    public boolean isSupportMulThread = false;

    /**
     * thread number in the pool
     */
    private int maxThreadNum;

    /**
     * thread number for each download
     */
    private int threadNum;


    /**
     * init with default value
     */
    public DownloadConfiguration(boolean isSupportMulThread) {
        maxThreadNum = DEFAULT_MAX_THREAD_NUMBER;
        threadNum = DEFAULT_THREAD_NUMBER;
        this.isSupportMulThread = isSupportMulThread;
        if(isSupportMulThread)
            threadNum = 2;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
