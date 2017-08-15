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

import com.ubtechinc.alpha2ctrlapp.entity.net.VerifyResetPwdCodeModule;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface IUserPwdSource {
    /**
     * 拉取登录的初始化信息
     */
    interface GetVerifyCodeCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);

    }

    interface CheckVerifyCodeCallback {

        void onSuccess(VerifyResetPwdCodeModule.Response response);

        void onFail(ThrowableWrapper e);



    }

    /**
     * 获取消息详情
     */
    interface ResetPwdCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }



    void getVerifyCode(String account, int registerType, @NonNull GetVerifyCodeCallback callback);

    void checkVerifyCode(String account, int accountType, String verifyCode, @NonNull CheckVerifyCodeCallback callback);

    void resetPwd(String userId, String pwd, String uuid, @NonNull ResetPwdCallback callback);
}
