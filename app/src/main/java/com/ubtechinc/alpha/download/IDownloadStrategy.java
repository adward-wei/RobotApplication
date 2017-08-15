package com.ubtechinc.alpha.download;

/**
 * @author：ubt
 * @description: 下载策略接口
 * @create: 2017/6/26
 * @email：slive.shu@ubtrobot.com
 * 
 */
public interface IDownloadStrategy {
    void initDownload(boolean isNeedMulThread);
    void download(String url);
    void cancel(String tag);
    void pause(String tag);
    void resume(String tag);
    void delete(String tag);
}
