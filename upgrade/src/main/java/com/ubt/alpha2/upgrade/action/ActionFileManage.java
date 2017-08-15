package com.ubt.alpha2.upgrade.action;

import com.ubt.alpha2.upgrade.utils.FilePathUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created by ubt on 2017/3/15.
 */
public class ActionFileManage {
    private static final String ACTION_TEMP_PATH = FilePathUtils.ACTION_PATH+File.separator+"actionInfoTmp.txt";
    private static final String ACTION_PATH = FilePathUtils.ACTION_PATH+File.separator+"/actionInfo.txt";
    private static File mFileTemp= new File(ACTION_TEMP_PATH);
    private static File mFile2 = new File(ACTION_PATH);

    public static void fileInit(){
        try {
            if(mFileTemp.exists()) {
                mFileTemp.delete();
            }
            mFileTemp.createNewFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //写txt临时文件
    public static boolean writeToTempTxt(String content) {
        try {
            OutputStreamWriter write = null;
            BufferedWriter out = null;

            try {   // new FileOutputStream(fileName, true) 第二个参数表示追加写入
                write = new OutputStreamWriter(new FileOutputStream(
                        mFileTemp,true), Charset.forName("gbk"));//一定要使用gbk格式
                out = new BufferedWriter(write, 1024);
            } catch (Exception e) {
            }

            out.write(content);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //追加txt动作文件
    public static boolean writeToActionTxt(String content) {
        try {
            OutputStreamWriter write = null;
            BufferedWriter out = null;

            try {   // new FileOutputStream(fileName, true) 第二个参数表示追加写入
                write = new OutputStreamWriter(new FileOutputStream(
                        mFile2,true), Charset.forName("gbk"));//一定要使用gbk格式
                out = new BufferedWriter(write, 1024);
            } catch (Exception e) {
            }

            out.write(content);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重命名
     * @param path
     * @param newName
     * @return
     */
    public static String renameFile(String path,String newName){
        boolean ret =  mFileTemp.renameTo(new File(newName));
        if(ret)
            return  ACTION_PATH;
        return null;
    }
}
