package com.ubt.alpha2.upgrade.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;


public class StorageUtils {

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
            LogUtils.d( "available= availableBlocks=" + availableBlocks);
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

	public static long getAvailableInternalMemorySize(){  
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return availableBlocks*blockSize;  
	}  
      
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
        LogUtils.d( "!!!!!!!!!!!!! available=" + available);
        if(available < 1124) {
            isGreaterThan1G = false;
        } else {
        }
        return isGreaterThan1G;
    }

    public static boolean isAvailableExternalMemoryGreaterThan100M() {
        boolean isGreaterThan100M = true;
        int available = (int) (getAvailableExternalMemorySize() / (1024*1024));
        LogUtils.d( "!!!!!!!!!!!!! available=" + available);
        if(available < 200) {
            isGreaterThan100M = false;
        } else {
        }
        return isGreaterThan100M;
    }
}