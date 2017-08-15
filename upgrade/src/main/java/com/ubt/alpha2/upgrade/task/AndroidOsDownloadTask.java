package com.ubt.alpha2.upgrade.task;

import android.text.TextUtils;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.io.File;

/**
 * Created by ubt on 2017/7/20.
 */

public class AndroidOsDownloadTask extends BaseDownloadTask {

    private String md5Value;
    private String downloadUrl;

    @Override
    public String getModuleName() {
        return UpgradeModel.ANDROID_OS;
    }

    @Override
    public void startDownload() {
        ServerVersionBean.ModuleInfo moduleInfo = getModuleInfo();
        //返回数据异常
        if(moduleInfo == null){
            LogUtils.d("  moduleInfo == null");
            return;
        }
        //返回数据正常
        if(!TextUtils.isEmpty(moduleInfo.url) && !(TextUtils.isEmpty(moduleInfo.md5))){
            patchUrl = moduleInfo.url;
            patchMD5 = moduleInfo.md5;
            md5Value = patchMD5;
            downloadUrl = patchUrl;
        }else{
            mainUrl = moduleInfo.urlMain;
            mainMD5 = moduleInfo.MD5Main;
            md5Value = mainMD5;
            downloadUrl = mainUrl;
        }

        String md5_local;
        String filePath = FilePathUtils.DOWN_ANDROID_PATH;
        if(TextUtils.isEmpty(md5Value))
            return;
        File fold = new File(FilePathUtils.DOWN_ANDROID_FOLD);
        if(!fold.exists())
            fold.mkdirs();
        File file = new File(filePath);
        LogUtils.d("file.exists(): "+file.exists());
        if(file.exists()){
            md5_local = ApkUtils.getFileMd5Value(file);
            if(md5Value.equalsIgnoreCase(md5_local)){
                downloadSuccess(filePath,filePath);
                return;
            }
        }
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setFolder(fold);
        builder.setUri(downloadUrl);
        builder.setName(FilePathUtils.DOWN_ANDROID_NAME);
        downloadManager.download(builder.build(),downloadUrl,new SimpleCallBack(){
            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                File file = new File(downloadInfo.getDir(),downloadInfo.getName());
                if (ApkUtils.checkMd5Value(md5Value, file)) {
                    saveDownloadFileInfo(file.getAbsolutePath());
                    downloadSuccess(file.getAbsolutePath(),downloadUrl);
                } else {
                    FileManagerUtils.deleteFile(file.getAbsolutePath());
                    downloadFailed(downloadUrl);
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress){
                super.onProgress(finished,total,progress);
                LogUtils.d("finished: "+finished+"  progress: "+progress +"  total: "+total);
            }

            @Override
            public void onFailed(DownloadException e) {
                LogUtils.d("onFailed");
            }
        });
    }
}
