package com.ubtechinc.alpha.utils;

import android.os.Environment;
import android.os.StatFs;

import com.ubtech.utilcode.utils.LogUtils;

import java.io.File;

/**
 * @ClassName StorageUtil
 * @date 4/18/2017
 * @author wangzengtian
 * @Description 存储空间管理
 * @modifier
 * @modify_time
 */
public class StorageUtil {
	private static final boolean LOG_D_OPEN = true;
	private static final boolean LOG_I_OPEN = true;
	private static final boolean LOG_V_OPEN = true;
	private static final boolean LOG_E_OPEN = true;

	public static boolean externalMemoryAvailable(){  
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}  
      
	public static long getAvailableExternalMemorySize(){  
        if(externalMemoryAvailable()){  
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            LogUtils.d("available= blockSize=" + blockSize);
            LogUtils.d("available= availableBlocks=" + availableBlocks);
            return availableBlocks*blockSize;
        } else{  
            return -1;  
        }  
	}  

	public static long getTotalExternalMemorySize(){  
        if(externalMemoryAvailable()){  
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();  
            long totalBlocks = stat.getBlockCount();  
            return totalBlocks*blockSize;  
        } else{  
            return -1;  
        }
	}
    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
	public static long getAvailableInternalMemorySize(){  
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return availableBlocks*blockSize;  
	}
    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
	public static long getTotalInternalMemorySize(){  
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return totalBlocks*blockSize;  
	} 

    public static boolean isAvailableExternalMemoryGreaterThan1G() {
        boolean isGreaterThan1G = true;
        int available = (int) (getAvailableExternalMemorySize() / (1024*1024));
        LogUtils.d("!!!!!!!!!!!!! available=" + available);
        if(available < 1124) {
            isGreaterThan1G = false;
        } else {
        }
        return isGreaterThan1G;
    }

    public static boolean isAvailableExternalMemoryGreaterThan100M() {
        boolean isGreaterThan100M = true;
        int available = (int) (getAvailableExternalMemorySize() / (1024*1024));
        LogUtils.d("!!!!!!!!!!!!! available=" + available);
        if(available < 200) {
            isGreaterThan100M = false;
        } else {
        }
        return isGreaterThan100M;
    }
}