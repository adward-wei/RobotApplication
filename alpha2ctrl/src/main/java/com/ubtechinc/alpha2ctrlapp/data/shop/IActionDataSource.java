package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @author：tanghongyu
 * @date：5/17/2017 9:20 PM
 * @modifier：tanghongyu
 * @modify_date：5/17/2017 9:20 PM
 * [A brief description]
 * version
 */

public interface IActionDataSource {

    /**
     * 拉取消息列表
     */
    interface LoadActionCallback {

        void onActionLoaded(List<ActionInfo> tasks);

        void onDataNotAvailable(ThrowableWrapper e);
    }


    interface LoadActionDetailCallback {

        void onActionDetailLoaded(ActionDetail tasks);

        void onDataNotAvailable(ThrowableWrapper e);
    }
    interface LoadShareUrlCallback {

        void onShareUrlLoaded(String url);

        void onDataNotAvailable(ThrowableWrapper e);
    }
    interface PraiseActionCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }
    void getLastActionList(int page, int pageSize, @NonNull LoadActionCallback callback);

    void searchAction(String actionName, int actionSonType,int page, int pageSize, @NonNull LoadActionCallback callback);

    void getActionList(int actionSonType,int page, int pageSize, @NonNull LoadActionCallback callback);

    void getActionDetail(int actionId,  @NonNull LoadActionDetailCallback callback);

    void praiseAction(String praiseObjectId, @NonNull PraiseActionCallback callback);

    void getShareUrl(String code,String type, @NonNull LoadShareUrlCallback callback);
}
