package com.ubtechinc.alpha.upload.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.utilcode.utils.CloseUtils;
import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 相册图片util类
 *
 * @author Administrator
 */
public class PhotoUtil {
    public static final String TAG = PhotoUtil.class.getSimpleName();
    public static File DIR_THUMBNAIL_FILE = new File(TransferPhotoConstants.DIR_PIC_THUMBNAIL);
    //缩略图后缀名
    private static String SUFFIX = "_thumb.jpg";
    //原图保存路径
    private static String DIR_PIC =  FileUtils.getSDCardPath() + "ubtech/Camera";
    private static int QUALITY = 50;
    private static int HEIGHT = 100;
    private static int WIDTH = 100;


    public static void setDirPic(String dirPic) {
        if (!TextUtils.isEmpty(dirPic)) {
            DIR_PIC = dirPic;
            LogUtils.d(TAG, "setDirPic = " +DIR_PIC);
        }
    }

    /**
     * 将原图进行缩略并进行保存
     *
     * @param
     */
    public static void changePicToThumbnail(String srcFilePath) {
        File file = new File(srcFilePath);
        String fileName = file.getName();
        if (file.getParentFile() == null || !file.getParentFile().exists() || !file.exists() || !fileName.contains(".jpg")) {
            Log.i(TAG, "file parent is not exits or file is not exists or file is not image file");
            return;
        }
        int startIndex = srcFilePath.lastIndexOf("/") + 1;
        int endIndex = srcFilePath.lastIndexOf(".");
        String fileNameWithoutPre = srcFilePath.substring(startIndex, endIndex);
        String toFilePath = DIR_THUMBNAIL_FILE.getAbsolutePath() + "/" + fileNameWithoutPre + SUFFIX;
        if (!DIR_THUMBNAIL_FILE.exists()) {
            DIR_THUMBNAIL_FILE.mkdirs();
        }
        LogUtils.d(TAG, "DIR_THUMBNAIL_FILE = " +DIR_THUMBNAIL_FILE);
        transImage(srcFilePath, toFilePath, WIDTH, HEIGHT, QUALITY);
    }

    /**
     * 根据省略图查找原图
     *
     * @param thumbnailName
     * @return file原图文件
     */
    public static File getPicByThumbnail(String thumbnailName) {
        if (thumbnailName.contains("/")) {
            int posi = thumbnailName.lastIndexOf("/");
            thumbnailName = thumbnailName.substring(posi + 1);
        }

        String oriName = thumbnailName.replace(SUFFIX, ".jpg");
        File retFile = new File(new File(DIR_PIC), oriName);
        if (!retFile.exists()) {
            retFile = null;
        }
        return retFile;
    }

    /**
     * 获取缩略图的总数
     *
     * @return amount
     */
    public static int getAmountOfThumbnail() {
        int amount = 0;
//    	    File dirThumbnail=new File(TransferPhotoConstants.DIR_PIC_THUMBNAIL);
        File[] childFiles = DIR_THUMBNAIL_FILE.listFiles();
        int len = 0;
        if (childFiles == null || (len = childFiles.length) == 0) {
            return amount;
        } else {
            amount = len;
        }
        return amount;
    }

    /**
     * 获取缩略图的总数
     *
     * @return amount
     */
    public static File[] getFileArrOfThumbnail() {
        File[] files = null;
        int amount = 0;
//    	    File dirThumbnail=new File(TransferPhotoConstants.DIR_PIC_THUMBNAIL);
        File[] childFiles = DIR_THUMBNAIL_FILE.listFiles();
        int len = 0;
        if (childFiles == null || (len = childFiles.length) == 0) {
            return files;
        } else {
            files = childFiles;
        }
        return files;
    }

    /**
     * 根据省略图删除原图
     *
     * @param
     * @return
     */
    public static boolean delPicByThumbnail(String thumbfileName) {
        boolean result = false;
        File thumbFile = new File(DIR_THUMBNAIL_FILE, thumbfileName);
        File orgFile = new File(new File(DIR_PIC), thumbfileName.replace(SUFFIX, ".jpg"));
        Log.i(TAG, "thumbFile=" + thumbFile.getAbsolutePath() + ">>orgFile==" + orgFile.getAbsolutePath());
        if (!thumbFile.exists() || !orgFile.exists()) {
            Log.i(TAG, "result0==" + result);
            return result;
        } else if (thumbFile.delete() && orgFile.delete()) {
            result = true;
            Log.i(TAG, "thumbfileName:<" + thumbfileName + ">is deleted success");
        }
        Log.i(TAG, "result1==" + result);
        return result;
    }

    /**
     * 按指定尺寸缩小图片方法
     *
     * @param fromFile
     * @param toFile
     * @param width
     * @param height
     * @param quality
     */
    public static void transImage(String fromFile, String toFile, int width, int height, int quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            /**缩放图片的尺寸*/
            float scaleWidth = (float) width / bitmapWidth;
            float scaleHeight = (float) height / bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            /**产生缩放后的Bitmap对象*/
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            /**save file*/
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();/**记得释放资源，否则会内存溢出*/
            }
            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 把缩略图的文件夹压缩成zip包
     *
     * @param fromFiles 要压缩文件夹
     * @param toZipFile 压缩成功的文件夹
     * @return
     */
    public static boolean toZipFilesList(File[] fromFiles, String toZipFile) {
        boolean zipResult = false;
        File zipDir = new File(toZipFile);
        if (zipDir.exists()) {
            zipDir.delete();
        }
        boolean mkdirResult = false;
        try {
            mkdirResult = zipDir.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (!mkdirResult) {
            return zipResult;
        } else {
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(zipDir);
                zos = new ZipOutputStream(fos);
                FileInputStream fis = null;
                byte[] buffer = new byte[1024];
                int read;
                for (File file : fromFiles) {
                    fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer);
                    }
                    fis.close();
                }
                zipResult = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.closeIOQuietly(fos);
            }
            return zipResult;
        }
    }

}
