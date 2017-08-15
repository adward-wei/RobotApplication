package com.ubtechinc.alpha2ctrlapp.entity.business.robot;


/**
 * [用于listview Alpha2Adapter 装饰模式]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015-2-13 下午2:14:01
 * 
 **/

public class NewDeviceInfo extends DeviceInfo {

	private DeviceInfo mDeviceInfo;
	private boolean isConnected =false;
	private boolean isConnecting;
	private boolean isBind =false ;

	public boolean isConnecting() {
		return isConnecting;
	}

	public void setConnecting(boolean isConnecting) {
		this.isConnecting = isConnecting;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	

	public NewDeviceInfo(DeviceInfo mDeviceInfo) {
		this.mDeviceInfo = mDeviceInfo;
	}

	@Override
	public String getmMacAddress() {
		// TODO Auto-generated method stub
		return this.mDeviceInfo.getmMacAddress();
	}

	@Override
	public String getmDevName() {
		// TODO Auto-generated method stub
		return this.mDeviceInfo.getmDevName();
	}

	@Override
	public String getmOsVersion() {
		// TODO Auto-generated method stub
		return this.mDeviceInfo.getmOsVersion();
	}

	@Override
	public String getmIpAddress() {
		// TODO Auto-generated method stub
		return this.mDeviceInfo.getmIpAddress();
	}

	@Override
	public String getRobotNo() {
		// TODO Auto-generated method stub
		return this.mDeviceInfo.getRobotNo();
	}

	public boolean isBind() {
		return isBind;
	}

	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}
	
	

}
