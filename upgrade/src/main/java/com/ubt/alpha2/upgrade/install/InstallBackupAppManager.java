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
 * @description: 安装备份app
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class InstallBackupAppManager extends BaseInstallManager {

    public InstallBackupAppManager() {
        super();
        setInstallResultListener(installBackupApkListener);
    }

    private IInstallResultListener installBackupApkListener = new IInstallResultListener() {
        @Override
        public void onSuccess(String result) {
            LogUtils.d("安装备份apk成功，删除备份apk,杀死运行的应用");
            FileManagerUtils.deleteAllFile(FilePathUtils.OLD_PATH);

            int count = installApkLists.size();
            for(int i =0;i<count;i++){
                LogUtils.d("即将杀死的app:"+installApkLists.get(i).packagename.trim());
                ApkUtils.killAppByPackageName(context,installApkLists.get(i).packagename.trim());
                try {
                    Thread.sleep(Constants.Kill_WAIT_TIME); //保证杀死成功
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            ApkUtils.killAppByPackageName(context,ServiceUtils.MAINSERVICE_PACKAGE_NAME); //主服务最后杀死
            try { //确保kill成功
                Thread.sleep(Constants.Kill_WAIT_TIME);
            }catch (Exception e){
            }
            //启动主服务
            ServiceUtils.startMainService(context);
            //启动ota升级apk
            Boolean b = ApkUtils.isAvilible(context,ServiceUtils.OTA_SERVICE_PACKAGE_NAME);
            if(b){
                LogUtils.d("已经安装了OTA升级 打开一次");
                ServiceUtils.startOTAService(context);
            }
        }

        @Override
        public void onFailure(String reason) {
            LogUtils.d("安装备份apk失败");//这种情况不用考虑，（要安装的apk已经被卸载了，安装不会出现这种情况）
        }
    };

}
