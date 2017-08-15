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

package com.ubtechinc.alpha2ctrlapp.data.user;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.business.user.MyFavoriteInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IRegisterDataSource
 * @date 6/5/2017
 * @author tanghongyu
 * @Description 注册相关
 * @modifier
 * @modify_time
 */
public interface ICollectDataSource {

    interface LoadCollectionCallback {

        void onSuccess(List<MyFavoriteInfo> response);

        void onFail(ThrowableWrapper e);
    }

    interface CollectCallback {
        void onSuccess();

        void onFail(ThrowableWrapper e);

    }



    void getCollectionList(int appType, @NonNull LoadCollectionCallback callback);
    void doCollect(int collectRelationId, int collectType, @NonNull CollectCallback callback);
    void cancelCollect(int collectRelationId, int collectType, @NonNull CollectCallback callback);
    void deleteCollect(int collectId, @NonNull CollectCallback callback);
}
