package com.ubtechinc.alpha.provider;

import android.content.Context;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.appmanager.AppInfo;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class AppInfoVisitor extends BaseVisitor<AppInfo> {

    private static AppInfoVisitor sInstance;

    private AppInfoVisitor(Context context) {
        super(context);
    }

    public static AppInfoVisitor getInstance() {
        if (sInstance == null) {
            synchronized (AppInfoVisitor.class) {
                sInstance = new AppInfoVisitor(AlphaApplication.getContext());
            }
        }
        return sInstance;
    }

    /**Note:要考虑版本升级的情况*/
    @Override
    protected EntityManager<AppInfo> entityManagerFactory() {
        dbMananger = EntityManagerFactory.getInstance(AlphaApplication.getContext(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(AppInfo.class,
                        EntityManagerHelper.DB_APP_INFO_TABLE);
        return dbMananger;
    }

    public AppInfo getTopApp() {
        List<AppInfo> appInfoList = getAllData();
        if(appInfoList != null && !appInfoList.isEmpty()) {
            return appInfoList.get(0);
        } else {
            return null;
        }
    }
}
