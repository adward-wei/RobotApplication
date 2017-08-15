package com.ubtechinc.alpha.download;

import android.content.Context;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.download.util.LogUtils;

import java.io.File;

/**
 * @author：ubt
 * @description:单/多线程、断点下载
 * @create: 2017/6/22
 * @email：slive.shu@ubtrobot.com
 */

public class MixThreadDownloadStrategy implements IDownloadStrategy {
    private static final String TAG = "MixThreadDownloadStrate";
    IFileDownloadListener listener;
    DownloadManager downloadManager;
    Context mc;
    private long costTime;

    public MixThreadDownloadStrategy(Context mc, IFileDownloadListener listener){
        this.listener = listener;
        this.mc = mc;
    }

    @Override
    public void initDownload(boolean isNeedMulThread) {
        downloadManager = DownloadManager.getInstance();
        downloadManager.init(mc,isNeedMulThread);
    }

    @Override
    public void download(String url) {
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setUri(url).build();
        downloadManager.download(builder.build(),url,new SimpleCallBack(){

            @Override
            public void onStarted(){
                costTime = System.currentTimeMillis();
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                if(listener != null){
                    listener.onProcess(progress);
                }
            }

            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                LogUtils.d(TAG,"costTime: "+(System.currentTimeMillis()-costTime));
                com.ubtechinc.nets.DownloadInfo downloadInfoValue = new com.ubtechinc.nets.DownloadInfo(downloadInfo.getUri());
                downloadInfoValue.setLength(downloadInfo.getLength());
                downloadInfoValue.setFilePath(downloadInfo.getDir().getAbsolutePath()+ File.separator+downloadInfo.getName());
                downloadInfoValue.setTotal(downloadInfo.getFinished());
                LogUtils.d(TAG,"FilePath: "+(downloadInfo.getDir().getAbsolutePath()+ File.separator+downloadInfo.getName()));
                if(listener != null){
                    listener.onCompleted(downloadInfoValue);
                }
            }

            @Override
            public void onDownloadPaused() {

            }

            @Override
            public void onDownloadCanceled() {

            }

            @Override
            public void onFailed(DownloadException e) {
                if(listener != null){
                    listener.onError(e);
                }
            }
        });
    }

    @Override
    public void cancel(String tag) {
        downloadManager.cancel(tag);
    }

    @Override
    public void resume(String tag) {

    }

    @Override
    public void pause(String tag) {
        downloadManager.pause(tag);
    }

    @Override
    public void delete(String tag) {
        downloadManager.delete(tag);
    }
}
