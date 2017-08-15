package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.DownloadTypeInfo;
import com.ubt.alpha2.upgrade.bean.MainServiceHandler;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;
import com.ubt.alpha2.upgrade.impl.IMainServiceHandlerListener;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.StorageUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;

import java.io.File;

/**
 * @author: slive
 * @description:  主服务的下载
 * @create: 2017/7/20
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class MainServiceDownloadTask extends BaseDownloadTask {

    @Override
    public String getModuleName() {
        return UpgradeModel.MAINSERVICE_MODLE_NAME;
    }

    @Override
    public void startDownload() {
        startDownloadProcess(getModuleInfo());
    }

    public void startDownloadProcess(ServerVersionBean.ModuleInfo moduleInfo){
        if(!checkLocalVersion(moduleInfo)){
            startDownLoad(moduleInfo);
        }else{
        }
    }

    //下载
    void startDownLoad(ServerVersionBean.ModuleInfo moduleInfo){
        UpgradeApplication.getInstance().isFirstChecked = false;
        LogUtils.d("delete local old apk");
        FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
        //外部内存小于1g就不下载
        if (!StorageUtils.isAvailableExternalMemoryGreaterThan1G()) {
            LogUtils.d( "insufficient storage< 1G" );
            return;
        }
        //进入下载程序
        LogUtils.d("call download");
        if(moduleInfo.url.trim().equals(moduleInfo.urlMain.trim())){
            //下载的是全量包
            downType = DownloadTypeInfo.DOWNLOAD_MAIN;
            mainMD5 = moduleInfo.MD5Main;
            mainUrl = moduleInfo.urlMain;
        }else {
            //下载的是patch包
            downType = DownloadTypeInfo.DOWNLOAD_PATCH;
            patchMD5 = moduleInfo.md5;
            patchUrl = moduleInfo.url;
        }
        LogUtils.d("downType："+downType);
        download(moduleInfo,downType);
    }

    /**
     * 执行zip 文件下载
     * @param info
     * @param downloadType
     */
    private void download(ServerVersionBean.ModuleInfo moduleInfo, int downloadType){
        final String url = moduleInfo.url;
        String md5Value;
        if(downloadType == DownloadTypeInfo.DOWNLOAD_PATCH){
            md5Value = moduleInfo.md5;
        }else{
            md5Value = moduleInfo.MD5Main;
        }
        downloadZip(url,md5Value);
    }


    /**
     * 下载zip
     * @param url
     * @param md5value
     */
    private void downloadZip(final String url, final String md5value){
        File folder = new File(FilePathUtils.NEW_PATH);
        if(!folder.exists())
            folder.mkdirs();
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setUri(url).setName(FilePathUtils.DOWN_APK_ZIP_NAME).setFolder(folder);
        downloadManager.download(builder.build(),url,new SimpleCallBack(){
            @Override
            public void onFailed(DownloadException e) {
                LogUtils.e("DownloadException: "+e);
                downloadFailed(url);
            }
            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                final File file = new File(downloadInfo.getDir(), downloadInfo.getName());
                LogUtils.e("onCompleted: " + file.getAbsolutePath());
                if (ApkUtils.checkMd5Value(md5value, file)) {
                    MainServiceHandler handler = new MainServiceHandler(getModuleInfo(), downType, new IMainServiceHandlerListener() {
                        @Override
                        public void reDownloadZipFile(String url, String md5Value) {
                            downloadZip(url,md5Value);
                        }

                        @Override
                        public void handleCompleted(boolean isCompleted) {
                            if(isCompleted){
                                saveDownloadFileInfo(file.getAbsolutePath());
                                downloadSuccess(file.getAbsolutePath(),url);
                            }
                        }
                    });
                    handler.unzipDownloadFile();
                } else {
                    FileManagerUtils.deleteFile(file.getAbsolutePath());
                    if(downType == DownloadTypeInfo.DOWNLOAD_MAIN)
                        downloadFailed(url);
                    else{
                        downloadMain();
                    }
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                LogUtils.d("finished: "+finished+"  total: "+total+"  progress: "+progress);
            }
        });
    }

    private boolean checkLocalVersion(ServerVersionBean.ModuleInfo moduleInfo){
        File newConfig = new File(FilePathUtils.NEW_CONFIG_PATH);
        String version;
        //本地已有下载
        LogUtils.d("newConfig.exists(): "+newConfig.exists());
        if(newConfig.exists()) {
            VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
            if (versionConfigs == null) {
                return false;
            }
            LogUtils.d("local is already download version");
            version = versionConfigs.version.toLowerCase();
            // 判断本地版本与服务器版本
            if (version.equals(moduleInfo.toVersion.toLowerCase())) {
                //网络的新版本与本地下载版本相同，检测是否合成功，成功则准备去安装
                int count = versionConfigs.apks.size();
                boolean isDownloadOk = true;
                for (int i = 0; i < count; i++) {
                    if (versionConfigs.apks.get(i).fileType.trim().equals("patchPackage")) {
                        String fileName = FilePathUtils.NEW_PATH + versionConfigs.apks.get(i).packagename.trim() + ".apk";
                        File file = new File(fileName);
                        LogUtils.d("patchPackage: " + fileName);
                        if (!file.exists()) {
                            LogUtils.d("patchPackage  apk不存在");
                            isDownloadOk = false;
                            break;
                        }
                    } else if (versionConfigs.apks.get(i).fileType.trim().equals("apkPackage")) {
                        String fileName = FilePathUtils.NEW_PATH + "/" + versionConfigs.apks.get(i).filename.trim();
                        File file = new File(fileName);
                        LogUtils.d("apkPackage:" + fileName);
                        if (!file.exists()) {
                            LogUtils.d("apkPackage  apk不存在");
                            isDownloadOk = false;
                            break;
                        }
                    }
                }
                if (isDownloadOk) {
                    //已经下载过apk 进入安装程序
                    LogUtils.d("已经下载过apk 进入安装程序");
                    if (UpgradeApplication.getInstance().isFirstChecked) {
                        UpgradeApplication.getInstance().isFirstChecked = false;
                    }
                    downloadSuccess(FilePathUtils.NEW_CONFIG_PATH,"");
                    return true;
                }
                LogUtils.d("本地文件下载不全");
                //删除本地下载
                FileManagerUtils.deleteAllFile(FilePathUtils.NEW_PATH);
            }
        }
        //网络的新版本与本地下载版本不同，准备去下载
        LogUtils.d("网络的新版本与本地下载版本不同，准备去下载");
        return false;
    }

    private void downloadMain(){
        if(!isReDownload){
            isReDownload = true;
            downloadZip(mainUrl,mainMD5);
            return;
        }
        downloadFailed(mainUrl);
    }
}
