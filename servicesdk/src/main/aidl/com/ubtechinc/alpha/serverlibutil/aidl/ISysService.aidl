// ISysService.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;

// Declare any non-default types here with import statements

interface ISysService {
    /**
     * @Description 获取机器人序列号
     * @param packageName
     * @return
     * @throws
     */
    String getSid();

    /**
     * @Description 获取机器人MIC版本号
     * @param packageName
     * @return
     * @throws
     */
    String getMICVersion();

    /**
     * @Description 查询机器人某个时点下设置的所有闹钟
     * @param data
     * @return
     * @throws
     */
     AlarmInfo[] queryAllAlarm(String data);

    /**
     * @Description 插入一个闹钟
     * @param packageName
     * @return
     * @throws
     */
    int insertAlarm(in AlarmInfo alarm);

    void startApp(in Uri uri);

    void enterUpgradeMode();

    void exitUpgradeMode();

    /**
     * @Description 获取胸版版本号
     */
    String getChestVersion();

    /**
     * @Description 获取胸版版本号
     */
    String getHeadVersion();

    /**
     * @Description 获取胸版版本号
     */
    String getBatteryVersion();

    /**
     * @Description 获取充电状态
     */
    boolean isPowerCharging();

    /**
     * @Description 获取电量
     */
    int getPowerValue();

}
