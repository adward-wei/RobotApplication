package com.ubtechinc.alpha.model.app;

import java.io.Serializable;


/**
 * [传ubt app 按钮配置信息]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2015年9月18日 上午9:34:42
 * 
 **/

public class UbtAppButtonEventData implements Serializable{
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
