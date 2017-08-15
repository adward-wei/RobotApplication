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

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IRobotAppDataSource
 * @date 6/14/2017
 * @author tanghongyu
 * @Description 机器人App
 * @modifier
 * @modify_time
 */
public interface IRobotAlarmDataSource {
    /**
     * 获取App列表信息
     */
    interface ControlAlarmCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }
    interface GetValidAlarmListCallback {
        void onLoadValidAlarmList( List<DeskClock> alarmList);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    interface GetAllAlarmListCallback {
        void onLoadAlarmList(List<DeskClock> alarmList);

        void onDataNotAvailable(ThrowableWrapper e);
    }


}
