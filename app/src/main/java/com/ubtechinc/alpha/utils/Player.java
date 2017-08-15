package com.ubtechinc.alpha.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.support.annotation.IntDef;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		OnPreparedListener {
	public static final int PREPARED = 0;
	public static final int ERROR = -1;
	public static final int COMPLETED = 1;
	@IntDef(value = {PREPARED, ERROR, COMPLETED})
	public @interface PlayerState{}
	private MediaPlayer mediaPlayer; // 媒体播放器
	private PlayerStateCallBack callBack;
	private final String MUSIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/micTest/左声道.mp3";

	public Player(PlayerStateCallBack callBack) {
		this.callBack = callBack;
		initMediaPlayer();
	}

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			LogUtils.d("init MediaPlayer...");
		} catch (Exception e) {
			e.printStackTrace();
			this.callBack.musicCallBack(ERROR);
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
	}

	/**
	 * @param url url地址
	 */
	public void playUrl(final String url) {
		ThreadPool.getInstance().submit(new ThreadPool.Job<Object>() {
			@Override
			public Object run(ThreadPool.JobContext jc) {
				try {
					//播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；
					// 第五个参数为循环次数，0不循环，-1无线循环;
					// 第六个参数为速率，速率 :最低0.5最高为2，1代表正常速度
					mediaPlayer.reset();
					LogUtils.d("prepare play url =  " + url);
					mediaPlayer.setDataSource(url); // 设置数据源
					mediaPlayer.prepare(); // prepare自动播放
					mediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
					Player.this.callBack.musicCallBack(ERROR);
					if (mediaPlayer != null){
						mediaPlayer.release();
						mediaPlayer = null;
					}
				}
				return null;
			}
		});
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
		LogUtils.d("onPrepared");
		callBack.musicCallBack(PREPARED);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		LogUtils.d("onCompletion");
		callBack.musicCallBack(COMPLETED);
	}

	/** 缓冲更新*/
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		LogUtils.d("onBufferingUpdate " + percent + " buffer");
	}

	public interface PlayerStateCallBack {
		void musicCallBack(@PlayerState int state);
	}
}
