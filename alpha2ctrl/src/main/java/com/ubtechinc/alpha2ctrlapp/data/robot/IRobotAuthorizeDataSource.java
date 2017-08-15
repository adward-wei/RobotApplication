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

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IRobotAuthorizeDataSource
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 机器人授权
 * @modifier
 * @modify_time
 */
public interface IRobotAuthorizeDataSource {
    /**
     * 获取App列表信息
     */
    interface BindRobotCallback {

        void onSuccess(String macAddress);

        void onFail(ThrowableWrapper e);
    }

    /**
     * 获取App列表信息
     */
    interface UnBindRobotCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }


    interface LoadRobotListCallback {

        void onLoadRobotList(List<RobotInfo> robotList);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    public interface IControlRobotCallback{
        void onSuccess();
        void onFail(ThrowableWrapper e);
    }
    public interface GetRobotIMStateCallback{
        void onLoadRobotIMState(Map<String, String> stateMap);
        void onDataNotAvailable(ThrowableWrapper e);
    }
    void bindRobot(String robtNo, String userOnlyId, @NonNull BindRobotCallback callback);
    void unbindRobot(String equipmentId, int userId, @NonNull UnBindRobotCallback callback);
    void loadRobotList(@NonNull LoadRobotListCallback callback);
    void loadHistoryRobotList(String userId,@NonNull LoadRobotListCallback callback);
}
