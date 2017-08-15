package com.alpha2.camera.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Player implements
		OnPreparedListener {

	public MediaPlayer mediaPlayer; // 媒体播放器

	// 初始化播放器
	public Player(OnCompletionListener listener ) {
		super();
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 *            url地址
	 */
	public void playUrl(String name, Context cnt) {
		try {
			AssetManager am = cnt.getAssets();// 获得该应用的AssetManager
			AssetFileDescriptor afd = am.openFd(name);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(afd.getFileDescriptor()); // 设置数据源
			mediaPlayer.prepare(); // prepare自动播放
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					stop();
				}
			});
		} catch (Exception e) {
			Log.i("BRIAN", "" + e.toString());
			e.printStackTrace();
		}
	}

	// 停止
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		Log.e("mediaPlayer", "onPrepared");
	}

}
