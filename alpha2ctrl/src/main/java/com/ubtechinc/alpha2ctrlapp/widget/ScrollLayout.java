package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

public class ScrollLayout extends RelativeLayout {

	private Scroller mScroller = null;
	private boolean isScrolled = false;
	private float dx = -1;

	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {

			scrollTo(mScroller.getCurrX(), 0);

			postInvalidate();
		}
	}

	public void beginScrollX(float _dx, int _duration,
			final IScrroListener _listener) {
		if (!isScrolled) {
			isScrolled = true;
			dx = _dx;
			mScroller.startScroll(0, 0, (int) dx, 0, _duration);
			if (_listener != null)
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						_listener.onScrroFinish();
					}
				}, _duration);

		}
		invalidate();
	}

	public void goBack(int _duration, final IScrroListener _listener) {
		if (isScrolled) {
			isScrolled = false;
			mScroller.startScroll(0, 0, 0, 0, _duration);
			if (_listener != null)
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						_listener.onScrroFinish();
					}
				}, _duration);

		}
		invalidate();
	}
	public interface IScrroListener {
		public void onScrroFinish();
	}
}
