package com.ubt.alpha2.download.core;



import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.db.DataBaseManager;
import com.ubt.alpha2.download.db.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * author liwushu
 * description single thread implements for download
 * created 2017/6/20
 * version 1.0
 * modify liwushu
 */
public class SingleDownloadTask extends DownloadTaskImpl {

    public SingleDownloadTask(DownloadInfo mDownloadInfo, ThreadInfo mThreadInfo,DataBaseManager dataBaseManager,
                              OnDownloadListener mOnDownloadListener) {
        super(mDownloadInfo, mThreadInfo, dataBaseManager,mOnDownloadListener);
    }

    @Override
    protected int getResponseCode() {
        if(mThreadInfo.getStart()+mThreadInfo.getFinished() == 0)
            return HttpURLConnection.HTTP_OK;
        else
            return HttpURLConnection.HTTP_PARTIAL;
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        Map<String, String> headers = new HashMap<String, String>();
        long start = info.getStart() + info.getFinished();
        long end = info.getEnd();
        if(start == 0)
            return null;
        headers.put("Range", "bytes=" + start + "-" + end);
        return headers;
    }
}

