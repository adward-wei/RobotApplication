package com.ubtechinc.zh_chat.play;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;

import com.ubtechinc.zh_chat.play.PlayThread.MusicCallBack;

import timber.log.Timber;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		OnPreparedListener {
	public MediaPlayer mediaPlayer; // 媒体播放器
	private MusicCallBack callBack;
	private final String MUSIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/micTest/左声道.mp3";

	public Player(Context context, MusicCallBack callBack) {
		this.callBack = callBack;
		initMediaPlayer();
	}

	public void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			Timber.d("init MediaPlayer...");
		} catch (Exception e) {
			e.printStackTrace();
			this.callBack.musicCallBack(-1);
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
	}

	/**
	 * @param url url地址
	 */
	public void playUrl(String url) {
		try {
			//播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1无线循环;第六个参数为速率，速率    最低0.5最高为2，1代表正常速度
//			pool.play(sourceid, 0.8f, 0.8f, 1, -1, 1.0f);
			mediaPlayer.reset();
			Timber.d("prepare play url =  " + url);
			mediaPlayer.setDataSource(url); // 设置数据源
			mediaPlayer.prepare(); // prepare自动播放
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
			this.callBack.musicCallBack(-1);
			if (mediaPlayer != null){
				mediaPlayer.release();
				mediaPlayer = null;
			}
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
		Timber.d("onPrepared");
		callBack.musicCallBack(0);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Timber.d("onCompletion");
		callBack.musicCallBack(1);
	}

	/** 缓冲更新*/
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Timber.d("onBufferingUpdate " + percent + " buffer");
	}
}
