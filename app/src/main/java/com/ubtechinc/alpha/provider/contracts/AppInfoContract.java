package com.ubtechinc.alpha.provider.contracts;

import android.net.Uri;

import com.ubtechinc.alpha.appmanager.AppInfo;
import com.ubtechinc.alpha.provider.EntityManagerHelper;
import com.ubtechinc.alpha.provider.AlphaContentProvider;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class AppInfoContract extends AlphaContract {
    private static final String TABLE_NAME = EntityManagerHelper.DB_APP_INFO_TABLE;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AlphaContentProvider.PROVIDER_NAME + "/" + TABLE_NAME);

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdSqlString(String id) {
        return id;
    }

    @Override
    public Class getTableEntityClazz() {
        return AppInfo.class;
    }
}
