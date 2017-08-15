package com.ubt.alpha2.upgrade.bean;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ubt.alpha2.statistics.StatisticsWrapper;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.db.UpgradeTable;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;

import java.util.HashMap;

/**
 * @author: slive
 * @description:  第三方统计 升级状态
 * @create: 2017/7/19
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ReportDataBean {
    public static final String REPORT_UPGRADE_KEY = "upgrade";
    public static final String MODULE_NAME = "module";
    public static final String ACCESS_TOKEN  = "accessToken";
    public static final String MODEL_ID = "modelId";
    public static final String FROM_VERSION = "fromVersion";
    public static final String TO_VERSION = "toVersion";
    public static final String DOWNLOAD_OK = "downloadOk";
    public static final String UPGRADE_OK = "upgradeOk";
    public static final String DEVICE_MODEL = "deviceModel";
    public static final String DEVICE_SERIAL_NO ="serialNumber";
    public static final String ADD_DOWNLOAD_TASK="addToDownloadTask";
    public static final String ADD_UPGRADE_TASK = "addToUpgradeTask";


    private HashMap<String,String>reportMaps;
    private static ReportDataBean reportDataBean;

    public static ReportDataBean getInstance(){
        if(reportDataBean == null){
            synchronized (ReportDataBean.class){
                if(reportDataBean == null){
                    reportDataBean = new ReportDataBean();
                }
            }
        }
        return reportDataBean;
    }

    private ReportDataBean(){
        reportMaps = new HashMap<>();
    }

    public void setDeviceSerialNumber(String serialNumber){
        reportMaps.put(DEVICE_SERIAL_NO,serialNumber);
    }

    /**
     * @author: slive
     * @description: 设置设备名称
     * @return:
     */
    public void setDeviceModel(String deviceModel){
        reportMaps.put(DEVICE_MODEL,deviceModel);
    }

    /**
     * @author: slive
     * @description:  设置升级模块信息
     * @return:
     */
    public void setModuleName(String moduleName){
        reportMaps.put(MODULE_NAME,moduleName);
    }

    /**
     * @author: slive
     * @description: 设置module Id
     * @return:
     */
    public void setModuleId(String moduleId){
        reportMaps.put(MODEL_ID,moduleId);
    }

    /**
     * @author: slive
     * @description: 设置获取AccessToken 成功还是失败
     * @return:
     */

    public void setAccessToken(String accessToken){
        reportMaps.put(ACCESS_TOKEN,accessToken);
    }

    /**
     * @author: slive
     * @description: 设置升级模块的local version
     * @return:
     */
    public void setFromVersion(String fromVersion){
        reportMaps.put(FROM_VERSION,fromVersion);
    }

    /**
     * @author: slive
     * @description: 设置升级模块的目标版本
     * @return:
     */
    public void setToVersion(String toVersion){
        reportMaps.put(TO_VERSION,toVersion);
    }

    /**
     * @author: slive
     * @description: 现在是否成功
     * @return:
     */
    public void setDownloadok(boolean isDownloadOk){
        reportMaps.put(DOWNLOAD_OK,String.valueOf(isDownloadOk));
    }

    /**
     * @author: slive
     * @description: 升级是否成功
     * @return:
     */
    public void setUpgradeOk(boolean isUpgradeOk){
        reportMaps.put(UPGRADE_OK,String.valueOf(isUpgradeOk));
    }

    public void setAddDownloadTask(boolean isAddToDownloadTask){
        reportMaps.put(ADD_DOWNLOAD_TASK,String.valueOf(isAddToDownloadTask));
    }

    /**
     * @author: slive
     * @description:  数据上报
     * @return:
     */
    public void report(){
        if(reportMaps != null && !reportMaps.isEmpty()){
            LogUtils.d("report111");
            StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),REPORT_UPGRADE_KEY,reportMaps);
            LogUtils.d("report222");
            String moduleName = reportMaps.get(MODULE_NAME);
            if(!TextUtils.isEmpty(moduleName)){
                saveFeedbackInfo(moduleName);
            }
        }
        reportDataBean = null;
    }

    private void saveFeedbackInfo(String moduleName){
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.module_id = reportMaps.get(MODEL_ID);
        feedbackInfo.module_name = moduleName;
        feedbackInfo.from_version = reportMaps.get(FROM_VERSION);
        feedbackInfo.to_version = reportMaps.get(TO_VERSION);
        feedbackInfo.access_token = reportMaps.get(ACCESS_TOKEN);
        feedbackInfo.robot_type = reportMaps.get(DEVICE_MODEL);
        feedbackInfo.robot_id = reportMaps.get(DEVICE_SERIAL_NO);
        UpgradeFeedbackConfig.getInstance().saveFeedbackInfo(moduleName,feedbackInfo);
    }

    /**
     * @author: slive
     * @description: 统计添加到下载队列的module
     * @return:
     */
    public static void addToDownloadTask(String moduleId,String moduleName){
        HashMap<String,String> params = new HashMap<>();
        params.put("moduleId",moduleId);
        params.put("moduleName",moduleName);
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),ADD_DOWNLOAD_TASK,
                params);
    }

    /**
     * @author: slive
     * @description: 统计添加到升级队列的module
     * @return:
     */
    public static void addToUploadTask(String moduleName){
        HashMap<String,String> params = new HashMap<>();
        params.put("moduleName",moduleName);
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),ADD_UPGRADE_TASK,
                params);
    }

    /**
     * @author: slive
     * @description: 统计下载结果
     * @return:
     */
    public static void reportDownloadOk(String moduleName,String moduleId,String url,boolean downloadOk){
        HashMap<String,String> params = new HashMap<>();
        params.put("moduleName",moduleName);
        params.put("moduleId",moduleId);
        params.put("downloadOk",String.valueOf(downloadOk));
        params.put("url",url);
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),DOWNLOAD_OK,
                params);
    }

    /**
     * @author: slive
     * @description: 统计升级结果
     * @return:
     */
    public static void reportUpgradeOk(String moduleName,boolean upgradeOk){
        HashMap<String,String> params = new HashMap<>();
        params.put("moduleName",moduleName);
        params.put("upgradeOk",String.valueOf(upgradeOk));
        StatisticsWrapper.getInstance().onEvent(UpgradeApplication.getContext(),UPGRADE_OK,
                params);
    }

    /**
     * @author: slive
     * @description: 上报下载 结果
     * @return:
     */
    public static void downloadReport(String moduleId,String moduleName,boolean isDownloadOk){
        ServerVersionBean.ModuleInfo moduleInfo = UpgradeModuleManager.getInstance().getUpgradeModuleInfo(moduleName);
        if(moduleInfo == null)
            return;
        ReportDataBean.getInstance().setAccessToken(UpgradeModuleManager.getInstance().getAccessToken());
        ReportDataBean.getInstance().setModuleId(moduleId);
        ReportDataBean.getInstance().setModuleName(moduleName);
        ReportDataBean.getInstance().setFromVersion(moduleInfo.fromVersion);
        ReportDataBean.getInstance().setToVersion(moduleInfo.toVersion);
        ReportDataBean.getInstance().setDownloadok(isDownloadOk);
        ReportDataBean.getInstance().setDeviceModel(ApkUtils.getProductModel());
        ReportDataBean.getInstance().setDeviceSerialNumber(ApkUtils.getRobotId());
        ReportDataBean.getInstance().report();
    }

    /**
     * @author: slive
     * @description: 统计升级结果
     * @return:
     */
    public static void upgradeReport(String moduleName,boolean isUpgradeOk){
        ServerVersionBean.ModuleInfo moduleInfo = UpgradeModuleManager.getInstance().getUpgradeModuleInfo(moduleName);
        if(moduleInfo == null)
            return;
        ReportDataBean.getInstance().setAccessToken(UpgradeModuleManager.getInstance().getAccessToken());
        ReportDataBean.getInstance().setModuleId(moduleInfo.module_id);
        ReportDataBean.getInstance().setModuleName(moduleName);
        ReportDataBean.getInstance().setFromVersion(moduleInfo.fromVersion);
        ReportDataBean.getInstance().setToVersion(moduleInfo.toVersion);
        ReportDataBean.getInstance().setDownloadok(true);
        ReportDataBean.getInstance().setUpgradeOk(isUpgradeOk);
        ReportDataBean.getInstance().setDeviceModel(ApkUtils.getProductModel());
        ReportDataBean.getInstance().setDeviceSerialNumber(ApkUtils.getRobotId());
        ReportDataBean.getInstance().report();
    }

    public static void upgradeReport(UpgradeTable.UpgradeInfo upgradeInfo, boolean isUpgradeOk){
        if(upgradeInfo == null)
            return;
        ReportDataBean.getInstance().setAccessToken(UpgradeModuleManager.getInstance().getAccessToken());
        ReportDataBean.getInstance().setModuleId(upgradeInfo.module_id);
        ReportDataBean.getInstance().setModuleName(upgradeInfo.module_name);
        ReportDataBean.getInstance().setFromVersion(upgradeInfo.fromVersion);
        ReportDataBean.getInstance().setToVersion(upgradeInfo.toVersion);
        ReportDataBean.getInstance().setDownloadok(true);
        ReportDataBean.getInstance().setUpgradeOk(isUpgradeOk);
        ReportDataBean.getInstance().setDeviceModel(ApkUtils.getProductModel());
        ReportDataBean.getInstance().setDeviceSerialNumber(ApkUtils.getRobotId());
        ReportDataBean.getInstance().report();
    }

}
