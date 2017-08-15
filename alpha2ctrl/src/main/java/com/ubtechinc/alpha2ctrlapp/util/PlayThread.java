package com.ubtechinc.alpha2ctrlapp.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.util.Log;

import com.ubtechinc.alpha2ctrlapp.constants.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class PlayThread extends Thread {
//877 937 1717
	private byte[] buffer;
	private long delay = 0;
	private int mnHandler;
	private AudioTrack atrack;
	private Timer playTimer;
	private PlayListener listener;
	private StatusUpdateTimerTask mStatusUpdateTimerTask;
	private long defTime = 1000;
	private long millisPlayTime;

	private class StatusUpdateTimerTask extends TimerTask {

		@Override
		public void run() {
			if (atrack != null) {
				// atrack.release();// �رղ��ͷ���Դ
				//atrack.stop();
				//atrack = null;
				// Log.i("zdy", "" + atrack.getPlayState());
			}

			listener.onComplete();
			stopTask();
		}
	}

	public interface PlayListener {
		public void onComplete();
	}

	public PlayThread(short[] b, int lens, long d, int nHandler,
                      PlayListener listener) {
		this.listener = listener;
		buffer = new byte[lens * 2];
		delay = d;
		mnHandler = nHandler;
		int i, j;
		for (i = 0, j = 0; i < lens; i++, j += 2) {

			buffer[j + 0] = (byte) (b[i] >> 0);
			buffer[j + 1] = (byte) (b[i] >> 8);
		}
		start();
	}

	private void initTimer() {
		playTimer = new Timer();
		mStatusUpdateTimerTask = new StatusUpdateTimerTask();
	}

	private synchronized void stopTask() {
		if (mStatusUpdateTimerTask != null) {
			mStatusUpdateTimerTask.cancel();
			mStatusUpdateTimerTask = null;
			playTimer.purge();
			playTimer.cancel();
		}
	}

	public synchronized void stopPlay() {
		if (atrack != null
				&& atrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
			stopTask();
			atrack.stop();
			atrack.release();// �رղ��ͷ���Դ
			atrack = null;
		}
		delay = 0;
		this.interrupt();
	}

	public void run() {
		// delay play if necessary
		while (delay > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delay -= 1000;
		}

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		atrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		/* 48000 */Constants.sampleRate,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, buffer.length,
				AudioTrack.MODE_STATIC);
		atrack.setPlaybackPositionUpdateListener(new OnPlaybackPositionUpdateListener() {

			@Override
			public void onPeriodicNotification(AudioTrack track) {
				// TODO Auto-generated method stub
				Log.i("zdy", "************");
			}

			@Override
			public void onMarkerReached(AudioTrack track) {
				// TODO Auto-generated method stub
				Log.i("zdy", "************");

			}
		});

		try {
			atrack.write(buffer, 0, buffer.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initTimer();
		atrack.play();
		// ����ʱ��
		playTimer.schedule(mStatusUpdateTimerTask, 0, playTime());
	}

	public long playTime() {
		// int i = Constants.kPlayJitter + Constants.kDurationsPerHail
		// + Constants.kBytesPerDuration * buffer.length
		// + Constants.kDurationsPerCRC;
		// long j = i * Constants.kSamplesPerDuration;
		// j = (long) (j / Constants.kSamplingFrequency * 1000);
		// return j;
		int len = buffer.length;
		long i = (long) (len / (Constants.avgBytesPerSec ));
		Log.i("zdy", "i" + i);
		return i;
	}
}
