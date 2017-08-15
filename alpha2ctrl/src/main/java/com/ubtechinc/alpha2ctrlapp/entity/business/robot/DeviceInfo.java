package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


import android.content.Context;

import com.ubtechinc.nets.utils.WifiControl;

/**
 * 设备信息
 * 
 * @author chenlin
 * 
 */
public class DeviceInfo {
	/**
	 * 设备名
	 */
	private String mDevName;

	/**
	 * Android版本号
	 */
	private String mOsVersion;

	/**
	 * IP地址
	 */
	private String mIpAddress;

	/**
	 * MAC ADDRESS
	 */
	private String mMacAddress;

	/** Alpha Robot Serial number **/     
	private String robotNo;
	public String getRobotNo() {
		return robotNo;
	}

	public void setRobotNo(String robotNo) {
		this.robotNo = robotNo;
	}

	public String getmMacAddress() {
		return mMacAddress;
	}

	public void setmMacAddress(String mMacAddress) {
		this.mMacAddress = mMacAddress;
	}

	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public DeviceInfo(Context context) {
		mDevName = android.os.Build.MODEL;
		mOsVersion = android.os.Build.VERSION.RELEASE;

		WifiControl wifiControl = WifiControl.get(context);
		int nAddress = wifiControl.getIPAddress();
		mIpAddress = intToIp(nAddress);

		mMacAddress = wifiControl.getMacAddress();
		robotNo = mMacAddress.replace(":", "");
	}

	public DeviceInfo() {

	}

	public String getmDevName() {
		return mDevName;
	}

	public void setmDevName(String mDevName) {
		this.mDevName = mDevName;
	}

	public String getmOsVersion() {
		return mOsVersion;
	}

	public void setmOsVersion(String mOsVersion) {
		this.mOsVersion = mOsVersion;
	}

	public String getmIpAddress() {
		return mIpAddress;
	}

	public void setmIpAddress(String mIpAddress) {
		this.mIpAddress = mIpAddress;
	}

}
