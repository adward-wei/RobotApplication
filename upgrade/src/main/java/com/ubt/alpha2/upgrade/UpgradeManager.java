package com.ubt.alpha2.upgrade;

import android.content.Context;
import android.text.TextUtils;

import com.ubt.alpha2.upgrade.action.ActionUpgradeTask;
import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.db.UpgradeDbManager;
import com.ubt.alpha2.upgrade.db.UpgradeTable;
import com.ubt.alpha2.upgrade.factory.DownloadTaskFactory;
import com.ubt.alpha2.upgrade.factory.UpgradeTaskFactory;
import com.ubt.alpha2.upgrade.impl.IDownloadListener;
import com.ubt.alpha2.upgrade.impl.IModuleDownloadTask;
import com.ubt.alpha2.upgrade.impl.IModuleUpgradeTask;
import com.ubt.alpha2.upgrade.impl.IUpgradeChooseListener;
import com.ubt.alpha2.upgrade.impl.IUpgradeListener;
import com.ubt.alpha2.upgrade.impl.IUpgradeVersionResult;
import com.ubt.alpha2.upgrade.task.ObtainUpgradeVersionTask;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.UpgradeOptionsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ubt on 2017/7/20.
 */

public class UpgradeManager {

    ArrayList<IModuleDownloadTask> downloadTaskList;
    HashMap<String,IModuleUpgradeTask> upgradeMap;
    Executor upgradeExecutor;
    Context mContext;

    private UpgradeManager(){
        downloadTaskList = new ArrayList<>();
        upgradeMap = new HashMap<>();
        upgradeExecutor = Executors.newSingleThreadExecutor();
        mContext = UpgradeApplication.getContext();
        initUpgrade();
    }

    public static UpgradeManager getInstance(){
        return UpgradeManagerHolder._instance;
    }

    private void initUpgrade(){
        UpgradeModuleManager upgradeModuleManager = UpgradeModuleManager.getInstance();
        upgradeModuleManager.addModule(UpgradeModel.SELF_MODLE_NAME);
        upgradeModuleManager.addModule(UpgradeModel.MAINSERVICE_MODLE_NAME);
        upgradeModuleManager.addModule(UpgradeModel.ANDROID_OS);
        upgradeModuleManager.addModule(UpgradeModel.MODEL_EMBEDDED_CHEST);
        upgradeModuleManager.addModule(UpgradeModel.MODEL_EMBEDDED_BATTERY);
    }

    public void startTask(){
        ActionUpgradeTask actionUpgradeTask = new ActionUpgradeTask();
        actionUpgradeTask.startUpgrade();
        ObtainUpgradeVersionTask getVersionTask = new ObtainUpgradeVersionTask(new IUpgradeVersionResult() {
            @Override
            public void onResult() {
                prepareDownload();
                startDownload();
            }
        });
        upgradeExecutor.execute(getVersionTask);
    }

    public void prepareDownload() {
        UpgradeModuleManager upgradeModuleManager = UpgradeModuleManager.getInstance();
        LogUtils.d("serverVersionBean: "+upgradeModuleManager.getServerVersionBean());
        if (upgradeModuleManager.getServerVersionBean() == null) {
            LogUtils.d("server has not new versions");
            return;
        }
        LogUtils.d("moduleInfoList: "+upgradeModuleManager.getServerVersionBean().getVersion());
        List<ServerVersionBean.ModuleInfo> moduleInfoList = upgradeModuleManager.getServerVersionBean().getVersion();
        if (moduleInfoList == null || moduleInfoList.isEmpty()) {
            LogUtils.d("server has not new versions");
            return;
        }

        for (ServerVersionBean.ModuleInfo moduleInfo : moduleInfoList) {
            List<UpgradeTable.UpgradeInfo> upgradeInfoList;
            UpgradeDbManager upgradeDbManager = UpgradeDbManager.getInstance();

            if(moduleInfo == null)
                continue;
            // urlMain 为 null时，服务器数据是错误的
            if(TextUtils.isEmpty(moduleInfo.urlMain)) {
                LogUtils.d("version is error");
                continue;
            }
            //是否是全量包下载
            boolean isFullPackage = isFullPackage(moduleInfo);
            if (isFullPackage) {
                upgradeInfoList = upgradeDbManager.queryUpgradeInfo(moduleInfo.MD5Main, moduleInfo.urlMain, true);
            } else {
                upgradeInfoList = upgradeDbManager.queryUpgradeInfo(moduleInfo.md5, moduleInfo.url, false);
            }

            if (upgradeInfoList == null || upgradeInfoList.isEmpty()) {
                //没有下载记录
                addDownloadTask(moduleInfo.module_id);
            } else if (upgradeInfoList.size() > 1) {
                //下载记录多余1条，下载过程出错，全部删除,重新下载
                if (isFullPackage)
                    upgradeDbManager.deleteUpgradeInfo(moduleInfo.urlMain, isFullPackage);
                else
                    upgradeDbManager.deleteUpgradeInfo(moduleInfo.url, isFullPackage);
                addDownloadTask(moduleInfo.module_id);
            } else {
                UpgradeTable.UpgradeInfo upgradeInfo = upgradeInfoList.get(0);
                String localVersion = null ;
                // 是否是自升级和android os升级，这两种升级方式无法监听升级是否完成
                // 判断数据库版本与本地版本号
                if(upgradeInfo.module_name.equals(UpgradeModel.SELF_MODLE_NAME)){
                    localVersion = "v"+ApkUtils.getVersionName(UpgradeApplication.getContext().getPackageName());
                }else if(upgradeInfo.module_name.equals(UpgradeModel.ANDROID_OS)){
                    localVersion ="v"+ApkUtils.getAndroidSystemVersion();
                }
                LogUtils.d("localVersion: "+localVersion+"  toVersion: "+upgradeInfo.toVersion);
                // 已升级，删除数据库记录和本地文件
                if(!TextUtils.isEmpty(localVersion)){
                    if(localVersion.equalsIgnoreCase(upgradeInfo.toVersion)){
                        upgradeDbManager.deleteUpgradeInfoByModuleName(upgradeInfo.module_name);
                        FileManagerUtils.deleteFile(upgradeInfo.localFilepath);
                        ReportDataBean.reportUpgradeOk(upgradeInfo.module_name,true);
                        ReportDataBean.upgradeReport(upgradeInfo,true);
                        return;
                    }
                }

                if(checkLocalFileAvailable(upgradeInfo,upgradeInfo.isPatch ==0)) {
                    addUpgradeTask(upgradeInfo.module_name,upgradeInfo.localFilepath);
                }
                addDownloadTask(moduleInfo.module_id);
            }
        }
    }

    private void addDownloadTask(final String moduleId){
        final IModuleDownloadTask downloadTask = DownloadTaskFactory.getDownloadTask(moduleId);
        if(downloadTask == null)
            return;
        downloadTask.setDownloadListener(new IDownloadListener() {

            @Override
            public void onDownloadSuccess(String moduleName,String filepath,String url) {
                LogUtils.d("onDownloadSuccess: "+moduleName+"   filepath: "+filepath);
                ReportDataBean.reportDownloadOk(moduleName,moduleId,url,true);
                ReportDataBean.downloadReport(moduleId,moduleName,true);
                addUpgradeTask(moduleName,filepath);
                startDownload();
            }

            @Override
            public void onDownloadFailed(String moduleName,String url) {
                LogUtils.d("onDownloadFailed");
                ReportDataBean.getInstance().setDownloadok(false);
                ReportDataBean.downloadReport(moduleId,moduleName,false);
                startDownload();
            }
        });
        if(!downloadTaskList.contains(downloadTask)) {

            downloadTaskList.add(downloadTask);
        }
    }

    /**
     * @author: slive
     * @description: 开始下载，下载结束后会启动升级
     * @return:
     */
    private void startDownload(){
        if(downloadTaskList == null || downloadTaskList.isEmpty()){
            LogUtils.d("download finish");
            if(upgradeMap != null && !upgradeMap.isEmpty())
                showUpgradeTipsToUser();
            return;
        }
        IModuleDownloadTask downloadTask = downloadTaskList.remove(0);
        if(downloadTask != null){
            downloadTask.startDownload();
        }
    }

    private boolean checkLocalFileAvailable(UpgradeTable.UpgradeInfo upgradeInfo,boolean isFullPackage) {
        String filePath = upgradeInfo.localFilepath;
        //file path is null
        if(TextUtils.isEmpty(filePath))
            return false;
        File file = new File(filePath);
        if(!file.exists())
            return false;
        if(isFullPackage) {
            return ApkUtils.checkMd5Value(upgradeInfo.MD5Main,file);
        }else {
            return ApkUtils.checkMd5Value(upgradeInfo.md5,file);
        }
    }

    private void addUpgradeTask(String moduleName,String filepath){
        final IModuleUpgradeTask moduleUpgradeTask = UpgradeTaskFactory.getModuleUpgradeTask(moduleName,filepath);
        if(moduleUpgradeTask != null) {
            upgradeMap.put(moduleName, moduleUpgradeTask);
        }
    }

    private boolean isFullPackage(ServerVersionBean.ModuleInfo moduleInfo) {
        if(TextUtils.isEmpty(moduleInfo.url))
            return false;
        return moduleInfo.urlMain.equals(moduleInfo.urlMain);
    }

    private void startUpgrade(){
        if(upgradeMap == null || upgradeMap.isEmpty()) {
            LogUtils.d("upgrade finish");
            if (UpgradeModuleManager.getInstance().isNeededReboot()) {
                ApkUtils.rebootRobot();
            }
            return;
        }
        LogUtils.d("upgradeMap: "+upgradeMap+"   size: "+upgradeMap.size());
        IModuleUpgradeTask selfModule = getModuleUpgradeTask(UpgradeModel.SELF_MODLE_NAME);
        if(selfModule != null){
            upgradeExecutor.execute(selfModule);
            return;
        }

        IModuleUpgradeTask chestModule = getModuleUpgradeTask(UpgradeModel.MODEL_EMBEDDED_CHEST);
        LogUtils.d("chestModule: "+chestModule);
        if(chestModule != null){
            upgradeExecutor.execute(chestModule);
            return;
        }

        IModuleUpgradeTask batteryModule = getModuleUpgradeTask(UpgradeModel.MODEL_EMBEDDED_BATTERY);
        LogUtils.d("batteryModule: "+batteryModule);
        if(batteryModule != null){
            upgradeExecutor.execute(batteryModule);
            return;
        }

        IModuleUpgradeTask mainServiceModule = getModuleUpgradeTask(UpgradeModel.MAINSERVICE_MODLE_NAME);
        LogUtils.d("mainServiceModule: "+mainServiceModule);
        if(mainServiceModule != null){
            upgradeExecutor.execute(mainServiceModule);
            return;
        }

        IModuleUpgradeTask androidOsModule = getModuleUpgradeTask(UpgradeModel.ANDROID_OS);
        LogUtils.d("androidOsModule: "+batteryModule);
        if(androidOsModule != null){
            upgradeExecutor.execute(androidOsModule);
            return;
        }
    }

    private IModuleUpgradeTask getModuleUpgradeTask(final String moduleName){
        final IModuleUpgradeTask moduleUpgradeTask = upgradeMap.get(moduleName);
        if(moduleUpgradeTask == null){
            upgradeMap.remove(moduleName);
            return null;
        }
        moduleUpgradeTask.setUpgradeListener(new IUpgradeListener() {
            @Override
            public void onUpgradeSuccess() {
                LogUtils.d("upgrade Success: "+moduleUpgradeTask.getClass().getSimpleName());
                upgradeMap.remove(moduleName);
                ReportDataBean.reportUpgradeOk(moduleName,true);
                ReportDataBean.upgradeReport(moduleName,true);
                UpgradeDbManager.getInstance().deleteUpgradeInfoByModuleName(moduleName);
                startUpgrade();
            }

            @Override
            public void onUpgradeFailed() {
                LogUtils.d("upgrade failed: "+moduleUpgradeTask.getClass().getSimpleName());
                ReportDataBean.reportUpgradeOk(moduleName,false);
                ReportDataBean.upgradeReport(moduleName,false);
            }
        });
        return moduleUpgradeTask ;
    }

    /**
     * @author: slive
     * @description: 语音提示用户升级
     * @return:
     */
    private void showUpgradeTipsToUser(){
        UpgradeOptionsUtils.upgradeChooseOptions(mContext.getString(R.string.wake_up_tips),
                new IUpgradeChooseListener() {
                    @Override
                    public void onChooseResult(boolean isUpgrade) {
                        if(isUpgrade){
                            startUpgrade();
                        }
                    }
                });
    }

    public void initUpgradeTest(String moduleName){
        UpgradeModuleManager upgradeModuleManager = UpgradeModuleManager.getInstance();
        upgradeModuleManager.clearModule();
        upgradeModuleManager.addModule(moduleName);
    }

    private static class UpgradeManagerHolder{
        static UpgradeManager _instance = new UpgradeManager();
    }

    /**
     * @author: slive
     * @description: 是否升级任务完成
     * @return:
     */
    public boolean isAllTaskCompleted(){
        //有下载任务
        if(downloadTaskList != null && !downloadTaskList.isEmpty())
            return false;
        //有升级任务
        if(upgradeMap != null && !upgradeMap.isEmpty())
            return false;
        return true;
    }
}
