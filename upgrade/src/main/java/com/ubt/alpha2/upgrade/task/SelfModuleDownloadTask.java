package com.ubt.alpha2.upgrade.task;

import android.text.TextUtils;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.io.File;

/**
 * Created by ubt on 2017/7/20.
 */

public class SelfModuleDownloadTask extends BaseDownloadTask {

    @Override
    public String getModuleName() {
        return UpgradeModel.SELF_MODLE_NAME;
    }

    @Override
    public void startDownload() {
        //返回数据异常
        ServerVersionBean.ModuleInfo moduleInfo = getModuleInfo();
        if(moduleInfo == null){
            LogUtils.d("moduleInfo == null");
            return ;
        }
        final String url= moduleInfo.urlMain;
        final String md5_server=moduleInfo.MD5Main;
        String md5_local;
        String filePath = FilePathUtils.DOWN_SELF_APK_PATH;
        if(TextUtils.isEmpty(md5_server))
            return;
        File fold = new File(FilePathUtils.SELF_PATH);
        if(!fold.exists())
            fold.mkdirs();
        File file = new File(filePath);
        //再次校验本地文件，排除数据库update 失败的情况
        if(file.exists()){
            md5_local = ApkUtils.getFileMd5Value(file);
            if(md5_server.equalsIgnoreCase(md5_local)){
                downloadSuccess(filePath,"");
                return;
            }
        }
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setFolder(fold);
        builder.setUri(url);
        builder.setName(FilePathUtils.DOWN_SELF_APK_NAME);
        downloadManager.download(builder.build(),url,new SimpleCallBack(){
            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                LogUtils.e("onCompleted");
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
                LogUtils.e("onFailed");
                downloadFailed(url);
            }
        });
    }

}
