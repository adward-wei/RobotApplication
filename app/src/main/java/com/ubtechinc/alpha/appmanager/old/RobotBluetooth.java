package com.ubtechinc.alpha.appmanager.old;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class RobotBluetooth {
    private String deviceName;
    private String deviceAddress;
    /**
     * 设备配对状态
     * BOND_NONE = 10;
     * BOND_BONDING = 11;
     * BOND_BONDED = 12;
     */
    private int bondState = -1;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }
}
