package com.ubt.alpha2.download.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
 */

public class StorageUtils {

    private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

    public static long getAvailableStorage() {
        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    public static boolean isSdcardAvailbable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取SD卡路径
     * <p>先用shell，shell失败再普通方法获取，一般是/storage/emulated/0/</p>
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        if (!isSdcardAvailbable()) return null;
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();
        BufferedReader bufferedReader = null;
        try {
            Process p = run.exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while ((lineStr = bufferedReader.readLine()) != null) {
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray.length >= 5) {
                        return strArray[1].replace("/.android_secure", "") + File.separator;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                IOCloseUtils.close(bufferedReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

}
