package com.ubtechinc.alpha.behavior;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.utils.Player;

import java.io.File;

import timber.log.Timber;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public class MusicBehavior extends Behavior implements Player.PlayerStateCallBack {
    private static final int size = 1024 * 500;//500kb
    private String url;
    private Player player;

    void setUrl(String url) {
        if (url.startsWith(Names.FILE_PREFFIX)) {
            this.url = url.replace(Names.FILE_PREFFIX, Names.FILE_PREFFIX + Environment.getExternalStorageDirectory().getAbsolutePath());
            isNativeFile = true;
        }else {
            this.url = url;
        }
    }

    private boolean isNativeFile = false;

    @Override
    protected boolean onStart() {
        if (TextUtils.isEmpty(url)) return false;
        if (isNativeFile){
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            File file = new File(path);
            if (!file.exists()) {
                LogUtils.E(TAG, "file:"+path+" is not exist!!");
                return false;
            }
        }
        player = new Player(this);
        player.playUrl(url);
        Timber.d(TAG, "MusicBehavior start: music url= %s", url);
        return true;
    }

    @Override
    public void musicCallBack(@Player.PlayerState final int state) {
        Timber.d(TAG, "music behavior state= %d", state);
        if(state == Player.COMPLETED || state == Player.ERROR) {
            player.stop();
            HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    resultCallback.onBehaviorResult(state == Player.COMPLETED);
                }
            });
        }
    }
}
