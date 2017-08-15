package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ubtech.utilcode.utils.SizeUtils;


/**
 * @author：liuhai
 * @date：2017/5/3 10:32
 * @modifier：ubt
 * @modify_date：2017/5/3 10:32
 * [A brief description]
 * version
 */

public class MyBottomRelativeLayout extends RelativeLayout {

    public MyBottomRelativeLayout(Context context) {
        this(context, null);
    }

    public MyBottomRelativeLayout(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyBottomRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initView() {

    }

    private boolean mScrolling;
    private float touchDownX;
    private float touchDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(
//                        getContext()).getScaledTouchSlop()) {
//                    mScrolling = true;
//                } else {
//                    mScrolling = false;
//                }
                if (touchDownY - event.getY() >= 60) {
                    mScrolling = true;
                } else if (event.getY() - touchDownY >= 60) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (touchDownX - x2 > SizeUtils.dp2px( 40)) {

                }
                if (touchDownX - x2 < -SizeUtils.dp2px(  40)) {

                }

                if (touchDownY - y2 > SizeUtils.dp2px(  60)) {
                    if (mSetOnSlideListener != null) {
                        mSetOnSlideListener.onBottomToTop();
                    }
                }

                if (touchDownY - y2 < -SizeUtils.dp2px(  60)) {
                    if (mSetOnSlideListener != null) {
                        mSetOnSlideListener.onTopToBottom();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private setOnSlideListener mSetOnSlideListener;

    public setOnSlideListener getmSetOnSlideListener() {
        return mSetOnSlideListener;
    }

    public void setmSetOnSlideListener(setOnSlideListener mSetOnSlideListener) {
        this.mSetOnSlideListener = mSetOnSlideListener;
    }

    public interface setOnSlideListener {


        void onBottomToTop();

        void onTopToBottom();
    }

}
