package com.ubt.alpha2.upgrade.utils;


import android.text.TextUtils;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.patch.PatchManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * @author: slive
 * @description: 
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class FileManagerUtils {

    public static String getFileName(String fileName){
        return fileName.substring(fileName.lastIndexOf(File.separator)+1);
    }

    /**
     * 删除单个文件
     *
     * @param {@link #path} 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    //删除new文件下的所有内容
    public static void deleteAllFile(String path){
        File fileDir = new File(path);
        if(fileDir.exists() && fileDir.isDirectory() ){
            deleteFolderFile(path,true);
        }
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeLocalConfig(){
        String localConfigJson = "{\"apks\": [\n" +
                "\t{\n" +
                "\t\t\"packagename\": \"com.ubtechinc.alpha2services\",\n" +
                "\t\t\"versionCode\": \"17\",\n" +
                "\t\t\"versionName\":\"V1.1.2.25\",\n" +
                "\t\t\"fileType\":\"apkPackage\",\n" +
                "\t\t\"filename\":\"alpha2services_V1.1.2.25.apk\",\n" +
                "\t\t\"apkpackageMd5Value\":\"284DE08DB0B1E7B96E5DA96E88A3503A\"\n" +
                "\t}\n" +
                "\t],\n" +
                "\n" +
                "\t\"version\": \"v1.0.1\"\n" +
                "}";
        writeProcess(localConfigJson);
    }

    public static void writeProcess(String json) {
        OutputStream fos = null;
        OutputStreamWriter out = null;
        try {
            fos = new FileOutputStream(FilePathUtils.LOCAL_CONFIG_PATH);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readConfigFile(String filepath) {
        StringBuffer jsonfile = new StringBuffer();
        File strFileName = new File(filepath);
        if(!strFileName.exists())
            return null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(strFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        String strLine = null;
        try {
            br = new BufferedReader(new InputStreamReader(fis,"gbk"));
            while ((strLine = br.readLine()) != null) {

                jsonfile.append(strLine.trim());
            }
            return jsonfile.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null){
                    br.close();
                }
                if(fis !=null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // newFilePath 合成后的新文件路径
    public static boolean patchNewFile(String pkn, String path,String newFilePath, String apkMd5) {
        LogUtils.d("filePATCH");
        PatchManager pm = new PatchManager();
        String old = ApkUtils.getDataApkPath(pkn, UpgradeApplication.getContext());
        if(old == null){
            //防止有的apk没有安装
            LogUtils.d("这个apk没有安装");
            return false;
        }
        File file = new File(path);
        if(!file.exists()){
            LogUtils.d("patch: "+  path+ " 文件不存在");
            return false;
        }
        String newPath = newFilePath +File.separator+ pkn + ".apk";
        boolean patchRet = pm.startPatch(old, newPath, path, apkMd5);
        //删除patch文件
        FileManagerUtils.deleteFile(path);
        if (patchRet) {
            // patch 合成成功,
            LogUtils.d("filePATCH OK");
        } else {
            //生成失败,删除合成的文件，请求完整路径
            LogUtils.d("filePATCH FAIL");
            FileManagerUtils.deleteFile(newPath);
        }
        return patchRet;
    }
}
