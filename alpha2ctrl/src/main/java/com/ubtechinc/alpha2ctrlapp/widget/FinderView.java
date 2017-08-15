/**
 *  2014-7-15   上午11:14:21
 *  Created By niexiaoqiang
 */

package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.ubtechinc.alpha2ctrlapp.R;


public class FinderView extends View {
	private static final long ANIMATION_DELAY = 30;
	private Paint finderMaskPaint;
	private int measureedWidth;
	private int measureedHeight;

	public FinderView(Context context) {
		super(context);
		init(context);
	}

	public FinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(leftRect, finderMaskPaint);
		canvas.drawRect(topRect, finderMaskPaint);
		canvas.drawRect(rightRect, finderMaskPaint);
		canvas.drawRect(bottomRect, finderMaskPaint);
		//画框
		zx_code_kuang.setBounds(middleRect);
		zx_code_kuang.draw(canvas);
		if (lineRect.bottom < middleRect.bottom) {
			zx_code_line.setBounds(lineRect);
			lineRect.top = lineRect.top + lineHeight / 2;
			lineRect.bottom = lineRect.bottom + lineHeight / 2;
		} else {
			lineRect.set(middleRect);
			lineRect.bottom = lineRect.top + lineHeight;
			zx_code_line.setBounds(lineRect);
		}
		zx_code_line.draw(canvas);
		postInvalidateDelayed(ANIMATION_DELAY, middleRect.left, middleRect.top, middleRect.right, middleRect.bottom);
	}

	private Rect topRect = new Rect();
	private Rect bottomRect = new Rect();
	private Rect rightRect = new Rect();
	private Rect leftRect = new Rect();
	private Rect middleRect = new Rect();

	private Rect lineRect = new Rect();
	private Drawable zx_code_kuang;
	private Drawable zx_code_line;
	private int lineHeight;

	private void init(Context context) {
		int finder_mask = context.getResources().getColor(R.color.finder_mask);
		finderMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		finderMaskPaint.setColor(finder_mask);
		zx_code_kuang = context.getResources().getDrawable(R.drawable.zx_code_kuang);
		zx_code_line = context.getResources().getDrawable(R.color.white);
		lineHeight = 5;
	}

	//////////////新增该方法//////////////////////
	/**
	 * 根据图片size求出矩形框在图片所在位置，tip：相机旋转90度以后，拍摄的图片是横着的，所有传递参数时，做了交换
	 * @param w
	 * @param h
	 * @return
	 */
	public Rect getScanImageRect(int w, int h) {
		//先求出实际矩形
		Rect rect = new Rect();
		rect.left = middleRect.left;
		rect.right = middleRect.right;
		float temp = h / (float) measureedHeight;
		rect.top = (int) (middleRect.top * temp);
		rect.bottom = (int) (middleRect.bottom * temp);
		return rect;
	}

	////////////////////////////////////
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
		measureedHeight = MeasureSpec.getSize(heightMeasureSpec);
		int borderWidth = measureedWidth / 2 + 100;
		middleRect.set((measureedWidth - borderWidth) / 2, (measureedHeight - borderWidth) / 2, (measureedWidth - borderWidth) / 2 + borderWidth, (measureedHeight - borderWidth) / 2 + borderWidth);
		lineRect.set(middleRect);
		lineRect.bottom = lineRect.top + lineHeight;
		leftRect.set(0, middleRect.top, middleRect.left, middleRect.bottom);
		topRect.set(0, 0, measureedWidth, middleRect.top);
		rightRect.set(middleRect.right, middleRect.top, measureedWidth, middleRect.bottom);
		bottomRect.set(0, middleRect.bottom, measureedWidth, measureedHeight);
	}
}
