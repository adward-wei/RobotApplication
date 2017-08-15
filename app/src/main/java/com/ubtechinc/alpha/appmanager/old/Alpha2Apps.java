package com.ubtechinc.alpha.appmanager.old;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.download.AppEntrityInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc : 旧的app管理器
 * @author: wzt
 * @time : 2017/5/24
 * @modifier:
 * @modify_time:
 */

public class Alpha2Apps {
    private Context context;
    private static final String TAG = "Alpha2Apps";
    public static Alpha2Apps instance;
    public static Alpha2Apps get(Context context){
       if(instance == null){
           synchronized(Alpha2Apps.class){
               if(instance ==null){
                instance =new Alpha2Apps(context);
               }
           }

       }
        return instance;
    }
    public Alpha2Apps(Context context) {
        this.context = context;
    }
    /**
     * 获取当前已安装app列表
     *
     * @return
     * @author zengdengyi
     */
    private List<PackageInfo> getAllApps() {

        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();

        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            apps.add(pak);
        }
        LogUtils.i(TAG, "run getAllApps apps.size = " + apps.size());
        return apps;
    }

//    private boolean isProtectedApp(String packageName) {
//
//        switch (packageName) {
//            case Alpha2Application.CHINESE_CHAT:
//                return true;
//            case Alpha2Application.ENGLISH_CHAT:
//                return true;
//            case Alpha2Application.MAIN_SERVICE:
//                return true;
//        }
//        return false;
//    }
//    private List<AppPackageInfo> mCustomApps = new ArrayList<AppPackageInfo>();
//    private List<PackageInfo> mAllApps;
//    private IGetPkgCallback iGetPkgCallback;
//    private Map<String, AppPackageInfo> mAppMap = new HashMap<>();

    /**
     * @param
     * @return
     * @throws
     * @Description 获取所有已安装的应用，不包含闲聊和主服务
     */
    public void getAllInstalledApps(IGetPkgCallback iGetPkgCallback) {

//        this.iGetPkgCallback = iGetPkgCallback;
//         mAllApps = getAllApps();
//        PackageManager pm = this.context.getPackageManager();
//        try {
//            Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
//
//            for (PackageInfo packageInfo : mAllApps) {
//
//                if (((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) &&( (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) && !isProtectedApp(packageInfo.packageName)){ // 系统应用和保护应用不可卸载
//                    AppPackageInfo bean = new AppPackageInfo();
//                    bean.setPackageName(packageInfo.packageName);
//                    bean.setName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
//                    bean.setVersionCode("" + packageInfo.versionCode);
//                    bean.setVersionName(packageInfo.versionName);
//                    Drawable drawable = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
//                    BitmapDrawable bd = (BitmapDrawable) drawable;
//                    Bitmap bm = bd.getBitmap();
//                    byte[] icon = Bitmap2Bytes(bm);
//                    // saveMyBitmap("test", bm);
//                    bean.setIcon(icon);
//                    LogUtils.i( " can install app = " + packageInfo.packageName + " applicationDir = "+ packageInfo.applicationInfo.sourceDir);
//                    mAppMap.put(packageInfo.packageName, bean);
//                }
//
//            }
//            if(mAppMap.size() == 0) {
//                iGetPkgCallback.onGetPkgInfo(mCustomApps);
//            }else {
//                //遍历查询包
//                for(String pkgName : mAppMap.keySet()) {
//                    getPackageSizeInfo(getPackageSizeInfo,pkgName, pm);
//                }
//            }
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }



    /**
     * 获取Alpha2已安装使用了SDK的第三方App列表详细信息
     *
     * @param allApps
     * @return
     * @author zengdengyi
     * @date
     */
    private List<PackageInfo> getAlpha2Apps(List<PackageInfo> allApps) {
        List<PackageInfo> mAppList = new ArrayList<PackageInfo>();
        for (int i = 0; i < allApps.size(); i++) {

            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager().getApplicationInfo(
                        allApps.get(i).packageName,
                        PackageManager.GET_META_DATA);
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (appInfo == null)
                continue;
            Bundle meta = appInfo.metaData;
            if (meta == null)
                continue;
            String appid = meta.getString("alpha2_appid");
            if (appid != null) {
                mAppList.add(allApps.get(i));
                Log.i("alphaApp", "packageName: " + allApps.get(i).packageName + " appid = " + appid);
            }
        }
        return mAppList;
    }

    public List<PackageInfo> getAlpha2Apps() {
        return getAlpha2Apps(getAllApps());
    }

    public List<AppEntrityInfo> getAppEntityInfo() {

        List<PackageInfo> allApps = getAllApps();
        List<AppEntrityInfo> mAppList = new ArrayList<AppEntrityInfo>();
        //
        AppEntrityInfo beanDefault = new AppEntrityInfo();
        beanDefault.setPackageName("com.ubtech.actionmodel");
        beanDefault.setName("");
        beanDefault.setAppKey("");
        beanDefault.setVersionCode("");
        beanDefault.setVersionName("");
        //
        mAppList.add(beanDefault);

        AppEntrityInfo clock = new AppEntrityInfo();
        clock.setPackageName("com.ubtech.deskclock");
        clock.setName("");
        clock.setAppKey("");
        clock.setVersionCode("");
        clock.setVersionName("");
        //
        mAppList.add(clock);

        for (int i = 0; i < allApps.size(); i++) {
            PackageInfo packageInfo = allApps.get(i);
            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager().getApplicationInfo(
                        packageInfo.packageName, PackageManager.GET_META_DATA);
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (appInfo == null)
                continue;
            Bundle meta = appInfo.metaData;
            if (meta == null)
                continue;
            String appKey = meta.getString("alpha2_appid");
            if (!TextUtils.isEmpty(appKey)) {

                AppEntrityInfo bean = new AppEntrityInfo();
                bean.setPackageName(packageInfo.packageName);
                bean.setName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                bean.setAppKey(appKey);
                bean.setVersionCode("" + packageInfo.versionCode);
                bean.setVersionName(packageInfo.versionName);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    LogUtils.d("FLAG_SYSTEM system app=" + packageInfo.packageName);
                    bean.setSystemApp(true);
                } else {
                    LogUtils.d("app=" + packageInfo.packageName);
                    bean.setSystemApp(false);
                }
                Drawable drawable = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                BitmapDrawable bd = (BitmapDrawable) drawable;
                Bitmap bm = bd.getBitmap();

                byte[] icon = Bitmap2Bytes(bm);
                bean.setIcon(icon);
                String config = meta.getString("alpha2_appconfig");
                if (config != null) {
                    if (config.equals("config")) {
                        bean.setSetting(true);
                    }
                }
                String buttonEvent = meta.getString("alpha2_buttonevent");
                if (buttonEvent != null) {//
                    if (buttonEvent.equals("buttonevent")) {
                        bean.setButtonEvent(true);
                    }
                }
                mAppList.add(bean);
            }
        }
        return mAppList;
    }



    // Bitmap转锟斤拷锟斤拷byte[]
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // public void saveMyBitmap(String bitName, Bitmap mBitmap) {
    // File f = new File("/sdcard/" + bitName + ".png");
    // try {
    // f.createNewFile();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // }
    // FileOutputStream fOut = null;
    // try {
    // fOut = new FileOutputStream(f);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    // try {
    // fOut.flush();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // try {
    // fOut.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 判断是否可以删除
     *
     * @param packageName
     * @return
     */
    public boolean getUsesPermission(String packageName) {
        boolean ret = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_PERMISSIONS);
            String[] usesPermissionsArray = packageInfo.requestedPermissions;
            for (int i = 0; i < usesPermissionsArray.length; i++) {
                // 得到每个权限的名字,如:android.permission.INTERNET
                String usesPermissionName = usesPermissionsArray[i];
                if (usesPermissionName
                        .equals("android.permission.PACKAGE_PROTECT")) {
                    ret = true;
                    break;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return ret;
    }

    /**
     * 判断app存不存在，存在的话启动
     **/
/*    public static boolean startApp(Context context, String packageName) {
        if (checkAppExist(context, packageName)) {
            Intent intent = new Intent();
            intent.setAction(StaticValue.ALPHA_APP_MANAGE);
            Bundle bundle = new Bundle();
            bundle.putString("appevent", "start");
            bundle.putString("packageName", packageName);
            intent.putExtras(bundle);
            context.sendBroadcast(intent);

            return true;
        } else {
            return false;
        }
    }
*/
    /****/
    public static boolean checkAppExist(Context context, String packageName) {
        boolean isAppExist = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_META_DATA);
            if (appInfo != null) {
                isAppExist = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isAppExist;
    }

    public interface IGetPkgCallback<T> {
        void onGetPkgInfo(T AppEntrityInfo);
    }
}
