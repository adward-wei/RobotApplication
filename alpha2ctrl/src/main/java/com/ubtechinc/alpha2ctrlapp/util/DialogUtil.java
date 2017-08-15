package com.ubtechinc.alpha2ctrlapp.util;

import android.app.Activity;
import android.content.res.Resources;
import android.view.Window;
import android.view.WindowManager;

/**
 * 根据设备的屏幕比例设置对话框的宽高 ，竖屏时：宽为屏幕宽度，高为屏幕高度的三分之二 ；横屏时：宽为屏幕宽度的三分之二，高为屏幕高度的三分之二。
 * 
 * @author guofeng
 * 
 */
public class DialogUtil {
	public static void setDialogSize(Activity activity) {
		// 屏幕宽高
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = activity.getResources();


		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
		   screenHeight =screenHeight -resources.getDimensionPixelSize(resourceId);//减去底部操作栏的高度
		}
		// 设置窗口的大小
		Window window = activity.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		if (screenWidth < screenHeight) {
			layoutParams.width = (int) (screenWidth * (7.0 / 10.0));
			layoutParams.height = (int) (screenHeight * (7.0 / 10.0));
		} else {
			layoutParams.width = (int) (screenWidth * (4.0 / 5.0));
			layoutParams.height = (int) (screenHeight * (3.0 / 4.0));
		}
		window.setAttributes(layoutParams);
	}
	public static void setDialogHalfSize(Activity activity) {
		// 屏幕宽高
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		// 设置窗口的大小
		Window window = activity.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
		   screenHeight =screenHeight -resources.getDimensionPixelSize(resourceId);//减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			layoutParams.width = (int) (screenWidth * (7.5 / 10.0));
			layoutParams.height = (int) (screenHeight * (5.6 /7.0));
		} else {
			layoutParams.width = (int) (screenWidth * (4.0 / 5.0));
			layoutParams.height = (int) (screenHeight * (3.0 / 4.0));
		}
		window.setAttributes(layoutParams);
	}
	public static void setDialogSize2(Activity activity) {
		// 屏幕宽高
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
		   screenHeight =screenHeight -resources.getDimensionPixelSize(resourceId);//减去底部操作栏的高度
		}
		// 设置窗口的大小
		Window window = activity.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		if (screenWidth < screenHeight) {
			layoutParams.width = (int) (screenWidth * (4.0 /5.0));
			layoutParams.height = (int) (screenHeight * (9.0 / 10.0));
		} else {
			layoutParams.width = (int) (screenWidth * (4.0 / 5.0));
			layoutParams.height = (int) (screenHeight * (3.0 / 4.0));
		}
		window.setAttributes(layoutParams);
	}
	public static void setDialogFourSize(Activity activity) {
		// 屏幕宽高
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		// 设置窗口的大小
		Window window = activity.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
		   screenHeight =screenHeight -resources.getDimensionPixelSize(resourceId);//减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			layoutParams.width = (int) (screenWidth * (8.0 / 10.0));
			layoutParams.height = (int) (screenHeight * (5.0 /10.0));
		} else {
			layoutParams.width = (int) (screenWidth * (4.0 / 5.0));
			layoutParams.height = (int) (screenHeight * (3.0 / 4.0));
		}
		window.setAttributes(layoutParams);
	}
	public static void setDialogToHalfSize(Activity activity) {
		// 屏幕宽高
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		// 设置窗口的大小
		Window window = activity.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			screenHeight =screenHeight -resources.getDimensionPixelSize(resourceId);//减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			layoutParams.width = (int) (screenWidth * (2.0 / 3.0));
			layoutParams.height = (int) (screenHeight * (1.0 /3.0));
		} else {
			layoutParams.width = (int) (screenWidth * (1.0 / 3.0));
			layoutParams.height = (int) (screenHeight * (2.0 / 3.0));
		}

		window.setAttributes(layoutParams);
	}
}
