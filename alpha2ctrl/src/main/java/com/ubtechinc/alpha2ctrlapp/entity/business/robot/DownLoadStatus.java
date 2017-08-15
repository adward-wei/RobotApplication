package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

public class DownLoadStatus {
	/**下载进度**/
	private String downloadProgress;
	/** 0 表示未下载，1表示下载中，2表示下载完成,3表示下载失败，4表示 文件不存在**/
	private int  downloadStaStatus;
	public String getDownloadProgress() {
		return downloadProgress;
	}
	public void setDownloadProgress(String downloadProgress) {
		this.downloadProgress = downloadProgress;
	}
	public int getDownloadStaStatus() {
		return downloadStaStatus;
	}
	public void setDownloadStaStatus(int downloadStaStatus) {
		this.downloadStaStatus = downloadStaStatus;
	}
	
}
