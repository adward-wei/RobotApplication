package com.alpha2.camera.arcsoft.dam;

import android.util.Log;

import com.alpha2.camera.ui.StateValue;

/**
 * 
 * @author zengdengyi
 * 
 */
public class ArcsoftDetectionRectF {

	private int left;
	private int top;
	private int right;
	private int bottom;
	private int type;// 类型 0 face 1hand 2 vhand
	private int length;

	public void calLength() {
		length = Math.abs(this.left - this.right);
	}

	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	private int width;
	private int height;

	/**
	 * 处理纵横比 ---裸数据是横屏的 则需要转为竖屏的
	 */
	public void proAspectRatio() {

		Log.e("ArcsoftDetectionRectF", "" + left + " " + top + " " + right
				+ " " + bottom + " ");

		width = Math.abs(this.left - this.right);
		height = Math.abs(this.top - this.bottom);
		int temp = this.left;
		this.left = Math.abs((StateValue.previewHeight - this.top - width));
		this.top = temp;

		temp = this.right;
		this.right = Math.abs(StateValue.previewHeight - this.bottom + width);
		this.bottom = temp;

		float scaleWidth = StateValue.windowWidth / StateValue.previewHeight;
		float scaleHeight = StateValue.windownHeight / StateValue.previewWidth;

		Log.e("ArcsoftDetectionRectF", "" + left + " " + top + " " + right
				+ " " + bottom + " ");

	}

	/**
	 * 机器人需要改变坐标变换
	 */
	public void rotationAngle() {
		this.left = Math.abs((StateValue.previewWidth - this.left));
		this.top = Math.abs((StateValue.previewHeight - this.top));
		this.right = Math.abs((StateValue.previewWidth - this.right));
		this.bottom = Math.abs((StateValue.previewHeight - this.bottom));
		int temp = this.left;
		this.left = this.right;
		this.right = temp;

		temp = this.top;
		this.top = this.bottom;
		this.bottom = temp;

		//
	}
}
