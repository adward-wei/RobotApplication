package com.ubtechinc.zh_chat.ui.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ubtechinc.zh_chat.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	int setFixPictureWidth = 1600;
	/**
	 * 设置当前最适合的图片高度
	 */
	int setFixPictureHeight = 1200;

	/**
	 * 创建的时候自动调用该方法
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera == null) {
			mCamera = CameraCheck.getCameraInstance(mContext);
		}
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
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
		try {
			setCameraParms();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			reAutoFocus();
		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
	private void setCameraParms() {
		Camera.Parameters myParam = mCamera.getParameters();

		List<Size> mSupportedsizeList = myParam
				.getSupportedPictureSizes();

		// 处理有一些手机上不支持直接 myParam.setPreviewSize(800, 600);
		Size optimalSize = getPreviewDisplaySize(mSupportedsizeList, 800, 600);
		myParam.setPreviewSize(optimalSize.width, optimalSize.height);

		myParam.setJpegQuality(100);
		myParam.setPreviewFormat(ImageFormat.NV21);


		if (Build.VERSION.RELEASE.equals("5.1.1")) {
			myParam.setRotation(0);  //rockchip
			myParam.set("rotation", 0);
		} else {
			myParam.setRotation(180);  //samsung
			myParam.set("rotation", 180);
			mCamera.setDisplayOrientation(180);
		}
		if(Build.VERSION.RELEASE.equals("5.1.1"))
		{
			myParam.setPictureSize(1600, 1200);
		} else {
			myParam.setPictureSize(setFixPictureWidth,
					setFixPictureHeight);
		}
		mCamera.setParameters(myParam);
		if (myParam.getMaxNumDetectedFaces() > 0) {
			// mCamera.startFaceDetection();
		}
	}
	public Size getPreviewDisplaySize(List<Size> mSupportedsizeList, int width,
			int height) {
		Size size = null;
		if (mSupportedsizeList.size() > 1) {
			Iterator<Size> itos = mSupportedsizeList.iterator();
			while (itos.hasNext()) {
				Size curSize = itos.next();

				//Log.e("zdy", "size==" + curSize.width + " " + curSize.height);
				if (curSize.width < width && curSize.height < height) {
					size = curSize;
					break;
				}

			}
		}
		return size;
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
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
//第二条语义进来，导致无法往下执行，activity没关掉
/*			if (state != State.IDLE)
				return;*/
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
				} else {
					state = State.IDLE;
					cameraActivity.finish();
				}
			}
		}
	};

	/**
	 * 调整照相的方向，设置拍照相片的方向
	 */
	private void takePhoto() {
		state = State.IDLE;
		if (mCamera != null) {
			mCamera.takePicture(shutterCallback, pictureCallback, mPicture);
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

	private ShutterCallback shutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
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
			mCamera.startPreview();// 重新开始预览
			state = State.IDLE;

		}
	};
	private CameraActivity cameraActivity;
	private String savaPath;

	public class SavePictureTask extends AsyncTask<byte[], String, String> {
		@SuppressLint("SimpleDateFormat")
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
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			} finally {
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
			cameraActivity.back(savaPath);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		reAutoFocus();
		return false;
	}

	public void setHandler(CameraActivity cameraActivity) {
		// TODO Auto-generated method stub
		this.cameraActivity = cameraActivity;
	}

}
