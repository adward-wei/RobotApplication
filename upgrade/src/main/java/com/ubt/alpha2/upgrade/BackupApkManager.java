package com.ubt.alpha2.upgrade;

import android.content.Context;

import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.MD5FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author: slive
 * @description: 
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class BackupApkManager {
    private Context context;

    public BackupApkManager(Context ct) {
        this.context = ct;
    }

    public boolean backupApk(String pkn){
        final String old = ApkUtils.getDataApkPath(pkn, context);
        if(old == null){
            return true; //为了防止工厂出厂有apk没有安装
        }
        LogUtils.d("old path："+old);
        File file=new File(old);

        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            try {
                String md5 = MD5FileUtil.getFileMD5String(file).toUpperCase();
                LogUtils.d("md5:"+md5);
                fis = new FileInputStream(file);
                size = fis.available();
                LogUtils.d("old size:"+size);
                LogUtils.d("name:"+ FilePathUtils.OLD_PATH+"/"+pkn+".apk");
                if(copyFile(old,FilePathUtils.OLD_PATH+"/"+pkn+".apk",md5)){
                    return true;
                }
                return false;
            }catch (Exception e){
                LogUtils.d("Exception :");
                return false;
            }
        }else {
            LogUtils.d("需要的备份文件不存在，则不备份");
            return true;
        }
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public boolean copyFile(String oldPath, String newPath, String oldmd5) {
        boolean isCopySuc = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(FilePathUtils.OLD_PATH);
            if(!newFile.exists()){
                newFile.mkdirs();
            }

            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            LogUtils.d("copy 成功");
            File newPathFile = new File(newPath);
            String md5 = MD5FileUtil.getFileMD5String(newPathFile).toUpperCase();
            LogUtils.d("newPathFile md5=="+md5);
            if(oldmd5.equals(md5)){
                isCopySuc = true;
            }
        }catch (Exception e) {
            LogUtils.d("copy 失败");
            e.printStackTrace();
        }
        return isCopySuc;

    }
}
