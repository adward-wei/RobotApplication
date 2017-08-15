/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IADDataSource
 * @date 5/18/2017
 * @author tanghongyu
 * @Description 广告数据
 * @modifier
 * @modify_time
 */
public interface IAppDataSource {
    /**
     * 获取App列表信息
     */
    interface AppListDataCallback {

        void onLoadAppList(List<AppInfo> appInfos);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 获取App列表信息
     */
    interface LoadAppDetailDataCallback {

        void onLoadAppDetail(List<AppDetail> appDetails);

        void onDataNotAvailable(ThrowableWrapper e);
    }


    /**
     * 获取App列表信息
     * @param page
     * @param callback
     */
    void loadAppList(int page , int pageSize, @NonNull AppListDataCallback callback);

    void loadRecentAppList(int page , int pageSize, @NonNull AppListDataCallback callback);
    /**
     * 搜索App
     * @param appName
     * @param page
     * @param pageSize
     * @param callback
     */
    void searchApp(String appName, int page , int pageSize, @NonNull AppListDataCallback callback);

    void getAppDetail(int appId, @NonNull LoadAppDetailDataCallback callback);

    void batchGetAppInfo(String  appKeyList,  @NonNull AppListDataCallback callback);
}
