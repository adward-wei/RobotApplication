/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2services.R;

/**
 * [音量控制类]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-12-31 上午11:04:54
 * @mofifier logic.peng 2017/4/26
 **/

public class SoundVolumesUtils {
	private static int[] rawids = {R.raw.media_volume, R.raw.wavefail, R.raw.qrcode,R.raw.app_switch_hint, R.raw.shoot};
	private static SoundVolumesUtils _instance;
	private Context mContext;
	private AudioManager mAudioManager;
	private SparseIntArray mSoundPoolMap;
	private SoundPool mSoundPool;
	private float mMaxVolume;  //当前设备能设置的最大音量
	private static final int MAX_VOLUME_LEVEL = 5; //当前设备音量划分的档位 不能大于15
	private float mVolumeStep;
	/**
	 * Requests the get of the Sound Manager and creates it if it does not
	 * exist.
	 * 
	 * @return Returns the single get of the SoundManager
	 */
	static public SoundVolumesUtils get(Context mContext) {
		if (_instance != null) return _instance;
		synchronized (SoundVolumesUtils.class) {
			if (_instance == null)
				_instance = new SoundVolumesUtils(mContext);
		}
		return _instance;
	}

	public SoundVolumesUtils(Context mContext) {
		this.mContext = mContext;
		// 音量控制,初始化定义
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mSoundPool = new SoundPool(rawids.length, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new SparseIntArray(rawids.length);
		loadSounds();
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mVolumeStep = mMaxVolume/MAX_VOLUME_LEVEL;
	}

	public void loadSounds() {
		for (int i = 0; i < rawids.length ; i ++){
			mSoundPoolMap.put(i+1,mSoundPool.load(mContext, rawids[i], 1));
		}
	}

	/**
	 * 增加音量
	 * 
	 * @author zengdengyi
	 * @date 2014-12-31 上午11:08:32
	 */
	public void addVolume(int value) {
		// 当前音量
		int currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		int maxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume += value;
		currentVolume = currentVolume < maxVolume ? currentVolume : maxVolume;
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		LogUtils.i("currentVolume:" + currentVolume);
		playSound();
	}

	/**
	 * 音量减
	 * 
	 * @author zengdengyi
	 * @date 2014-12-31 上午11:27:50
	 */
	public void mulVolume(int value) {
		// 当前音量
		int currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		currentVolume -= value;
		currentVolume = 0 < currentVolume ? currentVolume : 0;
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume,
				0); // tempVolume:音量绝对值
		LogUtils.i("currentVolume:" + currentVolume);
		playSound();
	}

	public void setVolume(int value){
		if(MAX_VOLUME_LEVEL < value){
			value = MAX_VOLUME_LEVEL;
		}else if(value < 0){
			value = 0;
		}
		int currentVolume = Math.round(mVolumeStep*value);

		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume,
				0); // tempVolume:音量绝对值
		LogUtils.i("setVolume : " + currentVolume);
	}

	/**
	 * 获取当前音量档位
	 * @return
     */
	public int getVolumeLevel(){
		int level = 0;
		int currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		level = Math.round(currentVolume/mVolumeStep);

		return  level;
	}
	public void playSound() {
		mSoundPool.play(mSoundPoolMap.get(1), 1, 1, 0, 0, 1.5f);
	}
	
	public void playSound(int i){
		mSoundPool.play(mSoundPoolMap.get(i), 1, 1, 0, 0, 1.5f);
	}
}
