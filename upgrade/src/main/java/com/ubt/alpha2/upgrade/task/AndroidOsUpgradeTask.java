package com.ubt.alpha2.upgrade.task;

import android.os.RecoverySystem;

import com.ubt.alpha2.upgrade.R;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.utils.BroadcastUtil;
import com.ubt.alpha2.upgrade.utils.ConditionUtil;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.MD5FileUtil;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by ubt on 2017/7/21.
 */

public class AndroidOsUpgradeTask extends BaseModuleUpgradeTask{

    boolean isPatchUpgrade;

    public AndroidOsUpgradeTask(String filepath){
        super(filepath);
    }

    public void startUpgrade(){
        boolean result = false;
        File upgradeFile = new File(filepath);
        if (upgradeFile.exists()) {
            String md5 = MD5FileUtil.getFileMD5String(upgradeFile);
            md5 = md5.toLowerCase();
            LogUtils.d("Android upgrade file is exist.");
            /** 正在充电，电量大于50%，才能升级 **/
            if(ConditionUtil.checkCondition(true, ConditionUtil.POWER_UPGRADE_THRESHOLD, -1)) {
                String hint = mContext.getString(R.string.upgrade_android_hint);
                BroadcastUtil.sendTTSContent(mContext, hint);
                try {
                    /**
                     * packageFile--升级文件
                     * listener--进度监督器
                     * deviceCertsZipFile--签名文件，如果为空，则使用系统默认的签名
                     */
                    LogUtils.d("startUpgrade RecoverySystem.verifyPackage:" + upgradeFile.getAbsolutePath());
                    RecoverySystem.verifyPackage(upgradeFile, upgradeListener, null);

                    LogUtils.d("startUpgrade RecoverySystem.installPackage");
                    ReportDataBean.getInstance().setUpgradeOk(true);
                    ReportDataBean.getInstance().report();
                    RecoverySystem.installPackage(UpgradeApplication.getContext(), upgradeFile);
                    LogUtils.d("startUpgrade finish");
                    result = true;
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    result = false;
                    e.printStackTrace();
                }
            } else {
                result = false;
                String hint = mContext.getString(R.string.upgrade_condition_hint);
                BroadcastUtil.sendTTSContent(mContext, hint);
            }
        }
        LogUtils.d("startUpgrade result:"+result);
    }

    private RecoverySystem.ProgressListener upgradeListener = new RecoverySystem.ProgressListener() {
        @Override
        public void onProgress(int progress) {

        }
    };
}
