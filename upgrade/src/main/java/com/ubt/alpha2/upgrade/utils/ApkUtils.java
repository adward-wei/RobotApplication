package com.ubt.alpha2.upgrade.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.tencent.openqq.protocol.imsdk.msg;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: slive
 * @description:  获取app的相关信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ApkUtils {

    private static String serialNumber = null;
    private static String productModel = null;

    /**
     * 获取版本号
     * @param packageName
     * @param context
     */

    public static String getVersion(String packageName, Context context) {
        PackageInfo pi = null;
        String version = "";
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
            // 改成用versionCode来判断版本号
            version = pi.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.d(" not found the app： "+packageName);
            version = "0";
        }
        return version;
    }

    /**
     * 获取VersionName
     * @param packageName
     * @param context
     */
    public static String getVersionName(String packageName) {
        PackageInfo pi;
        String version;
        Context context = UpgradeApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
            // 改成用versionCode来判断版本号
            version = pi.versionName+"";
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.d(" not found the app： "+packageName);
            version = "0";
//            e.printStackTrace();
        }
        return version;
    }

    public static String getDataApkPath(String packageName, Context context) {
        String dataPath = null;
        PackageManager pm = context.getPackageManager();
        ApplicationInfo app = null;
        try {
            app = pm.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (app != null)
            dataPath = app.sourceDir;
        return dataPath;
    }

    public static String getRobotId(){
        if(!TextUtils.isEmpty(serialNumber))
            return serialNumber;
        try {
            Context context = UpgradeApplication.getContext();
            serialNumber = PropertyUtils.getSystemProperty(context,Constants.ROBOT_ID_KEY,"");
            LogUtils.d("serialNumber1=="+serialNumber);
            //保证能得到序列号
            if(TextUtils.isEmpty(serialNumber)){
                Thread.sleep(3000);
                serialNumber = PropertyUtils.getSystemProperty(context,Constants.ROBOT_ID_KEY,"");
            }
            if(TextUtils.isEmpty(serialNumber))
                serialNumber = "40AI8D1SN3";
            LogUtils.d("serialNumber1=="+serialNumber);
        }catch (Exception e){
            LogUtils.logException(e);
            serialNumber = null;
        }finally {
            return serialNumber;
        }
    }

    // 检查手机上是否安装了指定的软件
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    //杀死服务
    public static void killAppByPackageName(Context context , String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(am, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Android系统版本信息
     */
    public static String getAndroidSystemVersion() {
        Context mc = UpgradeApplication.getContext();
        String androidVersion = PropertyUtils.getSystemProperty(mc, UpgradeModel.SYSTEM_VERSION, "0.0.0.0");
        androidVersion = androidVersion.trim().replace("\r|\n", "");
        return androidVersion;
    }

    /**
     * 反射ActivityThread的releaseProvider方法
     */
    public static void reflectReleaseProvider(){
        ContentResolver  contentResolver = UpgradeApplication.getContext().getContentResolver();
        try {
            //1、获取 acquireProvider 方法
            Method acquireProvider = contentResolver.getClass().getMethod("acquireProvider", Uri.class);
            LogUtils.d("acquireProvider: "+acquireProvider);
            if(acquireProvider == null){
                LogUtils.d("acquireProvider == null");
                return ;
            }
            acquireProvider.setAccessible(true);
            //2、获取 IContentProvider
            Object iContentProvider = acquireProvider.invoke(contentResolver,Uri.parse("content://alpha2.service.BinderProvider"));
            LogUtils.d("iContentProvider: "+iContentProvider);
            if(iContentProvider == null){
                return;
            }

            //3、获取 IBinder 对象
            Method asBinder = iContentProvider.getClass().getDeclaredMethod("asBinder");
            LogUtils.d("asBinder: "+asBinder);
            if(asBinder == null)
                return;

            asBinder.setAccessible(true);
            Object iBinder = asBinder.invoke(iContentProvider);
            LogUtils.d("iBinder: "+iBinder);
            if(iBinder == null)
                return;

            //4、获取 ActivityThread 的对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            LogUtils.d("activityThreadClass: "+activityThreadClass);
            if(activityThreadClass == null)
                return;
            Method currentActivityThread  = activityThreadClass.getMethod("currentActivityThread");
            LogUtils.d("currentActivityThread: "+currentActivityThread);
            if(currentActivityThread == null)
                return;
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);

            LogUtils.d("activityThread: "+activityThread);
            if(activityThread == null)
                return;
            //5、获取 mProviderRefCountMap
            Field mProviderRefCountMap =  activityThreadClass.getDeclaredField("mProviderRefCountMap");
            LogUtils.d("mProviderRefCountMap: "+mProviderRefCountMap);
            if(mProviderRefCountMap == null)
                return;
            mProviderRefCountMap.setAccessible(true);
            Object ProviderRefCountMap = mProviderRefCountMap.get(activityThread);

            //6、获取 providerRefCount
            Method getMethod = ProviderRefCountMap.getClass().getMethod("get",Object.class);
            LogUtils.d("getMethod: "+getMethod);
            if(getMethod == null)
                return;
            getMethod.setAccessible(true);
            Object providerRefCount = getMethod.invoke(ProviderRefCountMap,iBinder);
            LogUtils.d("providerRefCount: "+providerRefCount);
            if(providerRefCount == null)
                return;
            Class<?> providerRefCountClz = providerRefCount.getClass();
            //7、获取 stableCount
            Field field = providerRefCountClz.getDeclaredField("stableCount");
            LogUtils.d("field: "+field);
            if(field == null)
                return;
            field.setAccessible(true);
            int stableCount = (int) field.get(providerRefCount);
            //8、设置为 1
            LogUtils.d("stableCount: "+stableCount);
            if(stableCount>1){
                field.set(providerRefCount,1);
            }

            Field unStableField = providerRefCountClz.getDeclaredField("unstableCount");
            LogUtils.d("unStableField: "+unStableField);
            if(unStableField == null)
                return;
            unStableField.setAccessible(true);
            int unstableCount = (int) unStableField.get(providerRefCount);
            LogUtils.d("unstableCount111: "+unstableCount);
            if(unstableCount != 0){
                unStableField.set(providerRefCount,0);
            }
            unstableCount = (int) unStableField.get(providerRefCount);
            LogUtils.d("unstableCount222: "+unstableCount);
            //9、调用ActivityThread.releaseProvider
            LogUtils.d("IContentProvider.getClass: "+iContentProvider.getClass());
            Method releaseProviderMethod = activityThreadClass.getMethod("releaseProvider",new Class<?>[]{Class.forName("android.content.IContentProvider"),boolean.class});
            LogUtils.d("releaseProviderMethod: "+releaseProviderMethod);
            if(releaseProviderMethod == null)
                return;
            releaseProviderMethod.setAccessible(true);
            releaseProviderMethod.invoke(activityThread,new Object[]{iContentProvider,true});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProductModel(){
        if(TextUtils.isEmpty(productModel))
            productModel = Build.PRODUCT;
        return productModel;
    }

    public static void rebootRobot(){
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        UpgradeApplication.getContext().sendBroadcast(intent);
    }

    /**
     * check MD5 value
     * @param md5
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean checkMd5Value(String md5,File file) {
        String realMd5 = null;
        realMd5 = MD5FileUtil.getFileMD5String(file);
        LogUtils.d("realMd5: "+realMd5+"  md5: "+md5+"  md5.toUpperCase().equals(realMd5.toUpperCase(): "+(md5.toUpperCase().equals(realMd5.toUpperCase())));
        if(md5.equalsIgnoreCase(realMd5))
            return true;
        return false;
    }

    /**
     * @author: slive
     * @description: get file md5 value
     * @return:
     */
    public static String getFileMd5Value(File file){
        return MD5FileUtil.getFileMD5String(file);
    }
}
