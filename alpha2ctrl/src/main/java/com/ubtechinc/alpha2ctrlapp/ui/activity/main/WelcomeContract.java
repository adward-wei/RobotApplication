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

package com.ubtechinc.alpha2ctrlapp.ui.activity.main;


import com.ubtechinc.alpha2ctrlapp.base.BasePresenter;

/**
 * @ClassName WelcomeContract
 * @date 5/15/2017
 * @author tanghongyu
 * @Description 欢迎页
 * @modifier
 * @modify_time
 */
public interface WelcomeContract {

    interface View  {
        void finish();
        void skipToGuide();
        void skipMainToLogin(int state);
        void skipToMainPage();
    }

    interface Presenter extends BasePresenter {

        void doGo();

    }
}
