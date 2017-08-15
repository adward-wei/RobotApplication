package com.ubt.alpha2.upgrade.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ubt on 2017/6/28.
 */

public class WifiStateInfo {

    public static final int IDLE = 0;
    public static final int CONNECTING =1;
    public static final int CONNECTED = 2;
    public static final int DISCONNECTED = 3;

    @IntDef({IDLE,CONNECTING,CONNECTED,DISCONNECTED})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WifiState{}
}
