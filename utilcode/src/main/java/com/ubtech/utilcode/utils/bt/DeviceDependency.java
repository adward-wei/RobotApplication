package com.ubtech.utilcode.utils.bt;

import android.os.Build;

public class DeviceDependency {
    public static boolean shouldUseSecure() { // isHongmi()
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            if (Build.MODEL.equals("2013022") && Build.VERSION.RELEASE.equals("4.2.1")) {
                return true;
            }
        }

        if (Build.MODEL.equals("Lenovo A820")) {
            return true;
        }
        return false;
    }

    public static boolean shouldUseFixChannel() {
        if (Build.VERSION.RELEASE.startsWith("4.0.")) {
            if (Build.MANUFACTURER.equals("samsung")) {
                return true;
            }
            if (Build.MANUFACTURER.equals("HTC")) {
                return true;
            }
        }

        if (Build.VERSION.RELEASE.startsWith("4.1.")) {
            if (Build.MANUFACTURER.equals("samsung")) {
                return true;
            }
        }
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            if (Build.VERSION.RELEASE.equals("2.3.5")) {
                return true;
            }
        }
        return false;
    }
}
