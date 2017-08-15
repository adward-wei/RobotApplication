package com.ubtechinc.alpha.serverlibutil.service;

import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.ubtechinc.alpha.serverlibutil.aidl.IServiceFetcher;

/**
 * Created by Administrator on 2017/5/23.
 */
public class BinderFetchServiceUtil{
    private Context mContext;
    private IServiceFetcher mService;
    private static volatile BinderFetchServiceUtil fetchService;
    private boolean processExitWithBinderLinkToDeath = true;

    IBinder.DeathRecipient deathRecipient;
    IBinder binder;

    private BinderFetchServiceUtil(Context context) {
        this.mContext = context.getApplicationContext();
        deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                if(binder != null)
                    binder.unlinkToDeath(this, 0);
                System.out.println("processExitWithBinderLinkToDeath: "+processExitWithBinderLinkToDeath);
                if(processExitWithBinderLinkToDeath)
                    Runtime.getRuntime().exit(0);
                mService = null;
            }
        };
        getFetcherBinder();

    }

    public static BinderFetchServiceUtil get(Context context) {
        if (fetchService != null) return fetchService;
        synchronized (BinderFetchServiceUtil.class) {
            if (fetchService == null) {
                fetchService = new BinderFetchServiceUtil(context);

            }
        }
        return fetchService;
    }

    public IBinder getServiceBinder(String name) {
        try {
            if (mService == null) {
                getFetcherBinder();
            }
            return mService.getService(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String appId;
    private String appKey;
    //必须要持有LifeBinder对象，否则主服务无法通过该对象实现对client进程的生命监控
    private LifeBinder lifeBinder;
    public void autorityIdentify() {
        if (mService == null) {
            getFetcherBinder();
        }
        try {
            mService.registerAppIdKey(mContext.getPackageName(),appId,appKey,lifeBinder = new LifeBinder());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class LifeBinder extends Binder {

    }

    private void getFetcherBinder() {
        Uri uri = Uri.parse("content://alpha2.service.BinderProvider");
        Bundle bundle = null;
        try {
            bundle = mContext.getContentResolver().call(uri, "@", null, null);
        }catch (Exception e) {
            //主服务apk不在时会crash
        }
        if (bundle != null) {
            binder = bundle.getBinder("fetchBinder");
            mService = IServiceFetcher.Stub.asInterface(binder);
            try {
                binder.linkToDeath(deathRecipient, Binder.FLAG_ONEWAY);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setExitWithBinderLinkToDeath(boolean processExitWithBinderLinkToDeath){
        this.processExitWithBinderLinkToDeath = processExitWithBinderLinkToDeath;
    }

    public void unlinkToDeath(){
        System.out.println("unlinkToDeath111");
        boolean result = binder.unlinkToDeath(deathRecipient,Binder.FLAG_ONEWAY);
        System.out.println("unlinkToDeath222: "+result);
    }

}
