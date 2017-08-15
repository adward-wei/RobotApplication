package com.ubtechinc.alpha2ctrlapp.data.app;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.response.MessageResponse;


/**
 * @author：tanghongyu
 * @date：5/15/2017 6:19 PM
 * @modifier：tanghongyu
 * @modify_date：5/15/2017 6:19 PM
 * [A brief description]
 * version
 */

public interface IAppConfigDataSource {


    /**
     * 获取消息详情
     */
    interface GetAppConfigCallback {

        void onAppConfigLoaded(MessageResponse message);

        void onDataNotAvailable();
    }
    void getAppConfig(@NonNull GetAppConfigCallback callback);
}
