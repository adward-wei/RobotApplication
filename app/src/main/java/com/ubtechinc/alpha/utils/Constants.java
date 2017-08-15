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

package com.ubtechinc.alpha.utils;

import android.os.Environment;

/**
 * @desc : 常量
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/20
 * @modifier:
 * @modify_time:
 */

public final class Constants {

    //sys
    public static final String ALPHA_MIC_HARDWARE_VERSION = "ro.hardware.version";
    public static final String ROBOT_SYSTEM_VERSION = "ro.build.description";//[ro.build.description]
    public static final String MIC5_VERSION = "alpha2_10005";
    public static final String MIC2_VERSION = "alpha2_10002";
    public static final String LYNX_SYSTEM_VERSION= "Lynx";
    //动作文件存储路径
    public static String ACTION_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/actions";

    //binder name
    public static final String ACTION_BINDER_NAME="action";
    public static final String SPEECH_BINDER_NAME="speech";
    public static final String LED_BINDER_NAME="led";
    public static final String MOTOR_BINDER_NAME="motor";
    public static final String SYSINFO_BINDER_NAME = "sysinfo";

    //shareperferences key
    public static  final  String MASTER_NAME = "MASTER_NAME";

}
