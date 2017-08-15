package com.ubtechinc.alpha.download;

import com.ubtechinc.nets.DownloadInfo;

/**
 * Created by ubt on 2017/6/22.
 */

public interface IFileDownloadListener {
    void onCompleted(DownloadInfo info);
    void onError(Throwable e);
    void onProcess(int progress);
}
