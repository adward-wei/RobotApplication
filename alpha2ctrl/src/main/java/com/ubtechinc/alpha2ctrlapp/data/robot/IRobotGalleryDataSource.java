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

package com.ubtechinc.alpha2ctrlapp.data.robot;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IRobotGalleryDataSource
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 机器人相册
 * @modifier
 * @modify_time
 */
public interface IRobotGalleryDataSource {
    /**
     * 获取机器人相册信息
     */

    interface GetGalleryCallback {
        void onGallery(List<ImageModel> imageList);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 删除图片
     */
    interface DeleteImageCallback {
        void onSuccess();

        void onFail(ThrowableWrapper e);
    }



}
