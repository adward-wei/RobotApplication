package com.ubt.alpha2.upgrade.factory;

import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.impl.IModuleUpgradeTask;
import com.ubt.alpha2.upgrade.task.AndroidOsUpgradeTask;
import com.ubt.alpha2.upgrade.task.BatteryModuleUpgradeTaskTask;
import com.ubt.alpha2.upgrade.task.ChestModuleUpgradeTaskTask;
import com.ubt.alpha2.upgrade.task.MainServiceUpgradeTask;
import com.ubt.alpha2.upgrade.task.SelfModuleUpgradeTask;

/**
 * Created by ubt on 2017/7/21.
 */

public class UpgradeTaskFactory {

    public static IModuleUpgradeTask getModuleUpgradeTask(String moduleName,String filepath){
        IModuleUpgradeTask upgradeTask ;
        ReportDataBean.addToUploadTask(moduleName);
        switch (moduleName){
            case UpgradeModel.SELF_MODLE_NAME:
                upgradeTask = new SelfModuleUpgradeTask(filepath);
                break;
            case UpgradeModel.MAINSERVICE_MODLE_NAME:
                upgradeTask = new MainServiceUpgradeTask(filepath);
                break;
            case UpgradeModel.MODEL_EMBEDDED_CHEST:
                upgradeTask = new ChestModuleUpgradeTaskTask(filepath);
                break;
            case UpgradeModel.MODEL_EMBEDDED_BATTERY:
                upgradeTask = new BatteryModuleUpgradeTaskTask(filepath);
                break;
            case UpgradeModel.ANDROID_OS:
                upgradeTask = new AndroidOsUpgradeTask(filepath);
                break;
            default:
                upgradeTask = null;
        }
        return upgradeTask;
    }
}
