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

import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
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
public interface IMessageDataSource {
    /**
     * 拉取消息列表
     */
    interface LoadMessageCallback {

        void onMessageLoaded(List<NoticeMessage> tasks);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    /**
     * 获取消息详情
     */
    interface GetMessageCallback {

        void onMessageLoaded(NoticeMessage message);

        void onDataNotAvailable();
    }

    void getMessages(String userId, @NonNull LoadMessageCallback callback);

    void getMessageByMessageId(String userId,@NonNull String messageId, @NonNull GetMessageCallback callback);

    void saveMessage(@NonNull NoticeMessage message);
    void saveMessages(@NonNull List<NoticeMessage> messages);
    void getUnreadMessageAuthorize(String userId,@NonNull LoadMessageCallback callback);
    void refreshMessage();
    void deleteAllMessage(String userId);
    void deleteMessageByMessageId(String userId,@NonNull String messageId);
    void deleteMessageByMessageType(String userId,@NonNull int messageType);
}
