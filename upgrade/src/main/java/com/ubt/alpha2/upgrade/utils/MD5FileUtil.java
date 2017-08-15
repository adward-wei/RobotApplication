package com.ubt.alpha2.upgrade.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: slive
 * @description:  md5 encode
 * @create: 2017/6/27
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class MD5FileUtil {
    //private static final Logger logger = LoggerFactory
    //  .getLogger(MD5FileUtils.class);
    //读取1M的大小
    private final static int READ_SIZE = 1*1024*1024;
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //logger.error("MD5FileUtils messagedigest初始化失败", e);
        }
    }

    public static String getFileMD5String(String filePath) throws IOException{
        File file = new File(filePath);
        if(!file.exists())
            return "";
        return getFileMD5String(file);
    }

    public static String getFileMD5String(File file){
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
            byte[] buffer = new byte[READ_SIZE];
            int len ;
            while ((len = randomAccessFile.read(buffer))!= -1){
                messagedigest.update(buffer,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
        return bufferToHex(messagedigest.digest());
    }

    public static byte[] getFileMD5Bytes(File file) throws IOException {
//        FileInputStream in = new FileInputStream(file);
//        FileChannel ch = in.getChannel();
//        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
//                file.length());
//        messagedigest.update(byteBuffer);
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
            byte[] buffer = new byte[READ_SIZE];
            int len ;
            while ((len = randomAccessFile.read(buffer))!= -1){
                messagedigest.update(buffer,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messagedigest.digest();
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();

        File big = new File("G:\\1464835936031.zip");
        String md5 = getFileMD5String(big);

        long end = System.currentTimeMillis();
        System.out.println("md5:" + md5);
        System.out.println("time:" + ((end - begin) / 1000) + "s");

    }

}
