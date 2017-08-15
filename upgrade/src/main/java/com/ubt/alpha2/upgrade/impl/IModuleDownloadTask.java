package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/20.
 */

public interface IModuleDownloadTask{
    String getModuleName();
    void startDownload();
    void setDownloadListener(IDownloadListener downloadListener);
}
