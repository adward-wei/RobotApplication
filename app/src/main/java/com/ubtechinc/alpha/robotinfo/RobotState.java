package com.ubtechinc.alpha.robotinfo;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.utils.SharedPreferenceUtil;

/**
 * @desc : 机器人状态管理
 * @author: wzt
 * @time : 2017/5/18
 * @modifier:
 * @modify_time:
 */

public class RobotState {
    public static final String ROBOT_SERIAL_ID_KEY = "robot_serialId_key";
    private static volatile RobotState sRobotState;

    /** 是否处于省电模式 **/
    private volatile boolean isInPowerSave = false;
    /** 是否在充电状态 **/
    private volatile boolean isInsertDC = false;
    /** 边充边玩是否打开 **/
    private volatile boolean isChargePlayingOpen = true;
    /** 是否低电量 **/
    private volatile boolean isLowBattery = false;
    /** 是否已经进行低电量提示 **/
    private volatile  boolean hasLowBatteryTTS = false;
    /** 机器人序列号 **/
    private volatile String sid;
    /** 电池电量 **/
    private volatile int powerValue= -1;

    /** 升级模式*/
    private volatile boolean upgradeModeOpened = false;

    private RobotState(){
        init();
    }

    public static RobotState get() {
        if(sRobotState == null) {
            synchronized (RobotState.class) {
                if(sRobotState == null) {
                    sRobotState = new RobotState();
                }
            }
        }
        return sRobotState;
    }

    private void init() {
        sid = SharedPreferenceUtil.readString(AlphaApplication.getContext(), ROBOT_SERIAL_ID_KEY,"");
    }

    public boolean isInPowerSave() {
        return isInPowerSave;
    }

    public void setInPowerSave(boolean inPowerSave) {
        isInPowerSave = inPowerSave;
    }

    public boolean isInsertDC() {
        return isInsertDC;
    }

    public void setInsertDC(boolean insertDC) {
        isInsertDC = insertDC;
    }

    public boolean isChargePlayingOpen() {
        return isChargePlayingOpen;
    }

    public void setChargePlayingOpen(boolean chargePlayingOpen) {
        isChargePlayingOpen = chargePlayingOpen;
    }

    public boolean isLowBattery() {
        return isLowBattery;
    }

    public void setLowBattery(boolean lowBattery) {
        isLowBattery = lowBattery;
    }

    public boolean isHasLowBatteryTTS() {
        return hasLowBatteryTTS;
    }

    public void setHasLowBatteryTTS(boolean hasLowBatteryTTS) {
        this.hasLowBatteryTTS = hasLowBatteryTTS;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        if (sid != null && !sid.equals(this.sid)) {
            this.sid = sid;
            SharedPreferenceUtil.save(AlphaApplication.getContext(),ROBOT_SERIAL_ID_KEY,sid);
        }
    }

    public int getPowerValue() {
        return powerValue;
    }

    public void setPowerValue(int powerValue) {
        this.powerValue = powerValue;
    }

    public boolean isUpgradeModeOpened() {
        return upgradeModeOpened;
    }

    public void setUpgradeModeOpened(boolean upgradeModeOpened) {
        this.upgradeModeOpened = upgradeModeOpened;
    }
}
