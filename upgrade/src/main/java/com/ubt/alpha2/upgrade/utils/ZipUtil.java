package com.ubt.alpha2.upgrade.utils;

import android.content.Context;
import com.ubt.alpha2.upgrade.impl.IUnzipListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author: slive
 * @description: 
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ZipUtil {
    private Context context;
    private ArrayList file_directory=new ArrayList();

    private  IUnzipListener unzipListener;

    public ZipUtil(Context context, IUnzipListener unzipListener) {
        this.context = context;
        this.unzipListener = unzipListener;
    }

    public void unzip(String zipFileName, String outputDirectory)
            throws IOException {
        LogUtils.d("开始解压====111111111111: "+outputDirectory);
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFileName);
            Enumeration e = zipFile.entries();
            ZipEntry zipEntry = null;
            File dest = new File(outputDirectory);
            dest.mkdirs();
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                String entryName = zipEntry.getName();
                LogUtils.d("文件名字======"+entryName);
                file_directory.add(entryName);
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File f = new File(outputDirectory + File.separator
                                + name);
                        f.mkdirs();
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator
                                    + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator
                                    + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        File f = new File(outputDirectory + File.separator
                                + zipEntry.getName());
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);
                        int c;
                        byte[] by = new byte[1024];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    if(unzipListener != null)
                        unzipListener.onFailure(ex.toString());
                    throw new IOException("解压失败：" + ex.toString());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                        }
                    }
                    LogUtils.d("解压完成======");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            if(unzipListener != null)
                unzipListener.onFailure(ex.toString());
            throw new IOException("解压失败：" + ex.toString());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException ex) {
                }
            }
        }
        if(unzipListener != null)
            unzipListener.onSuccess("解压成功");
    }

    public ArrayList<String> getDirectoryList(){
        return file_directory;
    }
}
