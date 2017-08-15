package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ubtech.utilcode.utils.SizeUtils;


/**
 * Created by ubt on 2017/1/23.
 */

public class TextImageView extends android.support.v7.widget.AppCompatImageView{
    /**文本*/
    private String mText;
    /**文本颜色*/
    private int mTextColor;
    /**文本大小*/
    private int mTextSize;

    private Paint mPaint;

    public TextImageView(Context context) {
        this(context, null);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
    }

    /**
     * 设置文本内容
     * @param text 文本
     * */
    public void setText(String text) {
        mText = text;
    }

    /**
     * 设置文本颜色
     * @param textColor 颜色
     * */
    public void setTextColor(int textColor) {
        mTextColor = textColor;

        mPaint.setColor(textColor);
    }

    /**
     * 设置文本大小
     * @param textSize 文本大小
     * */
    public void setTextSize(int textSize) {
        mTextSize = textSize;

        mPaint.setTextSize(textSize);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!TextUtils.isEmpty(mText)) {
            int width = getWidth();
            int height = getHeight();

            float posX = (width - SizeUtils.getFontLength(mPaint, mText))/2;
            float poxY = (height - SizeUtils.getFontHeight(mPaint))/2 + SizeUtils.getFontLeading(mPaint);

            canvas.drawText(mText, posX, poxY, mPaint);
        }
    }
}
