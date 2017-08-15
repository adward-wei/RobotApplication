package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

/**
 * @author：tanghongyu
 * @date：4/7/2017 3:36 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:36 PM
 * [A brief description]
 * version
 */

public class ActionLocalDataSource implements IActionDataSource {

    private static ActionLocalDataSource INSTANCE;
    Context context;
    private ActionLocalDataSource(@NonNull Context context) {
       this.context = context;
    }
    public static ActionLocalDataSource getInstance(@NonNull Context context) {
        Preconditions.checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new ActionLocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getLastActionList(int page, int pageSize, @NonNull LoadActionCallback callback) {

    }

    @Override
    public void searchAction(String actionName, int actionSonType, int page, int pageSize, @NonNull LoadActionCallback callback) {

    }

    @Override
    public void getActionList(int actionSonType, int page, int pageSize, @NonNull LoadActionCallback callback) {

    }

    @Override
    public void getActionDetail(int actionId, @NonNull LoadActionDetailCallback callback) {

    }

    @Override
    public void praiseAction(String praiseObjectId, @NonNull PraiseActionCallback callback) {

    }

    @Override
    public void getShareUrl(String code, String type, @NonNull LoadShareUrlCallback callback) {

    }
}
