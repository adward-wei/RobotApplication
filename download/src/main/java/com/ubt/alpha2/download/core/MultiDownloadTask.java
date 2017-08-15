package com.ubt.alpha2.download.core;

import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.db.DataBaseManager;
import com.ubt.alpha2.download.db.ThreadInfo;
import com.ubt.alpha2.download.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liwushu
 * @description: multi thread download
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */
public class MultiDownloadTask extends DownloadTaskImpl {

    public MultiDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo, DataBaseManager dbManager,
                             OnDownloadListener listener) {
        super(downloadInfo, threadInfo, dbManager,listener);
    }


    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_PARTIAL;
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        Map<String, String> headers = new HashMap<String, String>();
        long start = info.getStart() + info.getFinished();
        long end = info.getEnd();
        headers.put("Range", "bytes=" + start + "-" + end);
        return headers;
    }

}
