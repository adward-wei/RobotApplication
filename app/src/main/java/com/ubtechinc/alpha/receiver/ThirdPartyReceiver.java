package com.ubtechinc.alpha.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.upload.photo.PhotoUploadInHttpImpl;
import com.ubtechinc.alpha.upload.photo.PhotoUtil;

/**
 * @date 2017/6/9
 * @author wzt
 * @Description 接收第三方应用的广播
 * @modifier
 * @modify_time
 */

public class ThirdPartyReceiver extends BroadcastReceiver {

    public static final String TAG = "ThirdPartyReceiver";
    private static ThirdPartyReceiver instance;

    public static ThirdPartyReceiver getInstance() {
        if(instance == null) {
            synchronized (ThirdPartyReceiver.class) {
                if (instance == null) {
                    instance = new ThirdPartyReceiver();
                }
            }
        }
        return instance;
    }

    public void registerReceiver() {
        IntentFilter thirdPartyFilter = new IntentFilter();
        thirdPartyFilter.addAction(StaticValue.UPLOAD_PHOTO_BY_SERVICE);
        AlphaApplication.getContext().registerReceiver(this, thirdPartyFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(StaticValue.UPLOAD_PHOTO_BY_SERVICE)){
            LogUtils.i("主服务上传照片的广播");
            final String path  = intent.getStringExtra(StaticValue.PHOTO_PATH_KEY);
            LogUtils.d(TAG, "receive path = " + path);
            ThreadPool.runOnNonUIThread(new Runnable() {
                @Override
                public void run() {
                    /**获取camera那边保存图片文件夹路径**/
                    PhotoUtil.setDirPic(path.substring(0, path.lastIndexOf("/")));
                    PhotoUtil.changePicToThumbnail(path);

                    PhotoUploadInHttpImpl.getInstance(AlphaApplication.getContext()).handleRequestOfGetPicByThumbnail(path, null, null);
                }
            });

        }
    }
}
