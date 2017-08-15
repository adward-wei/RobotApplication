package com.ubt.alpha2.upgrade.task;

import android.text.TextUtils;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.serial.SerialConstants;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.io.File;

/**
 * Created by ubt on 2017/7/20.
 */

abstract public class EmbeddedModelDownloadTask extends BaseDownloadTask {

    @Override
    public void startDownload() {
        ServerVersionBean.ModuleInfo moduleInfo = getModuleInfo();
        //数据异常
        if(moduleInfo == null){
            LogUtils.d("moduleInfo == null");
            return;
        }

        //数据正常
        final String url= moduleInfo.urlMain;
        final String md5_server=moduleInfo.MD5Main;
        String md5_local;
        if(TextUtils.isEmpty(md5_server)) {
            LogUtils.d("md5_server == null");
            return;
        }
        File fold = new File(FilePathUtils.DOWN_EMBEDDED_PATH);
        String fileName = FileManagerUtils.getFileName(url);
        LogUtils.d("fileName: "+fileName);
        if(!fold.exists())
            fold.mkdirs();
        File file = new File(fold,fileName);
        if(file.exists()){
            md5_local = ApkUtils.getFileMd5Value(file);
            if(md5_server.equalsIgnoreCase(md5_local)){
                downloadSuccess(file.getAbsolutePath(),"");
                return;
            }
        }
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setFolder(fold);
        builder.setUri(url);
        builder.setName(fileName);
        downloadManager.download(builder.build(),url,new SimpleCallBack(){
            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                File fileName = new File(downloadInfo.getDir(),downloadInfo.getName());
                if(ApkUtils.checkMd5Value(md5_server,fileName)){
                    saveDownloadFileInfo(fileName.getAbsolutePath());
                    downloadSuccess(fileName.getAbsolutePath(),url);
                }else{
                    FileManagerUtils.deleteFile(fileName.getAbsolutePath());
                    downloadFailed(url);
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress){
                super.onProgress(finished,total,progress);
                LogUtils.d("finished: "+finished+"  progress: "+progress +"  total: "+total);
            }

            @Override
            public void onFailed(DownloadException e) {
                downloadFailed(url);
            }
        });
    }

    abstract protected @SerialConstants.SerialType int getUpgradeType();
}
