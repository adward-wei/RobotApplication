package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import java.io.Serializable;

/**
 * 传输第三方APP数据
 * @author chenlin
 *
 */
public class TransferAppData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 命令
	 */
	private int cmd;
	/**
	 * 命令对应的数据
	 */
	private byte[] datas;
	/**
	 * 第三方APP包名
	 */
	private String packageName;
	
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public byte[] getDatas() {
		return datas;
	}
	public void setDatas(byte[] datas) {
		this.datas = datas;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
}
