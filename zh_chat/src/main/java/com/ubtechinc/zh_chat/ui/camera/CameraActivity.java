package com.ubtechinc.zh_chat.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.ubtech.iflytekmix.R;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.zh_chat.ui.SpeechMainActivity;

import java.util.TimerTask;

public class CameraActivity extends Activity {
	private final static String TAG = "CameraActivity";
	private final static String TRANSFER_PHOTO_BY_XMPP_ACTION = "com.ubtrobot.action.transfer_photo";
	private final static String PHOTO_PATH = "photo_path";
	private CameraPreview preview;
	private TimerTask mTimerTask;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		init();
		HandlerUtils.runUITask(new Runnable() {
			@Override
			public void run() {
				preview.takePicture();
			}
		},2000);
	}

	public void init() {
		preview = (CameraPreview) findViewById(R.id.preview);
		preview.setHandler(this);
	}

	public void takePicture(View view) {
		preview.takePicture();
	}

	public void back(String savaPath) {
		Log.d(TAG, "CameraActivity back 1");
		mHandler.obtainMessage(0, savaPath).sendToTarget();
		Intent intent = new Intent(TRANSFER_PHOTO_BY_XMPP_ACTION);
		intent.putExtra(PHOTO_PATH, savaPath);
		this.sendBroadcast(intent);
		Intent intent2=new Intent();  
		setResult(1,intent2);
		Log.d(TAG, "CameraActivity back 2");
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent intent = new Intent();
		intent.setAction(SpeechMainActivity.ALPHA_CAMERA_EXIT_ACTION);
		sendBroadcast(intent);
	}

}
