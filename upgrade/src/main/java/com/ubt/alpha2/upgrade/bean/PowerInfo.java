package com.ubt.alpha2.upgrade.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: slive
 * @description: 电量信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class PowerInfo {
    //升级的最小电量  不充电情况
    public static final int MIN_UPDATE_POWER = 50;
    //升级的最小电量  充电情况
    public  static final int MIN_UPDATE_POWER_AND_CHARGE = 20;

    /** 未知状态 **/
    public static final int POWER_STATE_UNKNOW = 0;
    /** 未充电 **/
    public static final int POWER_STATE_UNCHARGED = 1;
    /** 正在充电 **/
    public static final int POWER_STATE_CHARGING= 2;

    @IntDef({POWER_STATE_UNKNOW,POWER_STATE_UNCHARGED,POWER_STATE_CHARGING})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PowerState{}

    private int powerValue;
    @PowerInfo.PowerState
    private int powerState = PowerInfo.POWER_STATE_UNKNOW;

    private PowerInfo(){

    }

    public static PowerInfo getInstance(){
        return PowerInfoHolder._instance;
    }

    public static class PowerInfoHolder{
        private static PowerInfo _instance = new PowerInfo();
    }

    public int getPowerValue() {
        return powerValue;
    }

    public void setPowerValue(int powerValue) {
        this.powerValue = powerValue;
    }

    public int getPowerState() {
        return powerState;
    }

    public void setPowerState(int powerState) {
        this.powerState = powerState;
    }

    public boolean isCharging(){
        return powerState == POWER_STATE_CHARGING;
    }
}
