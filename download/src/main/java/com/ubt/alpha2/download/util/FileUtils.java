package com.ubt.alpha2.download.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class FileUtils {
    public static final String DOWNLOAD_DIR = "cache"+File.separator+"download";

    public static final File getDefaultDownloadDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), DOWNLOAD_DIR);
        }
        return new File(context.getCacheDir(), DOWNLOAD_DIR);
    }

    public static boolean isSDMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static final String getPrefix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static final String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static final String getShortFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("/") + 1);
    }
}
