package com.ubtechinc.alpha2ctrlapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * @author：ubt
 * @date：2017/3/21 14:58
 * @modifier：ubt
 * @modify_date：2017/3/21 14:58
 * [A brief description]
 * version
 */

public class UlitStatuBar {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    Context mContext;

    /**
     * 关联要监听的视图
     *
     * @param viewObserving
     */
    public static void assistActivity(Context context, View viewObserving) {
        new UlitStatuBar(context, viewObserving);
    }

    private View mViewObserved;//被监听的视图
    private int usableHeightPrevious;//视图变化前的可用高度
    private ViewGroup.LayoutParams frameLayoutParams;

    private UlitStatuBar(Context context, View viewObserving) {
        this.mContext = context;

        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });
        frameLayoutParams = mViewObserved.getLayoutParams();
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        Logger.i("height: usableHeightNow==" + usableHeightNow);
        Logger.i("height: usableHeightPrevious==" + usableHeightPrevious);
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
             frameLayoutParams.height = usableHeightNow - 40;
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        Logger.i("height: r.bottom==" + r.bottom);

        Logger.i("height: r.top==" + r.top);
         return (r.bottom - r.top);
    }

    /**
     * 获取虚拟按键高度
     *
     * @param context
     * @return
     */
    public int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 判断有没有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w("", e);
        }

        return hasNavigationBar;

    }

}
