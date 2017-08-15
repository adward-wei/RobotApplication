package com.alpha2.camera.arcsoft.dam;

/**
 * 识别结果容器
 * 
 * @author zengdengyi
 *
 */
public class RectfVector implements Cloneable {
	private ArcsoftDetectionRectF[] mFaceRect;
	private ArcsoftDetectionRectF[] mHandRect;
	private ArcsoftDetectionRectF[] mHandVRect;

	public ArcsoftDetectionRectF[] getmFaceRect() {
		return mFaceRect;
	}

	public void setmFaceRect(ArcsoftDetectionRectF[] mFaceRect) {
		this.mFaceRect = mFaceRect;
	}

	public ArcsoftDetectionRectF[] getmHandRect() {
		return mHandRect;
	}


	public void setmHandRect(ArcsoftDetectionRectF[] mHandRect) {
		this.mHandRect = mHandRect;
	}

	public ArcsoftDetectionRectF[] getmHandVRect() {
		return mHandVRect;
	}

	public void setmHandVRect(ArcsoftDetectionRectF[] mHandVRect) {
		this.mHandVRect = mHandVRect;
	}

	@Override
	public Object clone() {
		// 克隆Employee对象并手动的进一步克隆Employee对象中包含的Employer对象
		RectfVector mRectfVector=null;
		try {
			mRectfVector = (RectfVector) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mRectfVector;

	}
}
