package com.ubt.alpha2.upgrade.impl;

/**
 * 串口收到数据回调接口
 * @author chenlin
 */
public interface ISerialPortCallBack {
	/**
	 * 从串口收到数据
	 * @param buffer: 接到收数据的BUFFER
	 * @param size: 数据的大小
	 */
	void onDataReceived(final byte[] buffer, final int size);
}
