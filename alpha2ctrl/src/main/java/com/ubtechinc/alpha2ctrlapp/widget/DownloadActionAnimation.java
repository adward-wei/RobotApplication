package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.ubtechinc.alpha2ctrlapp.R;

public class DownloadActionAnimation extends Animation {
	  public static Animation getDownloadActionAnimation(Context context){
		   Animation a=   AnimationUtils.loadAnimation(context, R.anim.turn_around_anim);
		   a.setInterpolator(new LinearInterpolator());
		   a.setInterpolator(new LinearInterpolator());
		   return a;
	  }
	public static Animation getPlayActionAnimation(Context context){
		Animation a=   AnimationUtils.loadAnimation(context, R.anim.play_start);
		a.setInterpolator(new LinearInterpolator());
		a.setInterpolator(new LinearInterpolator());
		return a;
	}
}
