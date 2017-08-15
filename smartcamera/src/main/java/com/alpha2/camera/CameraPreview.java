package com.alpha2.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.alpha2.camera.ui.FaceView;
import com.alpha2.camera.ui.FaceViewCallBack;
import com.alpha2.camera.ui.SmartCameraActivity;
import com.alpha2.camera.ui.StateValue;
import com.alpha2.camera.arcsoft.dam.ArcCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/**
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法 通过调用方SurfaceHolder.addCallBack来绑定该方法 竖屏预览时候 需要
 * mCamera.setDisplayOrientation(getDisplayOritation(0, 0)); //90 设置了上面参数后竖屏拍照
 * 图片还是0度显示没被旋转到90度 拍照时重新获取Camera.Parameters设置 cameraParameter.setRotation(90);
 * mCamera.setParameters(cameraParameter); mCamera.takePicture(shutterCallback,
 * pictureCallback, mPicture); 再调用takePicture
 * 
 * @author zw.yan
 * 
 */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	enum State {
		IDLE, BUSY
	}

	private State state = State.IDLE;

	private FaceViewCallBack mCallback;

	ByteBuffer mBufferForGLRenderer;
	private int width;
	private int height;
	FaceView mFaceView;
	private final int PREVIEW_WIDTH=800;
	private final int PREVIEW_HEIGHT=600;
	private final static String TRANSFER_PHOTO_BY_XMPP_ACTION = "com.ubtrobot.action.transfer_photo";
	private final static String PHOTO_PATH = "photo_path";


	@SuppressWarnings("deprecation")
	public CameraPreview(Context context) {
		this(context, null);
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}


	private void init(Context context) {
		// TODO Auto-generated method stub
		this.mContext = context;
		isSupportAutoFocus = context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_AUTOFOCUS);
		mHolder = getHolder();
		// 兼容android 3.0以下的API，如果超过3.0则不需要设置该方法
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		mHolder.addCallback(this);// 绑定当前的回调方法
		DisplayMetrics metric = new DisplayMetrics();
		metric = context.getApplicationContext().getResources()
				.getDisplayMetrics();
		StateValue.windowWidth = metric.widthPixels;
		StateValue.windownHeight = metric.heightPixels;
	}


	public void setCallBack(FaceViewCallBack mCallback) {
		// TODO Auto-generated method stub
		this.mCallback = mCallback;
	}
	private String TAG = "CameraPreview";
	/**
	 * Surface的控制器，用来控制预览等操作
	 */
	private SurfaceHolder mHolder;
	/**
	 * 相机实例
	 */
	private Camera mCamera = null;
	/**
	 * 图片处理
	 */
	public static final int MEDIA_TYPE_IMAGE = 1;
	/**
	 * 预览状态标志
	 */
	private boolean isPreview = false;
	/**
	 * 设置一个固定的最大尺寸
	 */
	private int maxPictureSize = 5000000;
	/**
	 * 是否支持自动聚焦，默认不支持
	 */
	private Boolean isSupportAutoFocus = false;
	/**
	 * 获取当前的context
	 */
	private Context mContext;

	/**
	 * 设置最适合当前手机的图片宽度
	 */
//	int setFixPictureWidth = 1920;
	int setFixPictureWidth = 1600;
	/**
	 * 设置当前最适合的图片高度
	 */
//	int setFixPictureHeight = 1080;
	int setFixPictureHeight = 1200;


	/**
	 * 创建的时候自动调用该方法
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera == null) {
			Log.d(TAG,"Current Android System version   "+android.os.Build.VERSION.SDK + "        ,"
					+ android.os.Build.VERSION.RELEASE);
			mCamera = CameraCheck.getCameraInstance(mContext);
		}
		try {
			if (mCamera != null) {
				Thread initCameraEngine=new Thread(new Runnable() {
						@Override
						public void run() {
							setCameraParms();
						}
			   });
				initCameraEngine.start();
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
				mCamera.setPreviewCallback(mPreviewCallback);
			}
		} catch (IOException e) {
			if (null != mCamera) {
				mCamera.release();
				mCamera = null;
				isPreview = false;
			}
			e.printStackTrace();
		}

	}

	/**
	 * 当surface的大小发生改变的时候自动调用的
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		Log.d(TAG,"surfaceChanged");
		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e){
			// ignore: tried to stop a non-existent preview
		}
		// start preview with new settings
		try {
			//setCameraParms();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			mCamera.setPreviewCallback(mPreviewCallback);
			reAutoFocus();

		} catch (Exception e){
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}

	}

	private void setCameraParms() {
		Camera.Parameters myParam = mCamera.getParameters();
		//set frame fps
		int CurPreRange[];
		CurPreRange = new int[2];
		myParam.getPreviewFpsRange(CurPreRange);
		Log.d(TAG, "Current Preview MinFps = " + CurPreRange[0]
				+ "  MaxFps = " + CurPreRange[1]);
		if (CurPreRange[0] < 15000)

		{
			CurPreRange[0] = 15000;
		}

		myParam.setPreviewFpsRange(CurPreRange[0], CurPreRange[1]);

			List<Camera.Size> mSupportedsizeList = myParam
					.getSupportedPictureSizes();
			Size optimalSize = getPreviewDisplaySize(mSupportedsizeList, PREVIEW_WIDTH, PREVIEW_HEIGHT);
			Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			Log.d(TAG,"DISPLAY GET ROTATION"+display.getRotation());

			if(display.getRotation()==Surface.ROTATION_0)

			{
				myParam.setPreviewSize(optimalSize.height, optimalSize.width);
				mCamera.setDisplayOrientation(90);
			}

			if(display.getRotation()==Surface.ROTATION_90)

			{
				myParam.setPreviewSize(optimalSize.width, optimalSize.height);
				if (android.os.Build.VERSION.RELEASE.equals("5.1.1")) {
					myParam.setRotation(0);  //rockchip
					myParam.set("rotation", 0);
				} else {
					myParam.setRotation(180);  //samsung
					myParam.set("rotation", 180);
					mCamera.setDisplayOrientation(180);
				}
			}

			if(display.getRotation()==Surface.ROTATION_180)

			{
				myParam.setPreviewSize(optimalSize.height, optimalSize.width);
			}

			if(display.getRotation()==Surface.ROTATION_270)

			{
				myParam.setPreviewSize(optimalSize.width, optimalSize.height);
				mCamera.setDisplayOrientation(180);
			}

			//myParam.setPreviewSize(optimalSize.width, optimalSize.height);
			//Camera.Size pictureSize = getLargestPictureSize(myParam);
			if(android.os.Build.VERSION.RELEASE.equals("5.1.1"))

			{
				myParam.setPictureSize(1600, 1200);
			}

			else

			{
				myParam.setPictureSize(setFixPictureWidth,
						setFixPictureHeight);
			}

			myParam.setJpegQuality(100);
			myParam.setPreviewFormat(ImageFormat.NV21);

			mCamera.setParameters(myParam);
			StateValue.previewWidth=mCamera.getParameters().getPreviewSize().width;
			StateValue.previewHeight=mCamera.getParameters().getPreviewSize().height;

//		if (RobotValues.ROBOT_RUN) {
//			//Preview Direction
//			//mCamera.setDisplayOrientation(180);
//		}


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG,"surfaceDestroyed");
		if (mCamera != null) {
			Log.d(TAG,"surfaceDestroyed release");
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	public Size getPreviewDisplaySize(List<Size> mSupportedsizeList, int width,
			int height) {
		Camera.Size size = null;
		if (mSupportedsizeList.size() > 1) {
			Iterator<Camera.Size> itos = mSupportedsizeList.iterator();
			while (itos.hasNext()) {
				Camera.Size curSize = itos.next();

				Log.e(TAG, " PreviewDisplaySize  size==" + curSize.width + " " + curSize.height);
				if (curSize.width < width && curSize.height < height) {
					size = curSize;
					break;
				}

			}
		}
		return size;
	}

	/**
	 * Call the camera to Auto Focus
	 */
	public void reAutoFocus() {
		if (isSupportAutoFocus) {
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
				}
			});
		}
	}

	/**
	 * 自动聚焦，然后拍照
	 */
	public void takePicture() {
		if (mCamera != null) {
			if (state != State.IDLE)
				return;
			state = State.BUSY;
			mCamera.autoFocus(autoFocusCallback);
		}
	}

	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub

			if (success) {
				Log.i(TAG, "autoFocusCallback: success...");
				takePhoto();
			} else {
				Log.i(TAG, "autoFocusCallback: fail...");
				if (isSupportAutoFocus) {
					takePhoto();
				}
			}
		}
	};

	/**
	 * 调整照相的方向，设置拍照相片的方向
	 */
	private void takePhoto() {
		if (mCamera != null) {
			mCamera.takePicture(shutterCallback, pictureCallback, mPicture);
			//DisableShutterAudio();
		}
	}

	/**
	 * 设置预览方向
	 *
	 * @author zengdengyi
	 * @param degrees
	 * @param cameraId
	 * @return
	 * @date 2015年6月24日 上午9:38:29
	 */
	private int getDisplayOritation(int degrees, int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	private void DisableShutterAudio() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				boolean shutterState = mCamera.enableShutterSound(false);
				Log.i(TAG, "Shutter sound was" + (!shutterState ? "not " : " ") + "disabled");
			} else {
				Log.i(TAG, "Trying to disable shutter sound by altering the system audio manager.");
				// Backward compatibility method for disabling the shutter sound
//				AudioManager audio = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//				currentVolume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
//				audio.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//				MediaPlayer media = MediaPlayer.create(getContext(), R.raw.camera_shutter_click);
//				media.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//				isVolumeChanged = true;
			}

	}

	private PreviewCallback mPreviewCallback = new PreviewCallback() {

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			if(mFaceView!=null)
			mFaceView.clearFaces();
			if(camera==null){
				return;
			}
			Size size = camera.getParameters().getPreviewSize();
			width=width==0?size.width:width;
			height=height==0?size.height:height;
			int mBufferSize = size.width*size.height*3/2;
			if(mBufferForGLRenderer == null){
				mBufferForGLRenderer = ByteBuffer.allocateDirect(mBufferSize);
				mBufferForGLRenderer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
				mBufferForGLRenderer.position(0);
			}
			mBufferForGLRenderer.put(data);
			mBufferForGLRenderer.position(0);
			if(mCallback!=null)
			mCallback.onDetectFace(mBufferForGLRenderer,width,height);
		}

	};

	private ShutterCallback shutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
			//TODO NEED SYSTEM MODIFY TO CANCEL THE SYSTEM SHUTTER AUDIO BY LILIANG
		//	mHandler.sendEmptyMessage(SmartCameraActivity.PLAY_CAMERA_SOUND);
		}
	};

	private PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}
	};
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			new SavePictureTask().execute(data);
			mCamera.stopPreview();// 重新开始预览
			state = State.IDLE;

		}
	};
	private String savaPath;
	private ArcCallBack arcCallBack;

	public class SavePictureTask extends AsyncTask<byte[], String, String> {

		@Override
		protected String doInBackground(byte[]... params) {
			File pictureFile = FileUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE,
					mContext);
			if (pictureFile == null) {
				Toast.makeText(mContext, "请插入存储卡！", Toast.LENGTH_SHORT).show();
				return null;
			}
			FileOutputStream fos = null;
			try {
				 fos = new FileOutputStream(pictureFile);
				savaPath = pictureFile.getAbsolutePath();
				fos.write(params[0]);
				fos.flush();
				fos.close();
				try{
					Thread.sleep(800);
				}catch (Exception e){

				}
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}finally {
				if(fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		public void onPostExecute(String result) {
			//release camera resource
			if (mCamera != null) {
				Log.d(TAG,"camera release");
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
			Intent intent = new Intent(TRANSFER_PHOTO_BY_XMPP_ACTION);
			intent.putExtra(PHOTO_PATH, savaPath);
			mContext.sendBroadcast(intent);
            //Start iflytekmix broadcast
			mHandler.sendEmptyMessage(SmartCameraActivity.SUCCESS_TAKE_PHOTO);
			//release

		}

	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		reAutoFocus();
		return false;
	}

	/**demo*/
	private Handler mHandler;
	public void setHandler(Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;
	}

	public void setFaceView(FaceView mfView) {
		// TODO Auto-generated method stub
		mFaceView=mfView;
	}


	public void setArcCallBack(ArcCallBack arcCallBack) {
		// TODO Auto-generated method stub
		this.arcCallBack = arcCallBack;
	}

}
