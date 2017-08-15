package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义gridview，解决scrollview中嵌套gridview显示不正常的问题（1行半）
 * 
 * @author wangyx
 */
public class NoScrollListView extends ListView {
	public NoScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE){
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
