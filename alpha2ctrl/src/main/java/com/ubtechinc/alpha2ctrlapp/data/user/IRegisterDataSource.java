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

import com.ubtechinc.alpha2ctrlapp.entity.net.RegisterModule;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName IRegisterDataSource
 * @date 6/5/2017
 * @author tanghongyu
 * @Description 注册相关
 * @modifier
 * @modify_time
 */
public interface IRegisterDataSource {

    interface RegisterCallback {

        void onSuccess(RegisterModule.Response response);

        void onFail(ThrowableWrapper e);
    }
    /**
     * @ClassName IRegisterDataSource
     * @date 6/5/2017
     * @author tanghongyu
     * @Description
     * @modifier 获取注册验证码
     * @modify_time
     */
    interface GetRegisterCodeCallback {
        void onSuccess();

        void onFail(ThrowableWrapper e);


    }

    void getRegisterCode(String account, String registerType, @NonNull GetRegisterCodeCallback callback);

    void doRegister(String userAccount, String pwd, int accountType, String verifyCode, @NonNull RegisterCallback callback);

}
