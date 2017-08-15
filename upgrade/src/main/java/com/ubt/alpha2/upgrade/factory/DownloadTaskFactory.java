package com.ubt.alpha2.upgrade.factory;

import android.text.TextUtils;

import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.impl.IModuleDownloadTask;
import com.ubt.alpha2.upgrade.task.AndroidOsDownloadTask;
import com.ubt.alpha2.upgrade.task.BatteryModelDownloadTask;
import com.ubt.alpha2.upgrade.task.ChestModelDownloadTask;
import com.ubt.alpha2.upgrade.task.MainServiceDownloadTask;
import com.ubt.alpha2.upgrade.task.SelfModuleDownloadTask;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;

/**
 * @author: slive
 * @description: 生成download task的简单工厂类
 * @create: 2017/7/20
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class DownloadTaskFactory {

    public static IModuleDownloadTask getDownloadTask(String moduleId){
        String moduleName = UpgradeModuleManager.getInstance().getModuleName(moduleId);
        LogUtils.d("moduleId: "+moduleId+"  moduleName: "+moduleName);
        if(TextUtils.isEmpty(moduleName))
            return null;
        ReportDataBean.addToDownloadTask(moduleId,moduleName);
        IModuleDownloadTask downloadTask = null;
        switch (moduleName){
            case UpgradeModel.SELF_MODLE_NAME:
                downloadTask = new SelfModuleDownloadTask();
                break;
            case UpgradeModel.MAINSERVICE_MODLE_NAME:
                downloadTask = new MainServiceDownloadTask();
                break;
            case UpgradeModel.MODEL_EMBEDDED_CHEST:
                downloadTask = new ChestModelDownloadTask();
                break;
            case UpgradeModel.MODEL_EMBEDDED_BATTERY:
                downloadTask = new BatteryModelDownloadTask();
                break;
            case UpgradeModel.ANDROID_OS:
                downloadTask = new AndroidOsDownloadTask();
                break;
            default:
                downloadTask = null;
        }
        return downloadTask;
    }
}
