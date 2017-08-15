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

package com.google.zxing.client.android.encode;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.ubt.alphaqrlib.R;

/**
 * This class encodes data from an Intent into a QR code, and then displays it
 * full screen so that another person can scan it with their device.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EncodeActivity extends Activity {

	private static final String TAG = EncodeActivity.class.getSimpleName();
	private static final String USE_VCARD_KEY = "USE_VCARD";
	private QRCodeEncoder qrCodeEncoder;
	private TextView alpha_no;
	public static String serialNo = "";
	private Button btn_save;
	public final static String PIC_RECEIVE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ubtech/image/";
	private Bitmap qRBitMap;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
		} else {
			String action = intent.getAction();
			if (Intents.Encode.ACTION.equals(action)
					|| Intent.ACTION_SEND.equals(action)) {
				setContentView(R.layout.encode);
				alpha_no = (TextView) findViewById(R.id.alplah_no);
				alpha_no.setText("ID  "+serialNo);
				btn_save = (Button) findViewById(R.id.btn_save);
				btn_save.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						saveQrImage();
					}
				});
			} else {
				finish();
			}
		}
	}

	public void onBack(View v) {
		this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// // This assumes the view is full screen, which is a good assumption
		WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point displaySize = new Point();
		display.getSize(displaySize);
		int width = displaySize.x;
		int height = displaySize.y;
		int smallerDimension = width < height ? width : height;
		smallerDimension = smallerDimension * 7 / 8;

		Intent intent = getIntent();
		if (intent == null) {
			return;
		}

		try {
			boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
			qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension,
					useVCard);
			qRBitMap = qrCodeEncoder.encodeAsBitmap();
			if (qRBitMap == null) {
				Log.w(TAG, "Could not encode barcode");
				qrCodeEncoder = null;
				return;
			} else {
				ImageView view = (ImageView) findViewById(R.id.image_view);
				view.setImageBitmap(qRBitMap);
			}

			//

		} catch (WriterException e) {
			Log.w(TAG, "Could not encode barcode", e);
			qrCodeEncoder = null;
		}
	}

	private void saveQrImage() {
		if (qRBitMap != null) {
			File file = new File(PIC_RECEIVE_PATH, "core_image.png");
			// 从资源文件中选择一张图片作为将要写入的源文件
			try {
				FileOutputStream out = new FileOutputStream(file);
				qRBitMap.compress(CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!file.exists())
				Toast.makeText(this, "生成二维码失败，请重试", Toast.LENGTH_SHORT).show();
			// this.finish();
		}

	}
}
