package com.ubtechinc.alpha.provider.contracts;

import com.ubtechinc.framework.provider.DatabaseContract;

/**
 * @desc : alpha数据表协议
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

abstract public class AlphaContract implements DatabaseContract {
    private static final String SINGLE_MIME_TYPE =
            "vnd.android.cursor.item/com.ubtechic.alpha";
    private static final String MULTIPLE_MIME_TYPE =
            "vnd.android.cursor.dir/com.ubtechic.alpha";

    private static final String ID = "_id";

    @Override
    public String getDefaultSortOrder() {
        return ID + " ASC";
    }

    @Override
    public String getIdColumnName() {
        return ID;
    }

    @Override
    public String getSingleMimeType() {
        return SINGLE_MIME_TYPE;
    }

    @Override
    public String getMultipleMimeType() {
        return MULTIPLE_MIME_TYPE;
    }
}
