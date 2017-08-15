/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.ubt.alphaqrlib.R;

public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {

	// yuyong
	public final static int BindingRequestCode = 1001;
	public String uncode_result = "uncode_result";
	public final static String Result_other_way = "__Result_other_way__";
	private TextView txt_other_way;
	/** 二维码扫描结果反馈 **/
	public static final String ALPHA_QR_CODE = "com.ubt.alpha2.qr_code";
	private static final String TAG = CaptureActivity.class.getSimpleName();
	private boolean RESULT_TIMEOUT=false;

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		IntentFilter ift_cancel = new IntentFilter();
		ift_cancel.addAction("com.ubt.alpha2.qr_code.cancle");
		this.registerReceiver(mCancelBroadcast, ift_cancel);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.top_title_red);// 閫氱煡鏍忔墍闇�棰滆壊
		}
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		try {
			setContentView(R.layout.this_lib_capture_zxing);
		} catch (Exception e) {
			e.printStackTrace();
		}

		hasSurface = false;

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
		// 淇敼绔栧睆
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		}
		init();
		// 淇敼绔栧睆缁撴潫
	}

	private BroadcastReceiver mCancelBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Log.d(TAG,"CANCEL THE CAPTUREACTIVITY");
			CaptureActivity.this.finish();
		}
	};
	private Timer timer;

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public void onBack(View v) {

		this.finish();

	}

	@Override
	protected void onResume() {
		super.onResume();

		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		// yuyong
		txt_other_way = (TextView) findViewById(R.id.txt_other_way);
		txt_other_way.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				data.putExtra(uncode_result, Result_other_way);

				setResult(BindingRequestCode, data);
				finish();
			}
		});

		// yuyong
		Display mDisplay = getWindowManager().getDefaultDisplay();
		int w = mDisplay.getWidth();
		// viewfinderView.setCustomerRect(w / 2 - w / 4 - 50, w / 2 + w / 4 +
		// 50,
		// w / 2 - w / 4 - 70, w / 2 + w / 4 + 30);

		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		lastResult = null;
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {

			initCamera(surfaceHolder);
		} else {

			surfaceHolder.addCallback(this);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

		Intent intent = getIntent();

		source = IntentSource.NONE;
		decodeFormats = null;
		characterSet = null;

		if (intent != null) {

			String action = intent.getAction();

			if (Intents.Scan.ACTION.equals(action)) {

				source = IntentSource.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
				decodeHints = DecodeHintManager.parseDecodeHints(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH)
						&& intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						cameraManager.setManualFramingRect(width, height);
					}
				}

				if (intent.hasExtra(Intents.Scan.CAMERA_ID)) {
					int cameraId = intent.getIntExtra(Intents.Scan.CAMERA_ID,
							-1);
					if (cameraId >= 0) {
						cameraManager.setManualCameraId(cameraId);
					}
				}

			}

			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);

		}
	}

	@Override
	protected void onPause() {

		// yuyong
		// 锟剿达拷锟斤拷锟斤拷强锟斤拷锟剿筹拷锟侥达拷锟斤拷

		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		beepManager.close();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {

		inactivityTimer.shutdown();
		if (mCancelBroadcast != null) {
			this.unregisterReceiver(mCancelBroadcast);
		}
		cancelTask();
		if(RESULT_TIMEOUT){
			Intent data = new Intent(ALPHA_QR_CODE);
			data.putExtra(uncode_result, "");
			data.putExtra("flag", false);
			CaptureActivity.this.sendBroadcast(data);
		}else {
			Intent data = new Intent(ALPHA_QR_CODE);
			data.putExtra(uncode_result, "onDestroy");
			data.putExtra("flag", false);
			CaptureActivity.this.sendBroadcast(data);
		}
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;

		// yuyong
		// 锟斤拷锟截斤拷锟�------------for activity
		Intent data = new Intent(ALPHA_QR_CODE);
		data.putExtra(uncode_result, lastResult.getText());
		data.putExtra("flag", true);
		// zdy start send broadcast to service
		this.sendBroadcast(data);
		// zdy end
		setResult(BindingRequestCode, data);
		finish();

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);

			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	public class Task extends TimerTask {
		public void run() {
			if (timer != null) {
				Log.i("qrcode", "qrcode time out");
				onTimeOut();
			}
		}
	}

	public void init() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new Task(), 300 * 1000);
		}
	}

	public void onTimeOut() {
		RESULT_TIMEOUT=true;
		CaptureActivity.this.finish();
	}

	public void cancelTask() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
