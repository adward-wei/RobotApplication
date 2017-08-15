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
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfig;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;

/**
 * @ClassName MainPageContract
 * @date 5/15/2017
 * @author tanghongyu
 * @Description 主页
 * @modifier
 * @modify_time
 */
public interface MainPageContract {

    interface View  {
        void dismissMenuPop();
        void destoryMenuPop();
        boolean isMenuPopShow();
        void showMenuPop();
        void goMainMode(boolean fromBottom);
        void showMenuDiag(ButtonConfig ss, BaseContactFragememt fragment,
                          boolean hasSetting);
        void showLoadingDialog();
        void dismissLoadingDialog();
    }

    interface Presenter extends BasePresenter {

        void loginOut();
        void startPlayMp3(String filePath);
        void stopPlayMp3();

    }
}
