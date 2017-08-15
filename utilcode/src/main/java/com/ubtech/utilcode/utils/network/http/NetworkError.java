package com.ubtech.utilcode.utils.network.http;

/**
 * 网络错误类，定义了各种常见的网络错误
 * @author devilxie
 * @version 1.0
 *
 */
public interface NetworkError {
	/**
	 * 网络成功
	 */
	public final static int SUCCESS = 0;
	/**
	 * 网络未知错误
	 */
	public final static int FAIL_UNKNOWN = -1;
	/**
	 * 连接超时错误
	 */
	public final static int FAIL_CONNECT_TIMEOUT = -2;
	/**
	 * 资源未找到错误
	 */
	public final static int FAIL_NOT_FOUND = -3;
	/**
	 * 网络读写错误
	 */
	public final static int FAIL_IO_ERROR = -4;
	/**
	 * 用户中断
	 */	
	public final static int CANCEL = -5;
	
	/**
	 * 无网络
	 */
	public final static int NO_AVALIABLE_NETWORK= -6;	

}
