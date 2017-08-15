package com.ubt.alpha2.upgrade.bean;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.openqq.protocol.imsdk.msg;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.impl.IMainServiceHandlerListener;
import com.ubt.alpha2.upgrade.impl.IUnzipListener;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.MD5FileUtil;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;
import com.ubt.alpha2.upgrade.utils.ZipUtil;

import java.io.File;

/**
 * Created by ubt on 2017/6/29.
 */

public class MainServiceHandler {
    private int downType;
    private ServerVersionBean.ModuleInfo moduleInfo;
    IMainServiceHandlerListener listener;
    public MainServiceHandler(ServerVersionBean.ModuleInfo moduleInfo, int downType, IMainServiceHandlerListener listener){
        this.downType = downType;
        this.moduleInfo = moduleInfo;
        this.listener = listener;
    }

    public void unzipDownloadFile(){
        Context mc = UpgradeApplication.getContext();
        ZipUtil zipUtil = new ZipUtil(mc, iUnzipListener);
        try {
            zipUtil.unzip(FilePathUtils.DOWN_APK_ZIP_PATH, FilePathUtils.NEW_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IUnzipListener iUnzipListener = new IUnzipListener() {
        @Override
        public void onSuccess(String result) {
            LogUtils.d("解压成功");
            FileManagerUtils.deleteFile(FilePathUtils.DOWN_APK_ZIP_PATH);
            if(downType ==DownloadTypeInfo.DOWNLOAD_PATCH){
                //合patch
                File strFileName = new File(FilePathUtils.NEW_CONFIG_PATH);
                if(strFileName.exists()){
                    VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
                    if(versionConfigs == null){
                        return;
                    }

                    int count=versionConfigs.apks.size();
                    boolean isAllPatchOk = true;
                    //合patch包
                    for(int i =0;i<count;i++){
                        if(versionConfigs.apks.get(i).fileType.trim().equals("patchPackage")){
                            String packageName = versionConfigs.apks.get(i).packagename.trim();
                            String filename =FilePathUtils.NEW_PATH+"/"+versionConfigs.apks.get(i).filename.trim();
                            String patchMd5 = versionConfigs.apks.get(i).apkpackageMd5Value.trim();
                            if(!patchPackage(packageName,filename,patchMd5)){
                                isAllPatchOk = false;
                                break;
                            }
                        }else {
                            LogUtils.d("不是patchPackage");
                            continue;
                        }
                    }
                    if(isAllPatchOk){
                        //updateType 为1 时是紧急版本
                        if(moduleInfo.upgradeType.trim().equals("1")){
                            //进入安装流程
                            LogUtils.d("紧急版本，进入安装流程");
                            PropertyUtils.setSystemProperty(UpgradeApplication.getContext(), Constants.FORBIT_INSTALL,"false");
                        }else {
                            LogUtils.d("不是紧急版本，不处理");
                        }
                        handleSuccess();
                    }else {
                        //合成失败，删除本地下载包
                        FileManagerUtils.deleteAllFile(FilePathUtils.NEW_PATH);
                        //下载全量包
                        LogUtils.d("准备下载全量包");
                        downType = DownloadTypeInfo.DOWNLOAD_MAIN;
                        handleFailed();
                    }
                }
            }else {
                //进行md5分别验证
                boolean retMd5 = true;
                File strFileName = new File(FilePathUtils.NEW_CONFIG_PATH);
                if(strFileName.exists()){
                    VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
                    if(versionConfigs == null){
                        return;
                    }
                    int count=versionConfigs.apks.size();
                    for(int  i= 0;i<count;i++){
                        String packageName = versionConfigs.apks.get(i).packagename.trim();
                        String serverMd5 = versionConfigs.apks.get(i).apkpackageMd5Value.trim();
                        LogUtils.d("i=="+i);
                        String fileName = FilePathUtils.NEW_PATH+"/"+versionConfigs.apks.get(i).filename;
                        File big = new File(fileName);
                        LogUtils.d("FILE=="+fileName);
                        if(!big.exists()){
                            LogUtils.d("文件不存在");
                            retMd5 = false;
                            break;
                        }
                        String md5 = MD5FileUtil.getFileMD5String(big).toUpperCase();
                        LogUtils.d("packageName: "+packageName+"  md5:"+md5+"   serverMd5: "+serverMd5);
                        if(!md5.equals(serverMd5.toUpperCase())){
                            LogUtils.d("md5 != realMd5");
                            retMd5 = false;
                            break;
                        }
                    }
                }else {
                    LogUtils.d("strFileName 文件不存在");
                    retMd5 = false;
                }

                if(retMd5 ==false){//md5失败，删除全量包
                    LogUtils.d("downApkZip md5失败，删除全量包");
                    handleFailed();
                }else {
                    //验证成功，看是否是紧急版本  1 代表紧急版
                    if(!TextUtils.isEmpty(moduleInfo.upgradeType) &&moduleInfo.upgradeType.trim().equals("1")){
                        //进入安装流程
                        LogUtils.d("紧急版本，进入安装流程");
                        PropertyUtils.setSystemProperty(UpgradeApplication.getContext(),Constants.FORBIT_INSTALL,"false");
                        handleSuccess();
                    }else {
                        LogUtils.d("不是紧急版本，不处理");
                        handleSuccess();
                    }
                }
            }
        }

        @Override
        public void onFailure(String reason) {
            LogUtils.d("解压失败");
            //FileManagerUtils.deleteAllFile(FilePathUtils.NEW_PATH);
            if(downType == DownloadTypeInfo.DOWNLOAD_PATCH){
                downType = DownloadTypeInfo.DOWNLOAD_MAIN;
                //准备下全量包
                handleFailed();
            }else {
                //下载全量包失败，则不做处理
                LogUtils.d("解压全量包失败，则不做处理");
            }
        }
    };


    private boolean patchPackage(String packageName,String fileName,String patchMd5){
        if(!FileManagerUtils.patchNewFile(packageName,fileName,FilePathUtils.NEW_PATH,patchMd5.trim().toUpperCase())){
            LogUtils.d("patch不成功");
            return false;
        }
        LogUtils.d("patch成功");
        return true;
    }

    private void handleSuccess(){
        if(listener != null)
            listener.handleCompleted(true);
    }

    private void handleFailed(){
        String url = moduleInfo.urlMain;
        String md5Value = moduleInfo.MD5Main;
        if(listener != null){
            listener.reDownloadZipFile(url,md5Value);
        }
    }
}
