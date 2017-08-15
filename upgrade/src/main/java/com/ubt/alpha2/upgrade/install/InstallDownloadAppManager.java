package com.ubt.alpha2.upgrade.install;

import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.ServiceUtils;

import static com.tencent.qalsdk.service.QalService.context;

/**
 * @author: slive
 * @description: 安装下载 app
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class InstallDownloadAppManager extends BaseInstallManager {

    public InstallDownloadAppManager() {
        super();
        setInstallResultListener(installDownloadApkListener);
    }

    private IInstallResultListener installDownloadApkListener = new IInstallResultListener() {
        @Override
        public void onSuccess(String result) {
            //更新配置文件，删除本地下载文件和配置文件
            LogUtils.d(" 更新配置文件，删除本地下载文件和更新配置文件.. .");
            String configFile = FileManagerUtils.readConfigFile(FilePathUtils.NEW_CONFIG_PATH);
            FileManagerUtils.writeProcess(configFile);
            LogUtils.d("安装备份apk成功，删除备份apk,杀死运行的应用");
            FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);
            //卸载需要卸载的apk
            if(delApkList.size() !=0){
                LogUtils.d(" 开始卸载多余apk....");
                ApkInstaller.unInstallDelApk(delApkList);
            }
            int count = 0;
            if(installApkLists != null)
                count = installApkLists.size();

            for(int i =0;i<count;i++){
                LogUtils.d("即将杀死的app:"+installApkLists.get(i).packagename.trim());
                ApkUtils.killAppByPackageName(context,installApkLists.get(i).packagename.trim());
                try {
                    Thread.sleep(Constants.Kill_WAIT_TIME); //保证杀死成功
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            ApkUtils.killAppByPackageName(context, ServiceUtils.MAINSERVICE_PACKAGE_NAME); //主服务最后杀死
            try { //确保kill成功
                Thread.sleep(Constants.Kill_WAIT_TIME);
            }catch (Exception e){
            }
            //启动主服务
            ServiceUtils.startMainService(context);
        }

        @Override
        public void onFailure(String reason) {
            LogUtils.d("安装备份apk失败");//这种情况不用考虑，（要安装的apk已经被卸载了，安装不会出现这种情况）
        }
    };
}
