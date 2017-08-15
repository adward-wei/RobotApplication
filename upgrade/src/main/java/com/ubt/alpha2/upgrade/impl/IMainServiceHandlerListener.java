package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/6/29.
 */

public interface IMainServiceHandlerListener {
    void reDownloadZipFile(String url,String md5Value);
    void handleCompleted(boolean isCompleted);
}
