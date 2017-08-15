package com.ubtech.utilcode.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 *  @author: logic.peng
 *  @email  : pdlogic1987@gmail.com
 *  @time  : 2016/9/21
 *  @desc  : 日志相关工具类
 *
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static boolean logSwitch      = true;
    private static boolean log2FileSwitch = false;
    private static String TAG = "TAG";
    //setprop log.tag.alpha VERBOSE
    private static boolean logFilter = Log.isLoggable(TAG, Log.VERBOSE);
    private static String  dir            = null;
    private static int     stackIndex     = 0;

    /**
     * 初始化函数
     * <p>与{@link #getBuilder(String)}两者选其一</p>
     *
     * @param logSwitch      日志总开关
     * @param log2FileSwitch 日志写入文件开关，设为true需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}
     * @param tag            标签
     */
    public static void init(boolean logSwitch, boolean log2FileSwitch, String tag) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator + "ulog" + File.separator + tag + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator + "ulog" + File.separator + tag + File.separator;
        }
        LogUtils.logSwitch = logSwitch;
        LogUtils.log2FileSwitch = log2FileSwitch;
        LogUtils.TAG = tag;
        logFilter = Log.isLoggable(TAG, Log.VERBOSE);
    }

    /**
     * 获取LogUtils建造者
     * <p>与{@link #init(boolean, boolean, String)}两者选其一</p>
     *
     * @return Builder对象
     */
    public static Builder getBuilder(String dirName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator + (TextUtils.isEmpty(dirName)?"log":dirName) + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator + (TextUtils.isEmpty(dirName)?"log":dirName) + File.separator;
        }
        return new Builder();
    }

    public static class Builder {

        private boolean logSwitch      = true;
        private boolean log2FileSwitch = false;
        private String  tag            = "TAG";

        public Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public void create() {
            LogUtils.logSwitch = logSwitch;
            LogUtils.log2FileSwitch = log2FileSwitch;
            LogUtils.logFilter = logFilter;
            LogUtils.TAG = tag;
        }
    }

    /**
     * Verbose日志
     *
     * @param msg 消息
     */
    public static void v(Object msg) {
        log(TAG, msg.toString(), null, 'v');
    }

    /**
     * Verbose日志
     * @param msg 消息格式
     * @param TAG
     */
    public static void v(String TAG, Object msg){
        log(TAG, msg.toString(), null, 'v');
    }

    /**
     *  Verbose日志: 可变形参数
     * @param msg
     * @param args
     */
    public static void V(String msg, Object...args){
        log(TAG, String.format(msg, args), null, 'v');
    }

    /**
     * Verbose日志: 可变形参数
     * @param agrs
     * @param msg 消息
     * @param tr  异常
     */
    public static void v(Throwable tr, String msg, Object...agrs) {
        log(TAG, String.format(msg, agrs), tr, 'v');
    }

    /**
     * Verbose日志
     * @param TAG
     * @param msg 消息
     * @param tr  异常
     */
    public static void v(String TAG, Object msg, Throwable tr) {
        log(TAG, msg.toString(), tr, 'v');
    }

    /**
     * Debug日志
     *
     * @param msg 消息
     */
    public static void d(Object msg) {
        log(TAG, msg.toString(), null, 'd');
    }

    /**
     * Debug 日志: 可变参数形式
     * @param msg 消息格式
     * @param agrs
     */
    public static void D(String msg, Object... agrs){
        log(TAG, String.format(msg, agrs), null, 'd');
    }

    /**
     * Debug 日志
     * @param TAG
     * @param msg
     */
    public static void d(String TAG, Object msg){
        log(TAG, msg.toString(), null, 'd');
    }

    /**
     * Debug日志
     *
     * @param agrs
     * @param msg 消息
     * @param tr  异常
     */
    public static void d(Throwable tr, String msg, Object... agrs) {
        log(TAG, String.format(msg, agrs), tr, 'd');
    }

    /**
     * Debug日志
     *
     * @param TAG
     * @param msg 消息
     * @param tr  异常
     */
    public static void d(String TAG, Object msg, Throwable tr) {
        log(TAG, msg.toString(), tr, 'd');
    }

    /**
     * Info日志
     *
     * @param msg 消息
     */
    public static void i(Object msg) {
        log(TAG, msg.toString(), null, 'i');
    }

    /**
     * info日志:可变形参
     * @param msg 消息格式
     * @param agrs
     */
    public static void I(String msg, Object... agrs){
        log(TAG, String.format(msg, agrs), null, 'i');
    }

    public static void i(String TAG, Object msg){
        log(TAG, msg.toString(), null, 'i');
    }

    /**
     * Info日志
     *
     * @param agrs
     * @param msg 消息
     * @param tr  异常
     */
    public static void i(Throwable tr, String msg, Object...agrs) {
        log(TAG, String.format(msg, agrs), tr, 'i');
    }

    /**
     * Info日志
     *
     * @param TAG
     * @param msg 消息
     * @param tr  异常
     */
    public static void i(String TAG, Object msg,Throwable tr) {
        log(TAG, msg.toString(), tr, 'i');
    }

    /**
     * Warn日志
     *
     * @param msg 消息
     */
    public static void w(Object msg) {
        log(TAG, msg.toString(), null, 'w');
    }

    /**
     * warn日志
     * @param msg 消息格式
     * @param agrs
     */
    public static void W(String msg, Object... agrs){
        log(TAG, String.format(msg, agrs), null, 'w');
    }

    /**
     * warn日志
     * @param msg 消息格式
     * @param tag
     */
    public static void w(String tag, Object msg){
        log(tag, msg.toString(), null, 'w');
    }

    /**
     * Warn日志
     *
     * @param args
     * @param msg 消息
     * @param tr  异常
     */
    public static void w(Throwable tr, String msg, Object... args) {
        log(TAG, String.format(msg,args), tr, 'w');
    }

    /**
     * Warn日志
     *
     * @param tag
     * @param msg 消息
     * @param tr  异常
     */
    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /**
     * Error日志
     *
     * @param msg 消息
     */
    public static void e(Object msg) {
        log(TAG, msg.toString(), null, 'e');
    }

    /**
     * error日志
     * @param msg 消息格式
     * @param agrs
     */
    public static void E(String msg, Object... agrs){
        log(TAG, String.format(msg, agrs), null, 'e');
    }

    /**
     * error日志
     * @param tag
     * @param msg  消息格式
     */
    public static void e(String tag, Object msg){
        log(tag, msg.toString(), null, 'e');
    }

    /**
     * Error日志
     *
     * @param args
     * @param msg 消息
     * @param tr  异常
     */
    public static void e(Throwable tr, String msg, Object... args) {
        log(TAG, msg.toString(), tr, 'e');
    }

    /**
     * Error日志
     *
     * @param tag
     * @param msg 消息
     * @param tr  异常
     */
    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void log(String tag, String msg, Throwable tr, char type) {
        if (msg == null || msg.isEmpty()) return;
        if (logSwitch) {
            boolean loggable = type == 'd'||type=='e' ||type== 'w' || type == 'i';
            //d,w,e始终打印log，v需根据setprop log.tag.alpha VERBOSE命令控制显示
            if (loggable || logFilter) {
                printLog(generateTag(tag), msg, tr, type);
                if (log2FileSwitch) {
                    log2File(type, generateTag(tag), msg + '\n' + Log.getStackTraceString(tr));
                }
            }
        }
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void printLog(final String tag, final String msg, Throwable tr, char type) {
        final int maxLen = 4000;
        for (int i = 0, len = msg.length(); i * maxLen < len; ++i) {
            String subMsg = msg.substring(i * maxLen, (i + 1) * maxLen < len ? (i + 1) * maxLen : len);
            switch (type) {
                case 'e':
                    Log.e(tag, subMsg, tr);
                    break;
                case 'w':
                    Log.w(tag, subMsg, tr);
                    break;
                case 'd':
                    Log.d(tag, subMsg, tr);
                    break;
                case 'i':
                    Log.i(tag, subMsg, tr);
                    break;
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type 日志类型
     * @param tag  标签
     * @param msg  信息
     **/
    private synchronized static void log2File(final char type, final String tag, final String msg) {
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + msg + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(dateLogContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bw);
                }
            }
        }).start();
    }

    public synchronized static void writeLog2File(final String fileName, final String msg) {
        Date now = new Date();

        String time = new SimpleDateFormat("MM_dd_HH_mm_ss", Locale.getDefault()).format(now);
        final String fullPath = dir + fileName+"_"+time + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bw);
                }
            }
        }).start();
    }

    /**
     * 产生tag
     *
     * @return TAG
     */
    private static String generateTag(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stackIndex == 0) {
            while (!stacks[stackIndex].getMethodName().equals("generateTag")) {
                ++stackIndex;
            }
            stackIndex += 3;
        }
        StackTraceElement caller = stacks[stackIndex];
        String callerClazzName = caller.getClassName();
        String format = "Tag[" + tag + "] %s[%s, %d]";
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
    }



    // 下面四个是默认tag的函数
    public static void i(Object... args) {
        log(Log.INFO, null, TAG, args);
    }

    public static void d(Object... args) {
        log(Log.DEBUG, null, TAG, args);
    }

    public static void w(Object... args) {
        log(Log.WARN, null, TAG, args);
    }

    public static void e(Object... args) {
        log(Log.ERROR, null, TAG, args);
    }

    public static void v(Object... args) {
        log(Log.VERBOSE, null, TAG, args);
    }

    //下面是传入类名打印log
    public static void i(Class<?> _class, Object... args){
        log(Log.INFO, null, _class.getName(), args);
    }
    public static void d(Class<?> _class, Object... args){
        log(Log.DEBUG, null, _class.getName(), args);
    }
    public static void e(Class<?> _class, Object... args){
        log(Log.ERROR, null, _class.getName(), args);
    }
    public static void v(Class<?> _class, Object... args){
        log(Log.VERBOSE, null, _class.getName(), args);
    }

    public static void w(Class<?> _class, Object... args){
        log(Log.WARN, null, _class.getName(), args);
    }
    public static boolean isDebug = true;
    private static void log(int priority, Throwable ex, String tag,
                            Object... args) {

        if (isDebug == false)
            return;

        StringBuffer log = new StringBuffer("");
        if (ex == null) {
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    try{
                        log.append(obj.toString());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } else {
            String logMessage = ex.getMessage();
            String logBody = Log.getStackTraceString(ex);
            log.append(String.format(LOG_FORMAT, logMessage, logBody));
        }

        Log.println(priority, tag, log.toString());
    }
    private static final String LOG_FORMAT = "%1$s\n%2$s";
}