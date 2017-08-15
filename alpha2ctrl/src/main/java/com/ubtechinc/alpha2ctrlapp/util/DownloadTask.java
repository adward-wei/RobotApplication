package com.ubtechinc.alpha2ctrlapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.SDCardUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.ApkDownLoad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * apk
 * @author Administrator
 *
 */
public class DownloadTask extends Thread {


	private Context context;
	private String downLoadurl;
	private static final String apkStrorePath = Constants.apk_dir;// "/x431AutoDiag/x431AutoDiag.apk";
	private boolean exitDoaloading=false;

	private Handler handler;
	private ApkDownLoad  download;
	public DownloadTask(Context context, Handler handler, String url) {
		this.context = context;
		this.handler = handler;
		this.downLoadurl =url;
		download = new ApkDownLoad();
	}

	@Override
	public void run() {
				OutputStream fos = null;
				InputStream is = null;
				try {
					URL  url = new URL(downLoadurl);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(10000);
					con.setRequestMethod("GET");
					con.setRequestProperty("Connection", "Keep-Alive");
					con.setRequestProperty("Charset", "UTF-8");
					// con.setRequestProperty("Content-Type","multipart/form-data;boundary=******");
					con.setRequestProperty("content-type",
							"application/x-www-form-urlencoded");

					con.setDoInput(true);
					con.setDoOutput(true);
					int fileSize = con.getContentLength();
					Logger.i("downLoadurl "+downLoadurl);
					download.setFileSize(fileSize);
					is = con.getInputStream();
					int downloadSize = 0;
					long sdcardUsableSize = SDCardUtils.getFreeSpace();
					if (fileSize <= 0) {
						handler.obtainMessage(0).sendToTarget(); //获取网络文件异常

					} if(fileSize >sdcardUsableSize) {
						handler.obtainMessage(5).sendToTarget();// sd卡存储不足
						Intent intent=new Intent("show");
						context.sendBroadcast(intent);
					}else{
						handler.obtainMessage(1).sendToTarget();// 初始化
						byte[] bs = new byte[1024];
						int len;
						File file = new File(apkStrorePath);
						if (!file.exists()) {
							file.mkdirs();
						}

						String  fileName =context.getString(R.string.app_name)+".apk";

						Logger.i("wenjian "+apkStrorePath+fileName);
						File file2 = new File(apkStrorePath + fileName);
						if(file2.exists()){
//					Log.i("DownLoadTask", "删除之前的错误文件");
							file2.delete();
						}
						fos = new FileOutputStream(apkStrorePath + fileName);

						while ((len = is.read(bs)) != -1 &&!exitDoaloading) {
							fos.write(bs, 0, len);
							downloadSize += len;
							if (handler.hasMessages(1)) {
								handler.removeMessages(1);
							}
							handler.obtainMessage(2, downloadSize, fileSize).sendToTarget();// 正在下载
							// 下载完成后解压
							if (downloadSize == fileSize) {
								file2 = new File(apkStrorePath + fileName);
								if(file2.exists() && file2.length()>0){
									Log.i("DownloadTask", "下载成功");
									handler.obtainMessage(3).sendToTarget();
								}else{
									Log.i("DownloadTask", "下载失败");
									handler.obtainMessage(4).sendToTarget();
								}
							}else{
								long sdcardUsableSize2 =  SDCardUtils.getFreeSpace();
								if (sdcardUsableSize2 <fileSize){
									handler.obtainMessage(5).sendToTarget();// sd卡存储不足
								}
							}


						}

					}

				} catch (Exception e) {
					// TODO: handle exception
					handler.obtainMessage(6).sendToTarget();//异常
				} finally {
					try {
						if (fos != null) {
							fos.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

	}

	public boolean isExitDoaloading() {
		return exitDoaloading;
	}

	public void setExitDoaloading(boolean exitDoaloading) {
		this.exitDoaloading = exitDoaloading;
	}
}
