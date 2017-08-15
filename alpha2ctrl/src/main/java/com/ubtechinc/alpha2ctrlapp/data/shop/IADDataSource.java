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

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.RecommenedPageInfo;
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
public interface IADDataSource {
    /**
     * 拉取登录的初始化信息
     */
    interface ADDataCallback {

        void onLoadADData(List<RecommenedPageInfo> loginResponses);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 加载广告推荐信息
     * @param language
     * @param callback
     */
    void getAdvertisingPromotion(String language, @NonNull ADDataCallback callback);
}
