package com.ubt.alpha2.download.db;

import android.content.Context;

import java.util.List;
import java.util.Objects;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;
    private final ThreadInfoDao mThreadInfoDao;
    private Object updateObject = new Object();
    private Object deleteObject = new Object();
    private Object insertObject = new Object();

    public static DataBaseManager getInstance(Context context) {
        if(sDataBaseManager == null){
            synchronized (DataBaseManager.class){
                if (sDataBaseManager == null) {
                    sDataBaseManager = new DataBaseManager(context);
                }
            }
        }
        return sDataBaseManager;
    }

    private DataBaseManager(Context context) {
        mThreadInfoDao = new ThreadInfoDao(context);
    }

    public void insert(ThreadInfo threadInfo) {
        synchronized (insertObject){
            mThreadInfoDao.insert(threadInfo);
        }
    }

    public void delete(String tag) {
        synchronized (deleteObject){
            mThreadInfoDao.delete(tag);
        }
    }

    public void update(String tag, int threadId, long finished) {
        synchronized(updateObject){
            mThreadInfoDao.update(tag, threadId, finished);
        }
    }

    public List<ThreadInfo> getThreadInfos(String tag) {
        return mThreadInfoDao.getThreadInfos(tag);
    }

    public List<ThreadInfo> getThreadInfos() {
        return mThreadInfoDao.getThreadInfos();
    }

    public boolean exists(String tag, int threadId) {
        return mThreadInfoDao.exists(tag, threadId);
    }
}
