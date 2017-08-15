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

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.EditUserInfoRequest;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IUserConfigDataSource
 * @date 6/8/2017
 * @author tanghongyu
 * @Description 用户配置信息
 * @modifier
 * @modify_time
 */
public interface IUserConfigDataSource {

    interface LoadUserConfigCallback {

        void onLoadUserConfigData(List<RobotInfo> robotList, List<NoticeMessage> messageList);

        void onDataAvailable(ThrowableWrapper e);
    }
    interface ModifyUserInfoCallback {

        void onSuccess(UserInfo userInfo);

        void onFail(ThrowableWrapper e);
    }
    interface FeedbackCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }

    void getUserConfigData(@NonNull LoadUserConfigCallback callback);
    void modifyUserInfo(EditUserInfoRequest request, @NonNull ModifyUserInfoCallback callback);
    void feedback(String comment, @NonNull FeedbackCallback callback);
}
