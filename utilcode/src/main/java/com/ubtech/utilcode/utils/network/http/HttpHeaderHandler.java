package com.ubtech.utilcode.utils.network.http;

import java.net.HttpURLConnection;

/**
 * http 响应头字段预处理器
 * @author wenbiao.xie
 *
 */
public interface HttpHeaderHandler
{
	/**
	 * 对连接进行响应头字段处理
	 * @param conn Http连接
	 * @return 返回处理结果，具体值可参考 NetworkError
	 */
	int handle(HttpURLConnection conn);
}
