package com.ubtechinc.alpha.business;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.download.BusinessConstant;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ubtechinc.alpha.utils.PackageUtils.getInstalledApps;

/**
 * @author：tanghongyu
 * @date：6/28/2017 8:55 PM
 * @modifier：tanghongyu
 * @modify_date：6/28/2017 8:55 PM
 * [A brief description]
 * version
 */

public class StorageBusiness {
    private static final String TAG = "StorageBusiness";
    /**
     * @param
     * @return
     * @throws
     * @Description 获取所有已安装的应用，不包含闲聊和主服务
     */
    private List<AppPackageSimpleInfo> mCustomApps = new ArrayList<AppPackageSimpleInfo>();
    private List<PackageInfo> mAllApps;
    private IGetPkgCallback iGetPkgCallback;
    private Map<String, AppPackageSimpleInfo> mAppMap = new HashMap<>();

    public void getAllInstalledApps(Context context, IGetPkgCallback iGetPkgCallback) {

        this.iGetPkgCallback = iGetPkgCallback;
        mAllApps = getInstalledApps(context);
        PackageManager pm = context.getPackageManager();
        try {
            Method getPackageSizeInfo = pm.getClass().getMethod(
                    "getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);

            for (PackageInfo packageInfo : mAllApps) {
                if (((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                        && ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0)
                        && !isProtectedApp(packageInfo.packageName)) // 系统应用和保护应用不可卸载
                {

                    AppPackageSimpleInfo bean = new AppPackageSimpleInfo();
                    bean.setPackageName(packageInfo.packageName);
                    bean.setName(packageInfo.applicationInfo.loadLabel(
                            context.getPackageManager()).toString());
                    Drawable drawable = packageInfo.applicationInfo
                            .loadIcon(context.getPackageManager());
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bm = bd.getBitmap();

                    byte[] icon = ConvertUtils.bitmap2Bytes(bm, Bitmap.CompressFormat.PNG);

                    bean.setIcon(icon);
                    mAppMap.put(packageInfo.packageName, bean);
                }

            }
            if (mAppMap.size() == 0) {
                iGetPkgCallback.onGetPkgInfo(mCustomApps);
            } else {
                //遍历查询包
                for (String pkgName : mAppMap.keySet()) {
                    getPackageSizeInfo(getPackageSizeInfo, pkgName, pm);
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public void getPackageSizeInfo(Method getPackageSizeInfo, String pkg, PackageManager pm) {
        LogUtils.d(TAG, "run getPackageSizeInfo ");

        try {

            getPackageSizeInfo.invoke(pm, pkg, new PkgSizeObserver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isProtectedApp(String packageName) {

        switch (packageName) {
            case BusinessConstant.PACKAGENAME_CH_CHAT:
                return true;
            case BusinessConstant.PACKAGENAME_EN_CHAT:
                return true;
            case BusinessConstant.MAIN_SERVICE:
                return true;
        }
        return false;
    }

    public class AppPackageSimpleInfo {
        private String packageName;
        private String name;
        private byte[] icon;
        /**
         * App大小
         **/
        private long appSize;

        public long getAppSize() {
            return appSize;
        }

        public void setAppSize(long appSize) {
            this.appSize = appSize;
        }


        public byte[] getIcon() {
            return icon;
        }

        public void setIcon(byte[] icon) {
            this.icon = icon;
        }


        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AppEntrityInfo{" +
                    "packageName='" + packageName + '\'' +
                    ", name='" + name + '\'' +
                    ", appSize=" + appSize +

                    '}';
        }
    }

    class PkgSizeObserver extends IPackageStatsObserver.Stub {
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {

            AppPackageSimpleInfo entrityInfo = mAppMap.get(pStats.packageName);
            LogUtils.d(TAG, "run onGetStatsCompleted get packageName = " + pStats.packageName);
            if (pStats != null) {
                entrityInfo.setAppSize(pStats.codeSize);
            }
            LogUtils.d(TAG, entrityInfo.toString());
            mCustomApps.add(entrityInfo);
            //直到取到所有的App再回调
            if (mAllApps != null && mCustomApps != null && (mAppMap.size() == mCustomApps.size())) {
                LogUtils.d(TAG, "callback onGetPkgInfo mCustomApps = " + mCustomApps);
                iGetPkgCallback.onGetPkgInfo(mCustomApps);
            }
        }
    }

    public interface IGetPkgCallback<T> {
        void onGetPkgInfo(T AppEntrityInfo);
    }
}
