package com.alpha2.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.alpha2.camera.DamEngineModel;
import com.alpha2.camera.arcsoft.dam.ArcsoftDetectionRectF;
import com.alpha2.camera.arcsoft.dam.RectfVector;
import com.ubtech.smartcamera.R;
import com.alpha2.camera.utils.RobotValues;

import java.util.Timer;
import java.util.TimerTask;

public class FaceView extends ImageView {
	private static final String TAG = "FaceView";
	private Context mContext;
	private Paint mLinePaint;
	private Matrix mMatrix = new Matrix();
	private RectF mRect = new RectF();
	private Drawable mFaceIndicator = null;
	private Drawable mFaceIndicator1;
	private FaceViewCallBack mCallback;

	private int xNowAngle = 120;
	private int yNowAngle = 120;
	private int xLast;
	private int yLast;
	private int x;
	private int y;
	private RectfVector mRectfVector;
	private long lastSendTime;
	private Vertical ver = Vertical.UP;
	private Horizontal hor = Horizontal.LEFT;
	private boolean isDetect;// 有检测结果
	private int GAP_DISTANCE=50;
	private int GAP_DISTANCE_Y=150;
	private int MULTI_GAP_DISTANCE=150;
	private int SCALE_ANGLE=1;
	private int SEND_INTERVAL_TIME=20;
	private Timer    mTimer0=null;
	private TimerTask mTimerTask0 = null;
	private Timer    mTimer1=null;
	private TimerTask mTimerTask1 = null;
	int xAngle =0;
	int yAngle =0;
	int limitAngle=0;
	int CONFIRM_FACE_TIMES=10;
	int singleface_confirm_times=0;
	int multiface_confirm_times=0;
	int limit_confirm_times=0;
	boolean TEST=false;
	int move_times=0;
	int loop_var=0x01;
	/**
	 * 竖直
	 * 
	 * @author zengdengyi
	 *
	 */
	public enum Vertical {
		UP, DOWN
	}

	/**
	 * 水平
	 * 
	 * @author zengdengyi
	 *
	 */
	public enum Horizontal {
		LEFT, RIGHT
	}

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
		mContext = context;
		mFaceIndicator = getResources().getDrawable(R.drawable.ic_face_find_2);
		mFaceIndicator1 = getResources().getDrawable(R.drawable.ic_face_find_1);

	}

	private Handler mHandler;
	public void setHandler(Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;
	}
	public void setFaces(RectfVector mRectfVector) {
		this.mRectfVector = mRectfVector;
		invalidate();
	}

	public void clearFaces() {
		mRectfVector = null;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		if (mRectfVector == null) {
			isDetect = false;
		//	cruiseAngle();
			if(SmartCameraActivity.status>=500&&SmartCameraActivity.STRIKE_HEAD) {
				if(move_times<1) {
					cruiseVertialAngle();
				}else{
					cruiseAngle();
				}
			}
			return;
		}
		ArcsoftDetectionRectF[] mFaces = mRectfVector.getmFaceRect();
		ArcsoftDetectionRectF[] mHands = mRectfVector.getmHandRect();
		ArcsoftDetectionRectF[] mHandVs = mRectfVector.getmHandVRect();


		// 处理检测结果

		if (mFaces == null && mHands == null && mHandVs == null) {
			isDetect = false;
		//	cruiseAngle();
			if(SmartCameraActivity.status>=500)
			cruiseVertialAngle();
			return;
		}
		canvas.save();
		p.setStyle(Paint.Style.FILL); // 填充实心
		p.setColor(Color.BLACK);// 设置BLACK
		canvas.drawRect(0, 0, StateValue.previewWidth,
				StateValue.previewHeight, p);// 画线

		Paint mScreeenCenterPoint=new Paint();
		mScreeenCenterPoint.setColor(Color.YELLOW);
		mScreeenCenterPoint.setStrokeWidth((float) 50.0);
		canvas.drawPoint(0,0,mScreeenCenterPoint);
		Paint mScreeenBoraderPoint=new Paint();
		mScreeenBoraderPoint.setColor(Color.GREEN);
		mScreeenBoraderPoint.setStrokeWidth((float) 50.0);
		canvas.drawPoint(640,480,mScreeenBoraderPoint);
		detectResult(mFaces, mHands, mHandVs, canvas);


		canvas.restore();
		super.onDraw(canvas);
	}

	/**
	 * 处理检测结果
	 * 
	 * @param mFaces
	 * @param mHands
	 * @param mHandVs
	 * @param canvas
	 */
	public void detectResult(ArcsoftDetectionRectF[] mFaces,
			ArcsoftDetectionRectF[] mHands, ArcsoftDetectionRectF[] mHandVs,
			Canvas canvas) {
		// 判断跟随模式
		int index = DamEngineModel.getModel();
		ArcsoftDetectionRectF[] mRectModel = null;
		if (mFaces != null) {// 检测到人脸
			if (index == RobotValues.FACE_TRACK
					|| index == RobotValues.FACE_POSE) {// 判断跟随的模式
				mRectModel = onDrawRect(mFaces, canvas);
				if (index == RobotValues.FACE_POSE) {// 拍照结果
					if (!isDetect) {// 第一次检测到人脸进行TTS
						isDetect = true;
						//	mCallback.onTTS(0);
					}
				}
			} else {// 当前模式不是人脸检测这只绘制人脸框
				onDrawRect(mFaces, canvas);
			}
		}
		if (mHands != null) {
			if (index == RobotValues.HAND_TRACK) {
				mRectModel = onDrawRect(mHands, canvas);
			} else {
				onDrawRect(mHands, canvas);
			}
		}
		if (mHandVs != null) {
			if (index == RobotValues.HANDV_POSE) {
				mRectModel = onDrawRect(mHandVs, canvas);
			} else {
				onDrawRect(mHandVs, canvas);
			}
		}
		// 得到响应的跟随识别框
		if (mRectModel != null) {
			// 计算并且执行角度控制
			calAngle(mRectModel, index);
		}
	}

	/**
	 * 绘制响应的识别框
	 * 
	 * @param mRect
	 * @param canvas
	 * @return
	 */
	public ArcsoftDetectionRectF[] onDrawRect(ArcsoftDetectionRectF[] mRect,
											Canvas canvas) {
		ArcsoftDetectionRectF[] rectModel = null;
		for (int i = 0; i < mRect.length; i++) {
		ArcsoftDetectionRectF face = mRect[i];
			if (RobotValues.ROBOT_RUN)
				if(android.os.Build.VERSION.RELEASE.equals("5.1.1")) {

				}else {
					face.rotationAngle();
				}
             //Navigation face rectangle
			mFaceIndicator1.setBounds(face.getLeft(), face.getTop(),
					face.getRight(), face.getBottom());
			mFaceIndicator1.draw(canvas);
			int left = face.getLeft() * StateValue.windowWidth
					/ StateValue.previewWidth;
			int top = face.getTop() * StateValue.windownHeight
					/ StateValue.previewHeight;
			int right = face.getRight() * StateValue.windowWidth
					/ StateValue.previewWidth;
			int bottom = face.getBottom() * StateValue.windownHeight
					/ StateValue.previewHeight;
			//Preview Face rectangle
			mFaceIndicator.setBounds(left, top, right, bottom);
			mFaceIndicator.draw(canvas);

			Paint mScreeenCenterPoint=new Paint();
			mScreeenCenterPoint.setColor(Color.RED);
			mScreeenCenterPoint.setStrokeWidth((float) 20.0);
			canvas.drawPoint(320,240,mScreeenCenterPoint);

			int x1 = Math.abs(face.getRight() - face.getLeft()) / 2
					+ face.getLeft();
			int y1 = Math.abs(face.getBottom() - face.getTop()) / 2 + face.getTop();
			Paint mfaceCenterPoint=new Paint();
			mfaceCenterPoint.setColor(Color.BLUE);
			mfaceCenterPoint.setStrokeWidth((float) 20.0);
			canvas.drawPoint(x1,y1,mfaceCenterPoint);

			int varx=x1-x;
			int vary=y1-y;
			   //Log.d(TAG,"xAngle yAngle x1 y1 "+varx+"  "+vary);
			Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			textPaint.setTextSize(80);
			textPaint.setColor(Color.GREEN);
			canvas.drawText("( " + varx + ", " + vary + ")", (float) face.getRight(), (float) face.getBottom(), textPaint);

			//first face as importance
//			if (i == 0) {
//				rectModel = face;
//			}
			rectModel=mRect;
		}
		return rectModel;

	}

	/**
	 * 计算响应的角度，并回调
	 * 
	 * @param face
	 */
	public void calAngle(ArcsoftDetectionRectF[] face, int index) {
		// 原点中心
		x = StateValue.previewWidth / 2;
		y = StateValue.previewHeight / 2;
		//Log.d("FaceView", "X  Y" + x + "    " + y);

		if (face.length >= 1) {
			// 人脸中心
			int x1 = Math.abs(face[0].getRight() - face[0].getLeft()) / 2
					+ face[0].getLeft();
			int y1 = Math.abs(face[0].getBottom() - face[0].getTop()) / 2 + face[0].getTop();

			//Log.d("FaceView", "X1  Y1" + x1 + "    " + y1);
			if (index == RobotValues.FACE_POSE && isDetect) {// 人脸拍照
				// 在原点中心的范围50氛围内开始拍照
				// if ((x - 50 <= x1) && (x1 <= x + 50)
				// && ((y - 50 <= y1) && (y1 >= y + 50))) {
				isDetect = false;
				return;
				// }

			}

			if (x1 == xLast && y1 == yLast && x == x1 && y == y1) {// 假如保持不动时候，不再发送舵机命令
				return;
			}
			xLast = x1;
			yLast = y1;
			xAngle = x1 - x;
			yAngle = y1 - y;
			if ((x - GAP_DISTANCE <= x1) && (x1 <= x + GAP_DISTANCE)) {
				Log.d(TAG, "one face x CENTER< 50 ");
					TimerCancelSendHorizontalAngle();
				if ((y - GAP_DISTANCE_Y <= y1) && (y1 <= y +GAP_DISTANCE_Y)) {
					Log.d(TAG, "one face y CENTER< 50 ");
					       singleface_confirm_times++;
                        if (singleface_confirm_times == CONFIRM_FACE_TIMES) {
						Log.d(TAG, "FOUND FACE,CONFIRM" +SmartCameraActivity.status);
						singleface_confirm_times = 0;
						if(SmartCameraActivity.status==SmartCameraActivity.FACE_DETECTION) {
							mHandler.sendEmptyMessage(SmartCameraActivity.FINDING_FACE);
						}
						TimerCancelSendVerticalAngle();
					}
					return;
				} else {
					Log.d(TAG, "one face y CENTER adjustment ");
					if(mTimer0==null) {
						TimerSendVerticalAngle();
					}
					return;
				}
			}else {
				if(	mTimer1==null) {
					TimerSendHorizontalAngle();
				}
			}

		}else{
			//TODO CONSIDER MULTI FACE, CURRENTLY USE THE ONE FACE MODEL
			Log.d(TAG, "two or more face adjustment");
                 int [] faceX1=new int[face.length];
			     int [] faceY1=new int[face.length];
			for(int i=0;i<face.length;i++){

				faceX1[i]= Math.abs(face[i].getRight() - face[i].getLeft()) / 2
						+ face[0].getLeft();
				faceY1[i]= Math.abs(face[i].getBottom() - face[i].getTop()) / 2 + face[i].getTop();
			}

			  int maxFaceX1=getMaxValue(faceX1);
			  int maxFaceY1=getMaxValue(faceY1);

			   xAngle=maxFaceX1-x;
			   yAngle=maxFaceY1-x;

			if ((x - GAP_DISTANCE <= maxFaceX1) && (maxFaceX1 <= x + GAP_DISTANCE)) {
				Log.d(TAG, "more face x CENTER< 50 ");
				TimerCancelSendHorizontalAngle();
				if ((y -GAP_DISTANCE <= maxFaceY1) && (maxFaceY1 <= y + GAP_DISTANCE)) {
					Log.d(TAG, "more face y CENTER< 50 ");
					multiface_confirm_times++;
					if (multiface_confirm_times == CONFIRM_FACE_TIMES) {
						Log.d(TAG, "FOUND FACE,CONFIRM");
						multiface_confirm_times= 0;
						mHandler.sendEmptyMessage(SmartCameraActivity.FINDING_FACE);
						TimerCancelSendVerticalAngle();
					}
					return;
				} else {
					Log.d(TAG, "more face y CENTER adjustment ");
					if(mTimer0==null) {
						TimerSendVerticalAngle();
					}
					return;
				}
			}else{
				if(	mTimer1==null) {
					TimerSendHorizontalAngle();
				}
			}
		}
		//ADD BY BRIAN X AXIS MAXIMUM TO TRIGGER TAKE PHOTO BEGINNING
		if(xNowAngle==75||xNowAngle==165){//left and right maximum or minimum
			limit_confirm_times++;
			if(limit_confirm_times==CONFIRM_FACE_TIMES){
				mHandler.sendEmptyMessage(SmartCameraActivity.FINDING_FACE);
			}
		}
		//ADD BY BRIAN X AXIS MAXIMUM TO TRIGGER TAKE PHOTO ENDING
		Log.i("FaceView", "xAngle yAngle calAngle（x，y） " + xNowAngle + "," + yNowAngle);

	}



	private void TimerSendVerticalAngle(){
		mTimer0 = new Timer();
		mTimerTask0=new TimerTask(){
			@Override
			public void run() {
				verticalControl(yAngle);
			}
		};
		mTimer0.schedule(mTimerTask0,10,200);
	}

	public void TimerCancelSendVerticalAngle(){
		if(mTimer0!=null) {
			mTimer0.cancel();
			mTimerTask0.cancel();
			mTimer0=null;
		}
	}

	private void TimerSendHorizontalAngle(){
		mTimer1 = new Timer();
		mTimerTask1=new TimerTask(){
			@Override
			public void run() {
				HorizontalControl(xAngle);
			}
		};
		mTimer1.schedule(mTimerTask1,10,200);
	}

	public void TimerCancelSendHorizontalAngle(){
		if(mTimer1!=null) {
			mTimer1.cancel();
			mTimerTask1.cancel();
			mTimer1=null;
		}
	}

	private int  getMaxValue(int[] arr){
		int max = arr[0];//定义变量
		for (int x=1; x<arr.length; x++ )
		{
			if (arr[x]>max)
			{
				max = arr[x];
			}
		}
		return max;
	}


	private void HorizontalControl(int xAngle){

		if (xAngle > GAP_DISTANCE) {
			xNowAngle = xNowAngle + SCALE_ANGLE;
			if (xNowAngle > 165) {
				xNowAngle = 165;
			}

		} else if (xAngle < -GAP_DISTANCE) {
			xNowAngle = xNowAngle -SCALE_ANGLE;
			if (xNowAngle < 75) {
				xNowAngle = 75;
			}
		}
		sendAngle(SmartCameraActivity.FACE_HORIZONAL_ANGLE);
	}

	private void verticalControl(int yAngle){
		if (yAngle > GAP_DISTANCE) {
			yNowAngle = yNowAngle + SCALE_ANGLE;
			if (yNowAngle > 135) {
				yNowAngle = 135;
			}
		} else if (yAngle < -GAP_DISTANCE) {
			yNowAngle = yNowAngle - SCALE_ANGLE;
			if (yNowAngle < 105) {
				yNowAngle = 105;
			}

		}
		sendAngle(SmartCameraActivity.FACE_VERTICAL_ANGLE);
	}
	/**
	 * 巡航角度
	 */
	public void cruiseAngle() {
		// 水平
		switch (hor) {
		case RIGHT:
			if (xNowAngle == 165) {
				xNowAngle = xNowAngle - 1;
				hor = Horizontal.LEFT;
				changeVertical();
			} else {
				xNowAngle = xNowAngle + 1;
			}
			break;
		case LEFT:
			if (xNowAngle == 75) {
				xNowAngle = xNowAngle + 1;
				hor = Horizontal.RIGHT;
				changeVertical();
			} else {
				xNowAngle = xNowAngle - 1;
			}
			break;
		}

		//Log.i("FaceView", "cruiseAngle（x，y） " + xNowAngle + "," + yNowAngle + " Ver="
		//		+ ver + " Hor=" + hor);
		//sendAngle(SmartCameraActivity.FACE_HORIZONAL_VERTICAL_ANGLE);
		sendAngle(SmartCameraActivity.FACE_HORIZONAL_ANGLE);
	}

	public void cruiseVertialAngle() {
		// vertial
		switch (ver) {
			case UP:
				if (yNowAngle == 105) {
					yNowAngle = yNowAngle + 1;
					ver = Vertical.DOWN;
					if(loop_var==2){
						move_times++;
					}
				} else {
					yNowAngle = yNowAngle - 1;
				}
				break;
			case DOWN:
				if (yNowAngle == 135) {
					yNowAngle = yNowAngle - 1;
					ver = Vertical.UP;
					loop_var<<=1;
				} else {
					yNowAngle = yNowAngle + 1;
				}
				break;
		}

		Log.i("FaceView", "cruiseVertialAngle" + "," + yNowAngle + " Ver="
				+ ver);
		//sendAngle(SmartCameraActivity.FACE_HORIZONAL_VERTICAL_ANGLE);
		sendAngle(SmartCameraActivity.FACE_VERTICAL_ANGLE);
	}

	public void changeVertical() {
		// 竖直
		switch (ver) {
		case UP:
			if (yNowAngle == 105) {
				yNowAngle = yNowAngle + 1;
				ver = Vertical.DOWN;
			} else {
				yNowAngle = yNowAngle - 1;
			}
			break;
		case DOWN:
			if (yNowAngle == 135) {
				yNowAngle = yNowAngle - 1;
				ver = Vertical.UP;
			} else {
				yNowAngle = yNowAngle + 1;
			}
			break;
		}
	}

	public void sendAngle(int type) {
		if (RobotValues.ROBOT_RUN) {
			// TODO Auto-generated method stub
			if (System.currentTimeMillis() - lastSendTime < SEND_INTERVAL_TIME) {// 防止频繁发送数据
				return;
			}
			lastSendTime = System.currentTimeMillis();
			if(mCallback!=null)
			mCallback.onRobotAngle(type,xNowAngle, yNowAngle);
		}
	}

	private void initPaint() {
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// int color = Color.rgb(0, 150, 255);
		int color = Color.rgb(98, 212, 68);
		// mLinePaint.setColor(Color.RED);
		mLinePaint.setColor(color);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(5f);
		mLinePaint.setAlpha(180);
	}

	public void setCallBack(FaceViewCallBack mCallback) {
		// TODO Auto-generated method stub
		this.mCallback = mCallback;
	}


	public void setCurrentAngle(byte angle) {
		// TODO Auto-generated method stub
		this.xNowAngle = angle;
	}

}
