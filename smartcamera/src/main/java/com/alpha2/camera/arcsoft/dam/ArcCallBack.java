package com.alpha2.camera.arcsoft.dam;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ArcCallBack {
	private Handler mHandler;
	private RectfVector mRectfVector;

	public ArcCallBack() {
		this.mRectfVector = new RectfVector();
	}

	public void detectionResult(ArcsoftDetectionRectF[] rect) {
		for (int i = 0; i < rect.length; i++) {
			rect[i].calLength();// 计算边长
		}
		sort(rect);
		if (rect[0].getType() == 0) {
			mRectfVector.setmFaceRect(rect);
			Log.d("smartCamera","FACE FACE FACE ");
		} else if (rect[0].getType() == 1) {
			mRectfVector.setmHandRect(rect);
		} else if (rect[0].getType() == 2) {
			mRectfVector.setmHandVRect(rect);
		}
	}

	public void noResult() {
		// 容器中没有数据则清空画面
		if (mRectfVector.getmFaceRect() == null
				&& mRectfVector.getmHandRect() == null
				&& mRectfVector.getmHandVRect() == null) {
			Message m = mHandler.obtainMessage();
			m.what = 1;
			m.sendToTarget();
		} else {
			RectfVector mRect = (RectfVector) mRectfVector.clone();//数据拷贝
			Message m = mHandler.obtainMessage();
			m.what = 0;
			m.obj = mRect;
			m.sendToTarget();
		}
		//清空数据
		mRectfVector.setmFaceRect(null);
		mRectfVector.setmHandRect(null);
		mRectfVector.setmHandVRect(null);
	}

	/**
	 * 将大的
	 * 
	 * @param nums
	 */
	public void sort(ArcsoftDetectionRectF[] nums) {
		// 从此一个位开始循环数组
		for (int i = 0; i < nums.length; i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < nums.length; j++) {
				// 如果前一位比后一位小，那么就将两个数字调换
				// 这里是按降序排列
				// 如果你想按升序排列只要改变符号即可
				if (nums[i].getLength() < nums[j].getLength()) {
					ArcsoftDetectionRectF tem = nums[i];
					nums[i] = nums[j];
					nums[j] = tem;
				}
			}
		}
	}

	public void setFaceHandle(Handler mHandler) {
		// TODO Auto-generated method stub
		this.mHandler = mHandler;
	}
}
