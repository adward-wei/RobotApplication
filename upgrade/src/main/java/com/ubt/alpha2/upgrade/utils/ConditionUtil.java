package com.ubt.alpha2.upgrade.utils;

import android.os.Environment;
import android.os.StatFs;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.PowerInfo;
import com.ubtechinc.alpha.sdk.sys.SysApi;


import java.io.File;

/**
 * 升级条件判断类
 *
 * @author wangzhengtian
 * @Date 2017-04-05
 */

public class ConditionUtil {
    private static final String TAG = "ConditionUtil";
    /** 可以进行下载的电量阈值 **/
    public static final int POWER_DOWNLOAD_THRESHOLD = 20;
    /** 可以进行升级的电量阈值 **/
    public static final int POWER_UPGRADE_THRESHOLD = 50;

    /** 可以进行下载的存储空间阈值 **/
    public static final int STORAGE_DOWNLOAD_THRESHOLD = 800;

    public static boolean checkCondition(boolean isCheckCharge, int powerThreshold, int storageThreshold) {
        boolean isMeetCondition = true;

        //LogUtils.d("powerState: "+SysApi.get().isPowerCharging()+"  powerValue: "+SysApi.get().getPowerValue());
        //if(isCheckCharge && !SysApi.get().isPowerCharging()) {
//        if(isCheckCharge) {
//            LogUtils.d("PowerInfo.getInstance().isCharging()=" + SysApi.get().isPowerCharging());
//            isMeetCondition = false;
//            return isMeetCondition;
//        }

//        if(SysApi.get().getPowerValue() < powerThreshold) {
//            LogUtils.d("DoEventService.getPowerValue()=" + SysApi.get().getPowerValue());
//            isMeetCondition = false;
//            return isMeetCondition;
//        }

        long storage = getAvailableExternalMemorySize();
        storage = storage/(1024*1024);
        if(storage < storageThreshold) {
            LogUtils.d("storage=" + storage);
            isMeetCondition = false;
        }

        return isMeetCondition;
    }

    private static long getAvailableExternalMemorySize(){
        if(externalMemoryAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            LogUtils.d("available= blockSize=" + blockSize);
            LogUtils.d("available= availableBlocks=" + availableBlocks);
            return availableBlocks*blockSize;
        } else{
            return -1;
        }
    }

    private static boolean externalMemoryAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查主服务升级的电量和充电状态
     * @return
     */
    public static boolean checkPowerForMainserviceUpgrade(){
        SysApi sysApi = SysApi.get().initializ(UpgradeApplication.getContext());
        int powerValue = sysApi.getPowerValue();
        boolean isPowerCharging = sysApi.isPowerCharging();
        if (powerValue < PowerInfo.MIN_UPDATE_POWER_AND_CHARGE ||
                (powerValue < PowerInfo.MIN_UPDATE_POWER
                        &&!isPowerCharging)) {
            LogUtils.d("! power not enough before update" +powerValue
                    + " powerState " +isPowerCharging);
            return false;
        }
        return true;
    }
}
