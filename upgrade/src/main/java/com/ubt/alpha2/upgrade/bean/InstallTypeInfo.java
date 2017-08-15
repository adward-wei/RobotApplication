package com.ubt.alpha2.upgrade.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: slive
 * @description: 
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class InstallTypeInfo {
    //0 代表升级apk  1 代表安装备份apk 2 代表安装自己
    public static final int INSTALL_COMMON_APK = 0;
    public static final int INSTALL_BACKUP_APK = 1;
    public static final int INSTALL_SELF = 2;

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({INSTALL_COMMON_APK,INSTALL_BACKUP_APK,INSTALL_SELF})
    public @interface Type{}


}
