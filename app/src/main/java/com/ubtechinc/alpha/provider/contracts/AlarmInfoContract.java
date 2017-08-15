package com.ubtechinc.alpha.provider.contracts;

import android.net.Uri;

import com.ubtechinc.alpha.ops.alarm.AlarmInfo;
import com.ubtechinc.alpha.provider.AlphaContentProvider;
import com.ubtechinc.alpha.provider.EntityManagerHelper;

/**
 * @desc : 闹钟信息表协议
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class AlarmInfoContract extends AlphaContract {

    private static final String TABLE_NAME = EntityManagerHelper.DB_ALARM_INFO_TABLE;
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
        return AlarmInfo.class;
    }
}
