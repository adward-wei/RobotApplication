package com.ubtechinc.alpha2ctrlapp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;

/**
 * 文 件 名: DataCleanManager.java 描 述:
 * 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 * 
 * 
 * 本应用数据清除管理器
 */
public class DataCleanManager {
	/**
	 * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static boolean  cleanInternalCache(Context context) {
			cleanDatabases(context);
		 return deleteFilesByDirectory(context.getCacheDir());
	}
	/**
	 * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static boolean  cleanInternalCache2(Context context) {
		 return deleteFileSafely(context.getCacheDir());
	}

	/**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	public static boolean cleanDatabases(Context context) {
		return deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	public static boolean cleanSharedPreference(Context context) {
		return deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	public static boolean cleanFiles(Context context) {
		return deleteFilesByDirectory(context.getFilesDir());
	}
	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	public static boolean cleanLibs(Context context) {
		PackageManager pm = context.getPackageManager();
		String dataDir ="" ;
		try {
			dataDir = pm.getApplicationInfo(context.getPackageName(), 0).dataDir;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataDir = dataDir + "/libs/cnlaunch/car/";
		return deleteGeneralFile(dataDir);
	}
	/** 
     * 删除文件或文件夹 
     *  
     * @param path 
     *            待删除的文件的绝对路径 
     * @return boolean 
     */  
    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;  
    
        File file = new File(path);
        if (!file.exists()) { // 文件不存在  
        	return true; 
        }  else{
        
        	 if (file.isDirectory()) { // 如果是目录，则单独处理  
 	            flag = deleteDirectory(file.getAbsolutePath());  
 	        } else if (file.isFile()) {  
 	            flag = deleteFile(file);  
 	        }
        }       
  
        return flag;  
    }  
    /** 
     * 删除文件 
     *  
     * @param file 
     * @return boolean 
     */  
    private static boolean deleteFile(File file) {
    	Log.i("SrorageManager", "删除文件*************");
    	return file.delete();
        
    }  
  
    /** 
     * 删除目录及其下面的所有子文件和子文件夹，注意一个目录下如果还有其他文件或文件夹 
     * 则直接调用delete方法是不行的，必须待其子文件和子文件夹完全删除了才能够调用delete 
     *  
     * @param path 
     *            path为该目录的路径 
     */  
    private static boolean deleteDirectory(String path) {
        boolean flag = true;  
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {  
            return flag;  
        }  
        File[] files = dirFile.listFiles();
        for (File file : files) { // 删除该文件夹下的文件和文件夹
            // Delete file.
            if (file.isFile()) { 
                flag = deleteFile(file); 
            } else if (file.isDirectory()) {// Delete folder  
                flag = deleteDirectory(file.getAbsolutePath());  
            }  
            if (!flag) { // 只要有一个失败就立刻不再继续  
                break;  
            }  
        }  
        flag = dirFile.delete(); // 删除空目录  
        return flag;  
    }  
	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static boolean  cleanExternalCache(Context context) {
		boolean hasClean = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			hasClean =deleteFilesByDirectory(context.getExternalCacheDir());
		}
		return hasClean;
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static boolean  cleanCustomCache(String filePath) {
		return deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
	 * 
	 * @param directory
	 */
	private static boolean  deleteFilesByDirectory(File directory) {
		boolean hasClean = true;
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				hasClean = item.delete();
				if(!hasClean )
					return hasClean;
			}
		}
		return hasClean;
	}
	
	private static boolean deleteFileSafely(File file) {
		if (file != null) {
			String tmpPath = file.getParent() + File.separator
					+ System.currentTimeMillis();
			File tmp = new File(tmpPath);
			file.renameTo(tmp);
			return tmp.delete();
		}
		return false;
	}
	// 获取文件  
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据  
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据  
    public static long getFolderSize(File file) throws Exception {
        long size = 0;  
        try {  
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {  
                // 如果下面还有文件  
                if (fileList[i].isDirectory()) {  
                    size = size + getFolderSize(fileList[i]);  
                } else {  
                    size = size + fileList[i].length();  
                }  
            }  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return size;  
    }  
	/** 
     * 格式化单位 
     *  
     * @param size 
     * @return 
     */  
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;  
        if (kiloByte < 1) {  
            return size + "Byte";  
        }  
  
        double megaByte = kiloByte / 1024;  
        if (megaByte < 1) {  
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";  
        }  
  
        double gigaByte = megaByte / 1024;  
        if (gigaByte < 1) {  
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";  
        }  
  
        double teraBytes = gigaByte / 1024;  
        if (teraBytes < 1) {  
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";  
        }  
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";  
    }  
      
      
    public static String getCacheSize(Context context) throws Exception {

		File file = context.getCacheDir();
		Log.i("nxy", "dir"+file.getAbsolutePath());
		
        return getFormatSize(getFolderSize(file));  
    }  
      

}
