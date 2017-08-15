package com.ubt.alpha2.download;

import java.io.File;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class DownloadInfo {
    private String name;
    private String uri;
    private File dir;
    private int progress;
    private long length;
    private long finished;
    private boolean acceptRanges;
    private int onlywifi;
    private boolean activeRequest = false;

    private int status;

    public DownloadInfo() {}

    public DownloadInfo(String name, String uri, File dir, int onlywifi) {
        this.name = name;
        this.uri = uri;
        this.dir = dir;
        this.onlywifi = onlywifi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public int getOnlywifi() {
        return onlywifi;
    }

    public void setOnlywifi(int onlywifi) {
        this.onlywifi = onlywifi;
    }

    public boolean getActiveRequest() {
        return activeRequest;
    }

    public void setActiveRequest(boolean activeRequest) {
        this.activeRequest = activeRequest;
    }


}
