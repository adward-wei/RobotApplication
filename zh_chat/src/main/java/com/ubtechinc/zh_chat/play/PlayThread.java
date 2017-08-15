package com.ubtechinc.zh_chat.play;

import android.content.Context;

import com.ubtech.utilcode.utils.LogUtils;

/**
 * 音乐播放线程类
 * 
 * @author UBX-dean
 *
 */
public class PlayThread extends Thread {
	public interface MusicCallBack {
		public void musicCallBack(int i);
	}
	private Player player;
	private String url;

	public PlayThread(Context context, MusicCallBack callBack, String url) {
		this.player = new Player(context, callBack);
		this.url = url;
	}

	@Override
	public void run() {
		player.playUrl(url);
	}

	public void stopMusic() {
		if (player != null) {
			LogUtils.d("PlayBusiness: player...");
			player.stop();
			player = null;
		}
	}
}