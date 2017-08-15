package com.ubtechinc.alpha.provider.contracts;

import android.net.Uri;

import com.ubtechinc.alpha.ops.action.Action;
import com.ubtechinc.alpha.provider.AlphaContentProvider;
import com.ubtechinc.alpha.provider.EntityManagerHelper;

/**
 * @desc : 动作信息表协议
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class ActionInfoContract extends AlphaContract {
    private static final String TABLE_NAME = EntityManagerHelper.DB_ACTION_INFO_TABLE;
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
        return Action.class;
    }
}
