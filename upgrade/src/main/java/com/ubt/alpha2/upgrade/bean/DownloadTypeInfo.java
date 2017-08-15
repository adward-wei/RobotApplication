package com.ubt.alpha2.upgrade.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ubt on 2017/6/29.
 */

public class DownloadTypeInfo {
    public static final int DOWNLOAD_PATCH = 0;
    public static final int DOWNLOAD_MAIN = 1;

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({DOWNLOAD_PATCH,DOWNLOAD_MAIN})
    public @interface DownloadType{}
}
