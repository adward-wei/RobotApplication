package com.ubtechinc.alpha2ctrlapp.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liuliaopu on 2016/11/5.
 */

public class LoadingImageView extends ImageView{
    private int mBgColor = 0xff9ca1b5;//设置圆环颜色
    private int mFgColor = 0xff317add;//设置进度颜色

    private float mRadiusRatio = 0.9f;//设置圆环半径占整个半径的比例
    private float mRingWidthRatio = 0.05f;//设置圆环宽度占整个的比例

    private float mProgress = 0.0f;//进度，0.0~1.0

    private boolean mIsInAnimation;//是否是在动画进行中
    private final Paint mPaint;

    private OnAnimationListener mAnimationListener;

    public LoadingImageView(Context context) {
        this(context, null, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除据此
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mIsInAnimation) {
            int width = getWidth();
            int height = getHeight();

            int d = Math.min(width, height);
            int radius = (int)(d/2*mRadiusRatio);
            int ringWidth = Math.max((int)(mRingWidthRatio*d), 5);

            int centerX = width/2;
            int centerY = height/2;

            mPaint.setStrokeWidth(ringWidth);
            mPaint.setColor(mBgColor);
            canvas.drawCircle(centerX, centerY, radius, mPaint);

            mPaint.setColor(mFgColor);
            RectF oval = new RectF(centerX - radius, centerY - radius,
                    centerX + radius, centerY + radius);
            canvas.drawArc(oval, -90, 360*mProgress, false, mPaint);
        }
    }

    /**
     * 设置动画监听对象
     * */
    public void setAnimateListener(OnAnimationListener listener) {
        mAnimationListener = listener;
    }

    public void startAnimation(int time) {
        if(mIsInAnimation) {
            return;
        }

        mProgress = 0;
        mIsInAnimation = true;
        ValueAnimator animator = ValueAnimator.ofInt(0,1).setDuration(time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float fraction = animator.getAnimatedFraction();
                mProgress = fraction;

                postInvalidate();

                if (fraction >= 1) {
                    mIsInAnimation = false;
                    mProgress = 0;
                    postInvalidate();
                    if(mAnimationListener != null) {
                        mAnimationListener.onAnimationEnd();
                    }
                }
            }
        });
        animator.start();
    }

    /**
     * 动画结束监听接口
     * */
    public interface OnAnimationListener {
        void onAnimationEnd();
    }
}
