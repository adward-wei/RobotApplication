package com.ubtechinc.zh_chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.ubtechinc.alpha.sdk.SdkConstants;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/1
 * @modifier:
 * @modify_time:
 */

public final class Utils {

    public static String getMasterName(Context mContext){
        Context useCount;
        try {
            useCount = mContext.createPackageContext(SdkConstants.ALPHA_PACKAGE_NAME,
                    Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences prefs = useCount.getSharedPreferences("DefaultMasterName",
                    Context.MODE_MULTI_PROCESS |Context.MODE_WORLD_WRITEABLE|Context.MODE_WORLD_READABLE);
            return prefs.getString("sMasterName","主人");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
