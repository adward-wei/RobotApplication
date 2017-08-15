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

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.GetLogByMobileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @author tanghongyu
 * @ClassName IRobotAppDataSource
 * @date 6/14/2017
 * @Description 机器人App
 * @modifier
 * @modify_time
 */
public interface IRobotConfigDataSource {
    /**
     * 设置边冲边玩
     */
    interface SetChargeWhilePlayingCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }

    /**
     * 获取边冲边玩状态
     */
    interface GetChargeWhilePlayingStateCallback {

        void onLoadChargeWhilePlayingState(boolean isOpen);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 获取是否播放TTS时边播放动作状态
     */
    interface GetTTSActionStateCallback {
        void onLoadTTSActionState(boolean isOpen);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 设置是否播放TTS时边播放动作
     */
    interface SetTTSActionStateCallback {
        void onSuccess();

        void onFail(ThrowableWrapper e);
    }

    /**
     * 设置是否播放TTS时边播放动作
     */
    interface StartUploadLogCallback {
        void onSuccess(GetLogByMobileEntrity data);

        void onFail(ThrowableWrapper e);
    }

    interface UpdateInfoCallback {

        void onSuccess(String robotSerialNo,String userOtherName, String userImage);

        void onFail(ThrowableWrapper e);
    }
}
