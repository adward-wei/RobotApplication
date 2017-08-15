package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/20.
 */

public interface IDownloadListener {
    void onDownloadSuccess(String moduleName,String filepath,String url);
    void onDownloadFailed(String moduleName,String url);
}
