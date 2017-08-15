package com.ubt.alpha2.upgrade.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by ubt on 2017/6/27.
 */

public class FilePathUtils {
    private static String SDARD_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    //app-upgrade
    public static final String APP_PATH = SDARD_DIR + "/ubtech/alpha2/app";
    //action-upgrade
    public static final String ACTION_PATH = SDARD_DIR + "/actions";
    public static final String VERSION_PATH = SDARD_DIR + "/ubtech/alpha2/version.config";

    //mainService-upgrade
    public static final String MAINSERVICE_APK = "MainServiceSilence.apk";
    public static final String NEW_PATH = APP_PATH + "/new";
    public static final String OLD_PATH = APP_PATH + "/old";
    public static final String SELF_PATH = APP_PATH + "/self";
    public static final String LOCAL_CONFIG_PATH = SDARD_DIR+ "/actions/version.json";
    public static final String NEW_CONFIG_PATH = NEW_PATH + "/version.json";

    // self-upgrade
    public static final String DOWN_APK_ZIP_NAME = "downapk.zip";
    public static final String DOWN_SELF_APK_NAME = "selfapk.apk";

    // androidOS-upgrade
    public static final String DOWN_ANDROID_FOLD = SDARD_DIR + File.separator+"ubt/android";
    public static final String DOWN_ANDROID_NAME = "update.zip";
    public static final String DOWN_ANDROID_PATH = DOWN_ANDROID_FOLD+File.separator+DOWN_ANDROID_NAME;

    // Embedded-upgrade
    public static final String DOWN_EMBEDDED_PATH = SDARD_DIR + File.separator+"ubt/embedded";


    public static String DOWN_APK_ZIP_PATH= NEW_PATH + File.separator +DOWN_APK_ZIP_NAME;

    public static String DOWN_SELF_APK_PATH = SELF_PATH + File.separator + DOWN_SELF_APK_NAME;



}
