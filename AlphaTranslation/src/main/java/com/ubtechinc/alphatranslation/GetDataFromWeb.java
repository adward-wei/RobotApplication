package com.ubtechinc.alphatranslation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDataFromWeb {
	public static ExecutorService pool = Executors.newFixedThreadPool(5);
	
	public interface IJsonListener {
		public static final String RETURN_FAIL = "RETURN_FAIL";

		void onGetJson(boolean isSuccess, String json, long request_code, String reason);
	}

	public static long getJsonByGet(final String _url,
			final IJsonListener _listener) {

		final long request_code = new Date().getTime();

		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(_url);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "utf-8"));  
				        //StringBuffer buffer = new StringBuffer(); 
						
						String jsonStr = "";
						int byteRead;
						while ((byteRead = in.read()) != -1) {
							jsonStr += (char) byteRead;
						}

						in.close();
						_listener.onGetJson(true, jsonStr, request_code , "");
					} else {
						_listener.onGetJson(false, _listener.RETURN_FAIL,
								request_code, String.valueOf(conn.getResponseCode()));
					}
				} catch (Exception e) {
					_listener.onGetJson(false, e.getMessage(), request_code, e.getMessage());
				}
			}
		});

		return request_code;

	}
//
//	public static long getJsonByPost(final String _url, final String _params,
//			final IJsonListener _listener) {
//
//		final long request_code = new Date().getTime();
//
//		pool.execute(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//
//					if (_url == null && _params == null) {
//						// 这是测试模式
//						Thread.sleep(1000);
//						_listener.onGetJson(true, null, request_code);
//						return;
//					}
//
//					URL url = new URL(_url);
//					HttpURLConnection conn = (HttpURLConnection) url
//							.openConnection();
//					conn.setConnectTimeout(5000);
//					conn.setRequestProperty("Content-Type",
//							"application/json;charset=UTF-8");
//					conn.setRequestProperty("accept", "*/*");
//					conn.setRequestProperty("connection", "Keep-Alive");
//					conn.setRequestProperty("user-agent",
//							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//					conn.setDoOutput(true);
//					conn.setDoInput(true);
//					PrintWriter out = new PrintWriter(new OutputStreamWriter(
//							conn.getOutputStream(), "UTF-8"));
//					out.print(_params);
//					out.flush();
//
//					String result = "";
//					BufferedReader in = new BufferedReader(
//							new InputStreamReader(conn.getInputStream(),
//									"UTF-8"));
//					String line;
//					while ((line = in.readLine()) != null) {
//						result += line;
//					}
//
//					_listener.onGetJson(true, result, request_code);
//
//					out.close();
//					in.close();
//
//				} catch (Exception e) {
//					_listener.onGetJson(false, e.getMessage(), request_code);
//
//				}
//
//			}
//		});
//
//		return request_code;
//
//	}
}
