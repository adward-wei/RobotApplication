package com.ubt.alpha2.upgrade.install;

import android.content.Context;
import android.util.Log;

import com.ubt.alpha2.upgrade.BackupApkManager;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.InstallTypeInfo;
import com.ubt.alpha2.upgrade.bean.PowerInfo;
import com.ubt.alpha2.upgrade.bean.SystemVersionInfo;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;
import com.ubt.alpha2.upgrade.utils.Alpha2Property;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.ConditionUtil;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;
import com.ubtech.utilcode.utils.AppUtils;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.alpha.serverlibutil.service.BinderFetchServiceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author: slive
 * @description: 安装管理
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class BaseInstallManager implements AppSilence.SilenceListener{

    protected int currentPos = 0; //安装apk 的位置
    protected int totalSize = 0; //安装apk的总大小
    //记录已经安装的apk
    protected List<VersionConfigs.ApkInfo> installedApps;
    //需要安装的备份apk
    protected List<VersionConfigs.ApkInfo> installBackupApks;

    protected List<VersionConfigs.ApkInfo> installApkLists;//需安装的apk

    protected IInstallResultListener installResultListener;

    protected IInstallingListener installingListener;

    //备份apk
    protected VersionConfigs versionConfigs;
    //0 代表升级apk  1 代表安装备份apk 2 代表安装自己
    @InstallTypeInfo.Type
    protected int type = InstallTypeInfo.INSTALL_COMMON_APK;

    protected String path;
    protected ArrayList<String> delApkList ;

    protected int backupApkSize =0;
    protected int backupApkPos =0;

    Context mc;

    public BaseInstallManager(){
        initValues();
    }

    private void initValues(){
        mc = UpgradeApplication.getContext();
        installedApps = new ArrayList<>(5);
        delApkList = new ArrayList<>();
    }

    public void setInstallResultListener(IInstallResultListener installResultListener){
        this.installResultListener = installResultListener;
    }

    public void setInstallingListener(IInstallingListener installingListener){
        this.installingListener = installingListener;
    }

    public void installSelfApk(String filePath){
        boolean is2Mic = false;
        AppSilence install = new AppSilence();
        install.onSilenceSync(filePath, this, true, is2Mic);
    }

    //开始安装
    public void startInstall(){
        //检测电量是否充足或者是否在充电
        if (!ConditionUtil.checkPowerForMainserviceUpgrade()) {
            FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
            LogUtils.d("! power not enough before update:" + SysApi.get().initializ(mc).getPowerValue()
                    + " powerState :" + SysApi.get().initializ(mc).isPowerCharging());
            installFailed();
            return;
        }

        if(Alpha2Property.get(mc,Constants.IS_LYNX_INSTALLING).equals("true") ){
            LogUtils.d("系统正在升级 ");
            return;
        }
        PropertyUtils.setSystemProperty(mc, Constants.IS_LYNX_INSTALLING,"true");
        PropertyUtils.setSystemProperty(mc,Constants.IS_MAINSERVICE_UPDATE,"true");
        //备份apk
        versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
        BackupApkManager backupApkManager = new BackupApkManager(mc);
        int count =versionConfigs.apks.size();
        boolean copyOk = true;
        for(int i =0;i<count;i++){
            if(!backupApkManager.backupApk(versionConfigs.apks.get(i).packagename.trim())){
                copyOk = false;
                break;
            }
        }

        if(copyOk){ //备份完成 进入安装
            LogUtils.d(" 备份成功 进入安装....");
            VersionConfigs downVersionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
            //找出需要删除的apk
            delApkList = getDelApkList(downVersionConfigs,versionConfigs);
            startInstallAll(downVersionConfigs,true);
        }else {
            LogUtils.d(" 备份失败 删除备份文件....");
            FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
            if(installResultListener != null)
                installResultListener.onFailure("");
        }

    }

    public void startInstall(boolean isBuiltIn) {
        LogUtils.d("totalSize: "+totalSize);
        for(int i=currentPos;i<totalSize;i++){
            currentPos = i;
            LogUtils.d("fileType: "+versionConfigs.apks.get(i).fileType.trim());
            if(versionConfigs.apks.get(i).fileType.trim().equals("patchPackage")){
                LogUtils.d(" 安装合成的apk");
                boolean is2Mic = false;
                AppSilence install = new AppSilence();
                install.onSilenceSync(FilePathUtils.NEW_PATH+"/"+versionConfigs.apks.get(i).packagename.trim()+".apk", this, isBuiltIn, is2Mic);
                break;

            }else if(versionConfigs.apks.get(i).fileType.equals("apkPackage")){
                LogUtils.d(" full install apk");
                boolean is2Mic = false;
                AppSilence install = new AppSilence();
                install.onSilenceSync(FilePathUtils.NEW_PATH+"/"+versionConfigs.apks.get(i).filename.trim(), this, isBuiltIn, is2Mic);
                break;
            }
            if((currentPos+1)==totalSize){
                //安装完成
                if(installingListener != null)
                    installingListener.onSuccess(versionConfigs);
            }
        }
    }

    @Override
    public void onSilenceResult(int code) {
        LogUtils.d("code: "+code+"  type: "+type+"  currentPos: "+currentPos+"  totalSize: "+totalSize);
        if(type == InstallTypeInfo.INSTALL_COMMON_APK) {
            if (code == 1) {
                //success
                LogUtils.d("单个安装成功");
                installedApps.add(versionConfigs.apks.get(currentPos));
                if ((currentPos + 1) == totalSize) {
                    //安装完成
                    if(installingListener != null)
                        installingListener.onSuccess(versionConfigs);
                } else {
                    currentPos++;
                    startInstall(true);
                }
            } else if (code == 0) {//failed
                LogUtils.d("单个安装失败");
                if(installingListener != null)
                    installingListener.onFailure(installedApps);
            } else if (code == 2) {//insufficient storage
                LogUtils.d("单个安装失败  内存不够");
                if(installingListener != null)
                    installingListener.onFailure(installedApps);
            }
        }else if(type ==InstallTypeInfo.INSTALL_BACKUP_APK){
            if(code == 1){
                if(backupApkPos+1 ==backupApkSize ){
                    LogUtils.d("backupAPK安装完所有apk");
                    //安装完所有apk，杀死运行的应用
                    installSucessful();
                }else {
                    LogUtils.d("backupAPK继续安装apk");
                    //继续安装apk
                    backupApkPos++;
                    installBackupApk(installBackupApks);
                }
            }else if(code == 0){
                //备份的apk 安装失败不成功，不存在
                LogUtils.d("backupAPK单个安装失败");
                installFailed();
            }else if(code == 2){
                //备份的apk，并且需要的安装的apk已经被卸载， 安装不成功这种情况不存在
                LogUtils.d("backupAPK 单个安装失败 内存不够" );
                installFailed();
            }
        }else if(type ==InstallTypeInfo.INSTALL_SELF){
            if(code == 1){
                LogUtils.d("安装自己成功"); //安装自己成功后不再回调
                installSucessful();
            }else if(code == 2){
                LogUtils.d("安装自己失败");
                installFailed();
            }else if(code == 3){
                LogUtils.d("安装自己失败 内存不够");
                installFailed();
            }
        }
    }

    public void startInstallBackupApk(List<VersionConfigs.ApkInfo> list){
        this.installBackupApks = list;
        this.backupApkSize = list.size();
        if(backupApkSize == 0){
            return;
        }
        type = InstallTypeInfo.INSTALL_BACKUP_APK;
        BinderFetchServiceUtil.get(UpgradeApplication.getContext()).unlinkToDeath();
        installBackupApk(installBackupApks);
    }

    //安装备份文件
    public void installBackupApk(List<VersionConfigs.ApkInfo> list){
        ApkUtils.reflectReleaseProvider();
        File file  =  new File(FilePathUtils.OLD_PATH+"/"+list.get(backupApkPos).packagename.trim()+".apk");
        LogUtils.d("filepath: "+file.getAbsolutePath()+" file.exists: "+file.exists());
        if(!file.exists()){ //文件不存在 则需要查询下一个备份文件
            LogUtils.d("文件不存在 则需要查询下一个备份文件");
            if(backupApkPos+1 == backupApkSize ){
                LogUtils.d("文件不存在 backupAPK安装完所有apk");
            }else {
                LogUtils.d("文件不存在 backupAPK继续安装apk");
                //继续安装apk
                backupApkPos++;
                installBackupApk(installBackupApks);
            }
        }else {
            boolean is2Mic = false;
            AppSilence install = new AppSilence();
            install.onSilenceSync(FilePathUtils.OLD_PATH+"/"+list.get(backupApkPos).packagename.trim()+".apk", this, true, is2Mic);
        }
    }

    /**
     * 同时安装几个apk
     * @param versionConfigs
     * @param isBuiltIn
     */

    public void startInstallAll(VersionConfigs versionConfigs, boolean isBuiltIn) {
        this.type = InstallTypeInfo.INSTALL_COMMON_APK;
        //removeInstalledApk(versionConfigs);
        totalSize = versionConfigs.apks.size();
        //把主服务放在最后安装的位置
        for(int p=0;p<totalSize;p++){
            if(p+1 ==totalSize){
                break;
            }
            if(versionConfigs.apks.get(p).packagename.trim().equals(ServiceUtils.MAINSERVICE_PACKAGE_NAME)){
                VersionConfigs.ApkInfo apkInfo = versionConfigs.apks.get(p);
                versionConfigs.apks.remove(p);
                versionConfigs.apks.add(apkInfo);
                break;
            }
        }
        startInstall(isBuiltIn);
    }

    //找出需要删除的apk
    ArrayList<String> getDelApkList(VersionConfigs downVersionConfig,VersionConfigs oldVersionConfig){
        HashMap<String,String> configs = new HashMap<>();
        int size = downVersionConfig.apks.size();
        for(int i = 0;i<size;i++){
            configs.put(downVersionConfig.apks.get(i).packagename.trim(),"1");
        }
        int count = oldVersionConfig.apks.size();
        //需要删除的列表 delPackageList
        ArrayList<String>   delPackageList = new ArrayList<>();

        for(int i=0;i<count;i++){
            String value =configs.get(oldVersionConfig.apks.get(i).packagename.trim());
            if(value ==null){
                LogUtils.d("有 需要删除的apk 包名为："+oldVersionConfig.apks.get(i).packagename);
                delPackageList.add(oldVersionConfig.apks.get(i).packagename.trim());
            }
        }
        return delPackageList;
    }

    private void installSucessful(){
        if(installResultListener != null)
            installResultListener.onSuccess("");
    }

    private void installFailed(){
        if(installResultListener != null)
            installResultListener.onFailure("");
    }

    /**
     * 对比本地版本号
     * @param versionConfigs
     */
    private void removeInstalledApk(VersionConfigs versionConfigs){
        if(versionConfigs == null)
            return;
        Iterator<VersionConfigs.ApkInfo> iterator = versionConfigs.apks.iterator();
        while (iterator.hasNext()){
            VersionConfigs.ApkInfo apkInfo = iterator.next();
            String packageName = apkInfo.packagename;
            String versionCode = apkInfo.versionCode;
            String localVersionCode = ApkUtils.getVersion(packageName,mc);
            LogUtils.d("packageName: "+packageName+"  versionCode: "+versionCode+"  localVersionCode:"+localVersionCode);
            if(localVersionCode.compareTo(versionCode)< 0)
                continue;
            iterator.remove();
        }
    }
}
