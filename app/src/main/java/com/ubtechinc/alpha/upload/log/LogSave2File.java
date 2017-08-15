package com.ubtechinc.alpha.upload.log;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 将log保存到文件
 *
 * @author wangzhengtian
 * @Date 2017-02-28
 */

public class LogSave2File {
    private static final String TAG = "LogSave2File";
    private static final int MAX_LOG_TIME = 1200000;
    public static final String LOG_DIRECTORY = "Alpha2Log";

    /** 空闲 **/
    private static final int STATUS_LOG_IDLE = 0;
    /** 正在抓 **/
    private static final int STATUS_LOG_WORKING = 1;

    private static LogSave2File sLogSave2File;

//    private LogcatThread mThread;

    private Timer mTimer;
    /** 抓log的状态 **/
    private int mLogStatus = STATUS_LOG_IDLE;

    private LogSave2File(){

    }

    public static LogSave2File getInstance() {
        if(sLogSave2File == null) {
            sLogSave2File = new LogSave2File();
        }
        return sLogSave2File;
    }

    public void startSave() {
/*        try {
            Log.d(TAG, "startSave");
//            Process process = Runtime.getRuntime().exec("su");
//            String cmd = "logcat -v time > " + Environment.getExternalStorageDirectory() + "/20170228-2.txt";
//            process.getOutputStream().write(cmd.getBytes());
            Process process = Runtime.getRuntime().exec("logcat -v time -f /sdcard/20170228_2.txt");
        } catch (IOException e) {

        }*/
        Log.d(TAG, "startSave");
        if(mLogStatus == STATUS_LOG_IDLE) {
            Log.d(TAG, "startSaveing");
            mLogStatus = STATUS_LOG_WORKING;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopSave();
                }
            }, MAX_LOG_TIME);
        }
    }

    public void stopSave() {
        Log.d(TAG, "stopSave");
        if(mLogStatus == STATUS_LOG_WORKING) {
            mLogStatus = STATUS_LOG_IDLE;
            try {
                Log.d(TAG, "stopSaveing");
//            Process process = Runtime.getRuntime().exec("su");
//            process.getOutputStream().write("logcat -c".getBytes());
//            Process process = Runtime.getRuntime().exec("logcat -c");
//                String cmd = "logcat -d -v time -f /sdcard/" + LOG_DIRECTORY + "/" + UbtTimeUtil.getCurrentTime4RequestKey() + ".txt";
                String cmd = "logcat -d -v time -f /sdcard/" + LOG_DIRECTORY + "/" + ".txt";
                Process process = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(mTimer != null) {
                mTimer.cancel();
            }
        }
    }

    public String save() {
        Log.d(TAG, "stopSave");
        String fileName = null;

        checkDir();
        try {
            Log.d(TAG, "stopSaveing");
            fileName = "log_" +  ".txt";
//            fileName = "log_" + UbtTimeUtil.getCurrentTime4RequestKey() + ".txt";
            String cmd = "logcat -d -v time -f /sdcard/" + LOG_DIRECTORY + "/" + fileName;
            Process process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            fileName = "";
        }

        addAuxiliaryInfo(Environment.getExternalStorageDirectory()
                    +"/" + LOG_DIRECTORY + "/" + fileName);

        return fileName;
    }

    private void addAuxiliaryInfo(String fileName) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), true));
//            bufferedWriter.write("Android_version:" + RobotVersion.DEVICE_VERSION + "\n");
//            bufferedWriter.write("Service_version:" + RobotVersion.SERVICE_VERSION + "\n");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            Log.d(TAG, "addAuxiliaryInfo exception");
            e.printStackTrace();
        }
    }

    private void checkDir() {
        File file = new File(Environment.getExternalStorageDirectory()
                    +"/" + LOG_DIRECTORY);
        if(file != null) {
            if(!file.exists()) {
                file.mkdir();
            }
        }
    }
/*
    public void save() {
        mThread = new LogcatThread();
        mThread.start();
    }

    public void stop() {
        mThread.interrupt();
    }

    private class LogcatThread extends Thread {
        @Override
        public void run() {
            try {
                Process process = Runtime.getRuntime().exec("logcat -v time ");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("/sdcard/20170228-4.txt")));
                while(true) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder log = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        log.append(line);
                        Log.i(TAG, "line = " + line);
                    }
                    Log.i(TAG, "LALALL" + log.toString());
                    bufferedWriter.write(log.toString());
                    bufferedWriter.flush();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "logcatthread exit");
                        break;
                    }
                }
                bufferedWriter.close();
            } catch (IOException e) {
                Log.d(TAG, "IOException exit");
                e.printStackTrace();
            }
        }
    }
*/
/*
    private Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Process process = Runtime.getRuntime().exec("logcat -v time ");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("/sdcard/20170228-3.txt")));
                while(true) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder log = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        log.append(line);
                    }
                    Log.i(TAG, "LALALL" + log.toString());
                    bufferedWriter.write(log.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "thread exit");
                e.printStackTrace();
            }
        }
    });*/
}
