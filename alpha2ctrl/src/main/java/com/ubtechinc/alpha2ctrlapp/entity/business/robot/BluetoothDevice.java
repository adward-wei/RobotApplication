package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import com.ubtech.utilcode.utils.StringUtils;

import java.io.Serializable;

/*************************
 * @date 2016/7/4
 * @author 唐宏宇
 * @Description 蓝牙设备实体
 * @modify
 * @modify_time
 **************************/
public class BluetoothDevice  implements Serializable  {

	private String userId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	private String deviceName;
	private String deviceAddress;
	/**
	 * 设备配对状态
	 * BOND_NONE = 10;
	 * BOND_BONDING = 11;
	 * BOND_BONDED = 12;
	 */
	private int bondState = -1;

	private boolean isConnected;

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean connected) {
		isConnected = connected;
	}

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

	@Override
	public boolean equals(Object o) {
		BluetoothDevice device = (BluetoothDevice)o;

		return StringUtils.isEquals(device.getDeviceAddress(), this.deviceAddress);
	}
}
