package com.ubtechinc.alpha.service;

import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.AppManager;
import com.ubtechinc.alpha.serverlibutil.aidl.IServiceFetcher;

/**@author adward.wei
 * @data 2017/5/23.
 */
public class MainServiceImpl extends IServiceFetcher.Stub {
    private final static String TAG = "MainServiceImpl";
    private static volatile MainServiceImpl mainServiceImplInstance;

    private MainServiceImpl(){}

    public static MainServiceImpl getMainServiceImplInstance(){
        if(mainServiceImplInstance ==null ){
            synchronized(MainServiceImpl.class){
                if(mainServiceImplInstance == null){
                    mainServiceImplInstance =new MainServiceImpl();
                }
            }
        }
        return mainServiceImplInstance;
    }

    @Override
    public synchronized IBinder  getService(String name) throws RemoteException {
        return ServiceCache.getService(name);
    }

    @Override
    public synchronized void addService(String name, IBinder service) throws RemoteException {
        ServiceCache.addService(name,service);
    }

    @Override
    public synchronized void removeService(String name) throws RemoteException {
        ServiceCache.removeService(name);
    }

    @Override
    public synchronized void registerAppIdKey(String pkgName,String appId, String appKey,IBinder lifeBinder) throws RemoteException {
        AppManager.getInstance().lifeRegister(pkgName,lifeBinder);
    }
}
