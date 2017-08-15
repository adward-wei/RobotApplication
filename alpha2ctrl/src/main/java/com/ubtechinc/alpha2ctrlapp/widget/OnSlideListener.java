package com.ubtechinc.alpha2ctrlapp.widget;

import android.widget.ViewSwitcher;

public interface OnSlideListener {
	public void getNext(ViewSwitcher _switcher, String[] _img_ids);

	public void getPrev(ViewSwitcher _switcher, String[] _img_ids);
}
