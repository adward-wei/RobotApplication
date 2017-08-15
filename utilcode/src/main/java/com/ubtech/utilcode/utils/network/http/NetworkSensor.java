package com.ubtech.utilcode.utils.network.http;

import java.net.InetSocketAddress;
/**
 * 网络检测器接口，用于上层网络检测功能扩展，便于改善网络方面的体验
 * @author devilxie
 * @version 1.0
 *
 */
public interface NetworkSensor
{
	/**
	 * 无效的APN
	 */
	public static final String	INVALID_ACCESS_POINT	= "None";

	/**
	 * 判断是否有有效的网络
	 * 
	 * @return 返回判断结果。 false 表示没有有效网络
	 */
	boolean hasAvailableNetwork();

	/**
	 * 获取当前使用的网络的APN
	 * 
	 * @return 返回APN，如果无网络，将返回 INVALID_ACCESS_POINT
	 */
	String getAccessPoint();

	/**
	 * 获取当前代理
	 * 
	 * @return 返回当前设置的代理
	 */
	InetSocketAddress getProxy();

	/**
	 * 获取当前网络的类型，包括区分WIFI，2G，3G
	 * 
	 * @return 返回当前网络的类型，如果无有效网络，将返回 INVALID_ACCESS_POINT
	 */
	String getNetworkType();
}
