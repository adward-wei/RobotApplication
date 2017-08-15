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

import com.ubtechinc.alpha2ctrlapp.entity.WeiXinLoginInfo;
import com.ubtechinc.alpha2ctrlapp.entity.net.LoginModule;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetWeixinLoginRequest;
import com.ubtechinc.alpha2ctrlapp.entity.response.ThirdLoginResponse;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface ILoginDataSource {
    /**
     * 拉取登录的初始化信息
     */
    interface LoginDataCallback {

        void onLoadLoginData(LoginModule.Response loginResponses);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    interface ThirdLoginCallback {
        void onLoadLoginData(ThirdLoginResponse thirdLoginResponses);

        void onDataNotAvailable(ThrowableWrapper e);


    }

    /**
     * 获取消息详情
     */
    interface LoginOutCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }

    void loginOut(@NonNull LoginOutCallback callback);


    interface getWeiXinLoginInfoCallback {

        void onLoadLoginData(List<WeiXinLoginInfo> weiXinLoginInfos);

        void onDataNotAvailable();

    }
    void loginAndLoadData(String userName, String pwd, @NonNull LoginDataCallback callback);

    void doThirdLogin(String relationId, String relationType, @NonNull ThirdLoginCallback callback);
    void getWeiXinLoginInfo(GetWeixinLoginRequest request, @NonNull getWeiXinLoginInfoCallback callback);
}
