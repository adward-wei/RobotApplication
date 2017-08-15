package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;
import com.ubt.alpha2.upgrade.install.ApkInstaller;
import com.ubt.alpha2.upgrade.install.IInstallResultListener;
import com.ubt.alpha2.upgrade.install.IInstallingListener;
import com.ubt.alpha2.upgrade.install.InstallDownloadAppManager;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;
import com.ubt.alpha2.upgrade.utils.VersionUtils;
import com.ubtechinc.alpha.serverlibutil.service.BinderFetchServiceUtil;

import java.util.List;

/**
 * Created by ubt on 2017/7/21.
 */

public class MainServiceUpgradeTask extends BaseModuleUpgradeTask{

    public MainServiceUpgradeTask(String filepath){
        super(filepath);
    }

    @Override
    public void startUpgrade(){
        if(PropertyUtils.getSystemProperty(UpgradeApplication.getContext(),Constants.FORBIT_INSTALL).equals("true")){
            LogUtils.d("检测时间到禁止安装");
            return;
        }
        ApkUtils.reflectReleaseProvider();
        BinderFetchServiceUtil.get(mContext).setExitWithBinderLinkToDeath(false);
        killApp();
        baseInstallManager = new InstallDownloadAppManager();
        baseInstallManager.setInstallingListener(new IInstallingListener() {
            @Override
            public void onSuccess(VersionConfigs versionConfigs) {
                //更新配置文件，删除本地下载文件和配置文件
                LogUtils.d(" 更新配置文件，删除本地下载文件和更新配置文件.. .");
                String configFile = FileManagerUtils.readConfigFile(FilePathUtils.NEW_CONFIG_PATH);
                FileManagerUtils.writeProcess(configFile);
                FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
                //卸载需要卸载的apk
//                if(delApkList.size() !=0){
//                    LogUtils.d(" 开始卸载多余apk....");
//                    unInstallDelApk(delApkList);
//                }
                FileManagerUtils.deleteAllFile(FilePathUtils.NEW_PATH);

                LogUtils.d(" 安装成功，杀死应用，重启主服务");
                int j = versionConfigs.apks.size();
                for(int i =0;i<j;i++){
                    if(versionConfigs.apks.get(i).packagename.equals(ServiceUtils.MAINSERVICE_PACKAGE_NAME)){
                        continue;
                    }
                    LogUtils.d("即将杀死的app:"+versionConfigs.apks.get(i).packagename.trim());
                    ApkUtils.killAppByPackageName(mContext,versionConfigs.apks.get(i).packagename.trim());
                    try { //确保kill成功
                        Thread.sleep(Constants.Kill_WAIT_TIME);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                ApkUtils.killAppByPackageName(mContext,ServiceUtils.MAINSERVICE_PACKAGE_NAME);//主服务最后杀死
                try { //确保kill成功
                    Thread.sleep(Constants.Kill_WAIT_TIME);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //启动主服务
                ServiceUtils.startMainService(mContext);
                ReportDataBean.getInstance().setUpgradeOk(true);
                upgradeSuccess();
            }

            @Override
            public void onFailure(List<VersionConfigs.ApkInfo> installApks) {
                FileManagerUtils.deleteAllFile(FilePathUtils.NEW_PATH);
                List<VersionConfigs.ApkInfo> unInstallApkList = installApks;
                //卸载安装的apk
                //静默卸载
                if(unInstallApkList.size() != 0){
                    ApkInstaller.unInstall(unInstallApkList);
                }else {
                    //一个也没安装成功
                    LogUtils.d("一个也没安装成功");
                    FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
                    installAllApkListener.onFailure("");
                }
                ReportDataBean.getInstance().setUpgradeOk(false);
                upgradeSuccess();
            }
        });
        baseInstallManager.startInstall();
    }

    /**
     * 杀死app
     */
    void killApp(){
        VersionConfigs versionConfigs = VersionUtils.getVersionConfigs(FilePathUtils.NEW_CONFIG_PATH);
        int count = versionConfigs.apks.size();
        for(int i =0;i<count;i++){
            String packageName = versionConfigs.apks.get(i).packagename.trim();
            if(packageName.equals(ServiceUtils.MAINSERVICE_PACKAGE_NAME)){
                continue;
            }
            LogUtils.d("即将杀死的app:"+packageName);
            ApkUtils.killAppByPackageName(UpgradeApplication.getContext(),packageName);
            try {
                Thread.sleep(Constants.Kill_WAIT_TIME); //保证杀死成功
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //安装结果监听
    private IInstallResultListener installAllApkListener = new IInstallResultListener() {
        @Override
        public void onSuccess(String result) {
            PropertyUtils.setSystemProperty(mContext, Constants.IS_LYNX_INSTALLING,"false");
            LogUtils.d("安装所有apk成功.....");
            //NetRequest.getInstance().installCallback(context,MyApplication.getInstance().accessToken,Constants.SUCESS,httpsListener);
        }

        @Override
        public void onFailure(String reason) {
            PropertyUtils.setSystemProperty(mContext, Constants.IS_LYNX_INSTALLING,"false");
            LogUtils.d("安装所有APk失败.....");
            /**
             * 杀死所有应用，重启主服务
             */
            LogUtils.d("杀死所有应用，重启主服务.....");
            killApp();
            ApkUtils.killAppByPackageName(mContext,ServiceUtils.MAINSERVICE_PACKAGE_NAME); //主服务最后杀死
            try { //确保kill成功
                Thread.sleep(Constants.Kill_WAIT_TIME);
            }catch (Exception e){
                e.printStackTrace();
            }
            //启动主服务
            ServiceUtils.startMainService(mContext);
            //NetRequest.getInstance().installCallback(context,MyApplication.getInstance().accessToken,Constants.FAIL,httpsListener);
        }
    };
}
