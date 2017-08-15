package com.alpha2.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

public class CameraCheck {

	public static boolean CheckCamera(Context mContext) {

		if (mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)||mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			return true;

		} else {
			//Toast.makeText(mContext, "相机不存在！", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(Context mContext) {
		Camera c = null;
		if (CheckCamera(mContext)) {
			try {
				c=android.hardware.Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

			} catch (Exception e) {
				c = null;
			}
		}
		return c; // returns null if camera is unavailable
	}
}
