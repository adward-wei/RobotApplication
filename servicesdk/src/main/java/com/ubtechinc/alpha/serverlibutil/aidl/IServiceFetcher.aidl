// IServiceFetcher.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

interface IServiceFetcher {

    IBinder getService(String name);
    void addService(String name,in IBinder service);
    void removeService(String name);
    void registerAppIdKey(String appId,String appKey,IBinder lifeBinder);
}
