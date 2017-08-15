/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.jni;

import com.ubtechinc.alpha.utils.SysUtils;

public class LedControl {

    public native static boolean open();
    public native static boolean close();
    public native static boolean ledSetOn(int val);
    public native static boolean ledSetOFF(int val);
    public native static boolean ledSetEye(int rgb,int brightness,int reye,int leye,int on,int off,int total,int state);
    public native static boolean ledSetHead(int rgb,int brightness,int reye,int leye,int on,int off,int total,int state);
    public native static boolean ledSetMouth(int brightness,int on,int off,int total,int state);

    static {
        if(SysUtils.is5Mic()) {
            System.loadLibrary("head_led");
        }
    }
}
