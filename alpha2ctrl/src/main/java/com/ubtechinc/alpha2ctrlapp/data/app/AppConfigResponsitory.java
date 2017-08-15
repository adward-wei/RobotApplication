package com.ubtechinc.alpha2ctrlapp.data.app;

import android.support.annotation.NonNull;

/**
 * @author：tanghongyu
 * @date：5/15/2017 6:17 PM
 * @modifier：tanghongyu
 * @modify_date：5/15/2017 6:17 PM
 * [A brief description]
 * version
 */

public class AppConfigResponsitory implements IAppConfigDataSource {
    //使用volatile关键字保其可见性
    volatile private static AppConfigResponsitory INSTANCE = null;
    private AppConfigResponsitory(){}

    public static AppConfigResponsitory get() {
        try {
            if(INSTANCE != null){//懒汉式

            }else{
                //创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (AppConfigResponsitory.class) {
                    if(INSTANCE == null){//二次检查
                        INSTANCE = new AppConfigResponsitory();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getAppConfig(@NonNull GetAppConfigCallback callback) {

    }
}
