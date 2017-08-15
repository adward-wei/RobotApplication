package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha2ctrlapp.R;

/**
 * TODO: document your custom view class.
 */
public class BottomScrollLayout extends RelativeLayout {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;
    private Context mContext;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    /**
     * 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
     */
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;

    /**
     * 往左边滑动的最小距离，大于这个值时显示悬浮按钮
     */
    private static final int MIN_SCROLL_LEGTH = 60;

    public BottomScrollLayout(Context context) {
        super(context);
        mContext = context;

        init(null, 0);
    }

    public BottomScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public BottomScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BottomScrollLayout, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.BottomScrollLayout_exampleString);
        mExampleColor = a.getColor(
                R.styleable.BottomScrollLayout_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.BottomScrollLayout_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.BottomScrollLayout_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.BottomScrollLayout_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的dispatchTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            Logger.d("touchevent: y1==" + y1 + "  y2===" + y2 + "  y1-y2==" + (y1 - y2));
            if (y1 - y2 > MIN_SCROLL_LEGTH) {
             } else if (y2 - y1 > MIN_SCROLL_LEGTH) {
             } else if (x1 - x2 > 30) {
                //ivLeftFolat.setImageResource(R.drawable.ic_floating_close);
                 return true;
            } else if (x2 - x1 > MIN_SCROLL_LEGTH) {
                //  ivLeftFolat.setVisibility(View.VISIBLE);
                //  ivLeftFolat.setImageResource(R.drawable.ic_floating_open);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
