package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.install.BaseInstallManager;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;

/**
 * Created by ubt on 2017/7/21.
 */

public class SelfModuleUpgradeTask extends BaseModuleUpgradeTask{

    public SelfModuleUpgradeTask(String filepath){
        super(filepath);
    }

    @Override
    public void startUpgrade(){
        startInstall(filepath);
    }

    private void startInstall(String fileName) {
        LogUtils.d("self 下载成功");
        if (PropertyUtils.getSystemProperty(UpgradeApplication.getContext(), Constants.IS_LYNX_INSTALLING).equals("true")) {
            LogUtils.e("self 系统正在升级");
            return;
        }
        PropertyUtils.setSystemProperty(mContext, Constants.IS_LYNX_INSTALLING, "true");
        baseInstallManager.installSelfApk(fileName);
    }
}
