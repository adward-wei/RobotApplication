package com.ubtechinc.alpha2ctrlapp.entity.business.app;
/**
 * @ClassName ApkDownLoad
 * @date 2016/10/13
 * @author tanghongyu
 * @Description Apk下载实体类
 * @modifier
 * @modify_time
 */
public class ApkDownLoad {

	private String packageName;
	// 存储在本地的文件名称,带文件后缀
	private String fileName;
	// 下载的文件大小
	private String downloadUrl;
	// Http已读取的文件大小
	private long fileSize;
	private long downloadSize;
	private int downloadStatus;
	private float progress;
	private String percent;
	//// 存储的路径
	private String fileSaveFolder;
	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getDownloadSize() {
		return downloadSize;
	}

	public void setDownloadSize(long downloadSize) {
		this.downloadSize = downloadSize;
	}

	public String getFileSaveFolder() {
		return fileSaveFolder;
	}

	public void setFileSaveFolder(String fileSaveFolder) {
		this.fileSaveFolder = fileSaveFolder;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(int downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ApkDownLoad that = (ApkDownLoad) o;

		if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
			return false;
		if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null)
			return false;
		return downloadUrl != null ? downloadUrl.equals(that.downloadUrl) : that.downloadUrl == null;

	}

	@Override
	public int hashCode() {
		int result = packageName != null ? packageName.hashCode() : 0;
		result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
		result = 31 * result + (downloadUrl != null ? downloadUrl.hashCode() : 0);
		return result;
	}
}
