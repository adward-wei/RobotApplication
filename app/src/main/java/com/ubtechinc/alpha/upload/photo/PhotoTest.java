package com.ubtechinc.alpha.upload.photo;

import android.content.Intent;
import android.os.Environment;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.provider.PhotoInfoVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @desc : 模拟乐拍发广播，用于构造照片上传的测试环境
 * @author: wzt
 * @time : 2017/6/12
 * @modifier:
 * @modify_time:
 */

public class PhotoTest {
    private static final String TAG = "PhotoTest";

    private static PhotoTest sPhotoTest;

    private ArrayList<String> fileList;

    private Timer mTimer;

    private int count=1;

    private PhotoTest() {
        mTimer = new Timer();
        fileList = new ArrayList<String>();
    }

    public static PhotoTest get() {
        if(sPhotoTest == null) {
            synchronized (PhotoTest.class) {
                if(sPhotoTest == null)
                    sPhotoTest = new PhotoTest();
            }
        }
        return sPhotoTest;
    }

    public void start() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendTestBroadcast();
            }
        }, 10000);
    }

    private void enumerateFile() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = dir + File.separator + "TestCameraFile";
        File file = new File(dir);
    }

    private void sendTestBroadcast() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String path = dir + File.separator + "TestCameraFile" + File.separator + "IMG_20170612_115739" + ".jpg";
        String path = dir + File.separator + "IMG_20170612_115739" + ".jpg";
        File file = new File(path);
        if(!file.exists()) {
            // 文件不存在
            mTimer.cancel();
            return;
        } else {
            PhotoInfoVisitor provider = PhotoInfoVisitor.getInstance();
            PhotoInfo photoInfo = provider.query(path);
            if(photoInfo != null) {
                // 数据库已经有了，表示已经上传
                mTimer.cancel();
                return;
            }
        }

        count +=1;
        LogUtils.d(TAG, "count=" + count);
        Intent intent = new Intent();
        intent.setAction(StaticValue.UPLOAD_PHOTO_BY_SERVICE);
        intent.putExtra(StaticValue.PHOTO_PATH_KEY, path);

        AlphaApplication.getContext().sendBroadcast(intent);
    }

}
