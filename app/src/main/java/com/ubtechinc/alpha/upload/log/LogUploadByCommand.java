package com.ubtechinc.alpha.upload.log;

import android.content.Context;
import android.text.TextUtils;

import com.ubtechinc.alpha.upload.log.Interface.UploadResultListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 通过语音命令或者手机命令来抓取上传log
 *
 * @author wangzhengtian
 * @Date 2017-03-03
 */

public class LogUploadByCommand implements UploadResultListener {
    /** 最大上传尝试次数 **/
    private static final int MAX_UPLOAD_TRY = 3;
    private static final int INTERVAL_TIME = 5*60*1000;
    private Context mContext;
    private int mUploadCount = 0;
    private String mFileName = "";

    /** 是否在抓log，相邻两次要间隔5分钟以上 **/
    private boolean isGettingLog = false;
    private Timer mTimer;

    private static LogUploadByCommand sLogUploadByCommand;

    /** 上一次成功抓取的时间 **/
    private long startTime;

    private LogUploadByCommand(Context context) {
        mContext = context;
        mTimer = new Timer();
    }

    public static LogUploadByCommand getInstance(Context context) {
        if(sLogUploadByCommand == null) {
            sLogUploadByCommand = new LogUploadByCommand(context);
        }

        return sLogUploadByCommand;
    }

    public int start() {
        int intervalTime = -1;
        int minute = -1;
        if(isGettingLog) {
            /** 距离上次抓取不足5分钟 **/
            return getSurplusTime();
        }

        LogSave2File logSave2File = LogSave2File.getInstance();
        String fileName = logSave2File.save();
        if(!TextUtils.isEmpty(fileName)) {
            isGettingLog = true;
            startTime = System.currentTimeMillis();

            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isGettingLog = false;
                }
            },INTERVAL_TIME);

            mUploadCount = mUploadCount + 1;
            mFileName = fileName;
//            FileUpload2QiNiuImpl upload2QiNiu = FileUpload2QiNiuImpl.getInstance(mContext);
//            upload2QiNiu.uploadFile2QINIU(Environment.getExternalStorageDirectory() + "/" + LogSave2File.LOG_DIRECTORY + "/" + fileName, this);
        }

        return 10;
    }

    @Override
    public void onUploadResult(int code) {
        if(code == UploadResultCode.ERROR_FAIL && mUploadCount < MAX_UPLOAD_TRY  && !TextUtils.isEmpty(mFileName)) {
            /** 再次尝试上传 **/
            mUploadCount = mUploadCount + 1;
//            FileUpload2QiNiuImpl upload2QiNiu = FileUpload2QiNiuImpl.getInstance(mContext);
//            upload2QiNiu.uploadFile2QINIU(Environment.getExternalStorageDirectory() + "/" + mFileName, this);
        }
    }

    private int getSurplusTime() {
        int intervalTime = (int) (System.currentTimeMillis() - startTime);
        int minute = 5 - (intervalTime/(1000*60));
        return minute;
    }
}
