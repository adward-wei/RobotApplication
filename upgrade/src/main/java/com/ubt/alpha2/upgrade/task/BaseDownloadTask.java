package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.DownloadTypeInfo;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.db.UpgradeDbManager;
import com.ubt.alpha2.upgrade.db.UpgradeTable;
import com.ubt.alpha2.upgrade.impl.IDownloadListener;
import com.ubt.alpha2.upgrade.impl.IModuleDownloadTask;

/**
 * Created by ubt on 2017/7/20.
 */

public abstract class BaseDownloadTask implements IModuleDownloadTask {
    IDownloadListener downloadListener;
    protected boolean isReDownload = false;
    protected String mainMD5;
    protected String patchMD5;
    protected String mainUrl;
    protected String patchUrl;
    protected DownloadManager downloadManager;
    @DownloadTypeInfo.DownloadType
    protected int downType = DownloadTypeInfo.DOWNLOAD_PATCH;

    public BaseDownloadTask(){
        downloadManager = DownloadManager.getInstance();
        downloadManager.init(UpgradeApplication.getContext(),true);
    }

    protected void downloadSuccess(String filepath,String url){
        if(downloadListener != null)
            downloadListener.onDownloadSuccess(getModuleName(),filepath,url);
    }

    protected void downloadFailed(String url) {
        if (isReDownload) {
            if (downloadListener != null)
                downloadListener.onDownloadFailed(getModuleName(),url);
            return;
        }
        reDownload();
    }

    private void reDownload(){
        isReDownload = true;
        startDownload();
    }

    @Override
    public void setDownloadListener(IDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    protected ServerVersionBean.ModuleInfo  getModuleInfo(){
        return UpgradeModuleManager.getInstance().getUpgradeModuleInfo(getModuleName());
    }

    /**
     * save the download file info
     * @param localFilepath
     */
    protected void saveDownloadFileInfo(String localFilepath){
        UpgradeTable.UpgradeInfo upgradeTabInfo = new UpgradeTable.UpgradeInfo();
        upgradeTabInfo.localFilepath = localFilepath;
        upgradeTabInfo.downloadSuccess = 1;
        ServerVersionBean.ModuleInfo moduleInfo = getModuleInfo();
        if(moduleInfo == null)
            return ;
        upgradeTabInfo.fromVersion = moduleInfo.fromVersion;
        upgradeTabInfo.toVersion = moduleInfo.toVersion;
        upgradeTabInfo.md5 = moduleInfo.md5;
        upgradeTabInfo.module_id = moduleInfo.module_id;
        upgradeTabInfo.remark = moduleInfo.remark;
        upgradeTabInfo.urlMain = moduleInfo.urlMain;
        upgradeTabInfo.url = moduleInfo.url;
        upgradeTabInfo.MD5Main = moduleInfo.MD5Main;
        upgradeTabInfo.updateType = moduleInfo.updateType;
        upgradeTabInfo.upgradeType = moduleInfo.upgradeType;
        upgradeTabInfo.module_name = getModuleName();
        UpgradeDbManager.getInstance().insertUpgradeInfo(upgradeTabInfo);
    }
}
