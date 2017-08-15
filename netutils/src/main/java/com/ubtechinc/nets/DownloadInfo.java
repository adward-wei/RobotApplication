package com.ubtechinc.nets;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/19
 * @modifier:
 * @modify_time:
 */

public class DownloadInfo {
    public static final long TOTAL_ERROR = -1;
    private String url;
    private long total;
    private long length;
    private String filePath;

    public DownloadInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
