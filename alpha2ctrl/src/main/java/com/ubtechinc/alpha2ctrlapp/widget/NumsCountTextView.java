package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.ubtechinc.alpha2ctrlapp.constants.Constants;


public class NumsCountTextView extends android.support.v7.widget.AppCompatTextView {
	public int nums = 1;
	public NumsCountTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	
	private int getAvailableWidth(){
		int i = getMaxLines()*(Constants.appImageWidth - getPaddingLeft() - getPaddingRight());
      
		return  i;
    }
    public boolean isOverFlowed() {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) 
        	return true;
        return false;
    }

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}
    
}
