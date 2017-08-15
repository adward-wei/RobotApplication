package com.ubtechinc.alpha.download;

import android.content.Context;

import com.ubt.alpha2.download.util.LogUtils;
import com.ubtechinc.nets.DownloadInfo;
import com.ubtechinc.nets.DownloadManager;

/**
 * @author：ubt
 * @description: 单线程下载
 * @create: 2017/6/22
 * @email：slive.shu@ubtrobot.com
 */
public class SingleThreadDownloadStrategy implements IDownloadStrategy {
    IFileDownloadListener listener;
    private DownloadManager downloadManager;
    private long costTime;

    public SingleThreadDownloadStrategy(Context mc, IFileDownloadListener fileDownloadListener){
        this.listener = fileDownloadListener;
    }

    @Override
    public void initDownload(boolean isMuliThread) {
        downloadManager = DownloadManager.getInstance();
    }

    @Override
    public void download(String url) {
        downloadManager.download(url, new DownloadManager.DownloadListener() {
            @Override
            public void onStart() {
                costTime = System.currentTimeMillis();
            }

            @Override
            public void onCompleted(DownloadInfo info) {
                if(listener != null){
                    listener.onCompleted(info);
                }
                LogUtils.d("slive","costTime: "+(System.currentTimeMillis()-costTime));
            }

            @Override
            public void onError(Throwable e) {
                if(listener != null){
                    listener.onError(e);
                }
            }

            @Override
            public void onProcess(int progress) {
                if(listener != null){
                    listener.onProcess(progress);
                }
            }
        });
    }

    @Override
    public void cancel(String url) {
        downloadManager.cancel(url);
    }

    @Override
    public void resume(String url) {

    }

    @Override
    public void pause(String url) {

    }

    @Override
    public void delete(String url) {

    }
}
