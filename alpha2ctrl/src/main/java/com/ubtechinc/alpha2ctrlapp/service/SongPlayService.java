package com.ubtechinc.alpha2ctrlapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;

/**
 * Created by ubt on 2017/2/13.
 */

public class SongPlayService extends IntentService {
    private static final String TAG = "SongPlayService";

    public static final String EXTRA_MUSIC_NAME = "path";

    public SongPlayService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String musicName  = intent.getStringExtra(EXTRA_MUSIC_NAME);
        Logger.d("nxy", "[onHandleIntent]play music, musicName=" + musicName);
        if(!TextUtils.isEmpty(musicName)) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                if(!musicName.endsWith("mp3")) {
                    musicName += ".mp3";
                }
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC );
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                Logger.d("nxy", "currentVolume = " + currentVolume + ", maxVolume=" + maxVolume);
                if(currentVolume < (int)(maxVolume*0.6)) {
                    audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, (int)(maxVolume*0.6), 0);
                }

                AssetFileDescriptor afd = getAssets().openFd(musicName);
                mediaPlayer.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(), afd.getLength());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                Logger.d("nxy", "start play " + musicName);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, currentVolume, 0);
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
