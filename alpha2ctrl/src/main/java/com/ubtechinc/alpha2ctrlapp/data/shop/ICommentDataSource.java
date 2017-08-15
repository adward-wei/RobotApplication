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

package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName IADDataSource
 * @date 5/18/2017
 * @author tanghongyu
 * @Description 广告数据
 * @modifier
 * @modify_time
 */
public interface ICommentDataSource {
    /**
     * 拉取登录的初始化信息
     */
    interface CommentDataCallback {

        void onLoadADData(List<CommentInfo> loginResponses);

        void onDataNotAvailable(ThrowableWrapper e);
    }

    interface AddCommentCallback {

        void onSuccess();

        void onFail(ThrowableWrapper e);
    }

    /**
     * 获取评论列表
     * @param actionId
     * @param callback
     */
    void getCommentList(int actionId, @NonNull CommentDataCallback callback);

    void addComment(int actionId,String commentContext, int commentType, int replyCommentId, @NonNull AddCommentCallback callback);
}
