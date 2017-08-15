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

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IRobotVoiceDataSource
 * @date 6/17/2017
 * @author tanghongyu
 * @Description 机器人语音相关
 * @modifier
 * @modify_time
 */
public interface IRobotVoiceDataSource {
    /**
     * 设置边冲边玩
     */
    interface StartVoiceCompoundCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }


    interface ReplayTTSContentCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }
    interface GetSpeechRecordCallback {

        void onLoadSpeechRecords(List<RecordResultInfo> speechRecordList);

        void onDataNotAvailable(ThrowableWrapper e);
    }

}
