/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.ubtechinc.alpha2ctrlapp.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.shop.ActionLocalDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.ActionRepository;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.AppReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.ADPromotionReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.shop.ADRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.shop.CommentReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.LoginAndOutReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageLocalDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRemoteDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.data.user.RegisterReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.UserConfigReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.UserPwdReponsitory;


/**
 * Enables injection of mock implementations for
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static MessageRepository provideMessageRepository(@NonNull Context application) {
        return MessageRepository.getInstance(MessageRemoteDataSource.getInstance(application),
                MessageLocalDataSource.getInstance(application));
    }
    public static LoginAndOutReponsitory provideLoginRepository(@NonNull Context application) {
        return LoginAndOutReponsitory.getInstance();
    }

    public static RegisterReponsitory provideRegisterRepository(@NonNull Context application) {
        return RegisterReponsitory.get();
    }
    public static UserConfigReponsitory provideUserConfigRepository(@NonNull Context application) {
        return UserConfigReponsitory.get();
    }

     public static UserPwdReponsitory provideUserPwdRepository(@NonNull Context application) {
        return UserPwdReponsitory.get();
    }

    public static ADPromotionReponsitory provideADPromotionRepository(@NonNull Context application) {
        return ADPromotionReponsitory.getInstance(ADRemoteDataSource.getInstance(application));
    }
    public static CommentReponsitory provideCommentRepository(@NonNull Context application) {
        return CommentReponsitory.getInstance();
    }
    public static AppReponsitory provideAppRepository(@NonNull Context application) {
        return AppReponsitory.getInstance(AppRemoteDataSource.getInstance(application));
    }
    public static ActionRepository provideActionRepository(@NonNull Context application) {
        return  ActionRepository.getInstance(ActionLocalDataSource.getInstance(application),ActionRemoteDataSource.getInstance(application));
    }

    public static RobotAuthorizeReponsitory provideRobotRepository(@NonNull Context application) {
        return  RobotAuthorizeReponsitory.getInstance();
    }
}
