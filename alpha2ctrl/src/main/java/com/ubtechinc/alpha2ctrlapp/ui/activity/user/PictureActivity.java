package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.database.NoticeManager;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.DetailImageView;

import static com.tencent.qalsdk.service.QalService.context;

public class PictureActivity extends BaseContactActivity {

	private DetailImageView picture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		picture = (DetailImageView) findViewById(R.id.picture);
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(R.string.msg_photo);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Intent intent = getIntent();
		NoticeMessage noticeMessage = (NoticeMessage) intent.getSerializableExtra(IntentConstants.NOTICE_MESSAGE);
		if(noticeMessage != null) {
			NoticeManager.getInstance().updateNoticeStatus(noticeMessage.getNoticeId(), true); // 改为已读
			// String path = Environment.getExternalStorageDirectory()
			// .getAbsolutePath() + "/LazyList/13416082321171.jpg";
//		picture.setImageBitmap(zoomBitmap(
//				BitmapFactory.decodeFile(Constants.PIC_RECEIVE_PATH + name),
//				widthPixels, heightPixels));
//      LoadImage.LoadPicture(mContext, picture, name, 4);
			LoadImage.LoadAlumPicture(context, picture, noticeMessage.getNoticeContent(), 8);
		}

	}

	private class PictureHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg == null) {
				return;
			}
			switch (msg.what) {
			case MessageType.ALPHA_LOST_CONNECTED:
				isCurrentAlpha2MacLostConnection((String) msg.obj);
				break;
			}
		}

	}

	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null)
			return bitmap;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.reset();
		//matrix.setRotate(180);
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		if (scaleWidth < scaleHeight) {
			matrix.postScale(scaleWidth, scaleWidth);
		} else {
			matrix.postScale(scaleHeight, scaleHeight);
		}
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	// 确定不再需要该bitmap对象的时候可以将其回收掉
	public void recycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			System.gc();// 提醒系统及时回收
		}
	}



}
