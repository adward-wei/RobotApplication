package com.ubt.alpha2.download;


import android.text.TextUtils;

import com.ubt.alpha2.download.util.FileUtils;
import com.ubt.alpha2.download.util.StorageUtils;

import java.io.File;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class DownloadRequest {
    private String mUri;

    private File mFolder;

    private CharSequence mName;

    private CharSequence mDescription;

    private boolean mScannable;

    private NetStatus mNetStatus = NetStatus.Wifi;

    public enum NetStatus {
        Wifi, Mobile
    };

    // 是否为用户主动请求
    public boolean activeRequest = false;

    private DownloadRequest() {}

    private DownloadRequest(String uri, File folder, CharSequence name, CharSequence description, boolean scannable,
                            NetStatus status) {
        this.mUri = uri;
        this.mFolder = folder;
        this.mName = name;
        this.mDescription = description;
        this.mScannable = scannable;
        this.mNetStatus = status;
    }

    public String getUri() {
        return mUri;
    }

    public File getFolder() {
        return mFolder;
    }

    public CharSequence getName() {
        return mName;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public boolean isScannable() {
        return mScannable;
    }

    public NetStatus getNetStatus() {
        return mNetStatus;
    }

    public static class Builder {

        private String mUri;

        private File mFolder;

        private CharSequence mName;

        private CharSequence mDescription;

        private boolean mScannable;

        private NetStatus mNetStatus = NetStatus.Mobile;

        public Builder() {}

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setFolder(File folder) {
            this.mFolder = folder;
            return this;
        }

        public Builder setName(CharSequence name) {
            this.mName = name;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Builder setScannable(boolean scannable) {
            this.mScannable = scannable;
            return this;
        }

        public Builder setNetStatus(NetStatus status) {
            this.mNetStatus = status;
            return this;
        }

        public DownloadRequest build() {
            if(TextUtils.isEmpty(mName)){
                mName = FileUtils.getShortFileName(mUri);
            }
            if(mFolder == null){
                String root = StorageUtils.getSDCardPath();
                mFolder = new File(root+ File.separator+FileUtils.DOWNLOAD_DIR);
                if(!mFolder.exists())
                    mFolder.mkdirs();
            }
            DownloadRequest request = new DownloadRequest(mUri, mFolder, mName, mDescription, mScannable, mNetStatus);
            return request;
        }
    }
}
