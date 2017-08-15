package com.ubtechinc.alpha2ctrlapp.data.user;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetMessageListModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @author：tanghongyu
 * @date：4/7/2017 3:36 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:36 PM
 * [A brief description]
 * version
 */

public class MessageRemoteDataSource implements IMessageDataSource {

    private static final String TAG = "MessageRemoteDataSource";
    private static MessageRemoteDataSource INSTANCE;
    Alpha2Application mContext;
    private MessageRemoteDataSource(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        this.mContext = (Alpha2Application) application;
    }
    public static MessageRemoteDataSource getInstance(@NonNull Context application) {
        Preconditions.checkNotNull(application);
        if (INSTANCE == null) {
            INSTANCE = new MessageRemoteDataSource(application);
        }
        return INSTANCE;
    }

    @Override
    public void getMessageByMessageId(String userId, @NonNull String messageId, @NonNull GetMessageCallback callback) {

    }

    @Override
    public void getUnreadMessageAuthorize(String userId, @NonNull LoadMessageCallback callback) {

    }

    @Override
    public void deleteAllMessage(String userId) {

    }

    @Override
    public void deleteMessageByMessageId(String userId, @NonNull String messageId) {

    }

    @Override
    public void deleteMessageByMessageType(String userId, @NonNull int messageType) {

    }

    @Override
    public void getMessages(String userId,@NonNull final LoadMessageCallback callback) {
        Preconditions.checkNotNull(callback);
        GetMessageListModule.Request request = new GetMessageListModule().new Request();
        HttpProxy.get().doGet(request, new ResponseListener<GetMessageListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetMessageListModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onMessageLoaded(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });




    }



    @Override
    public void saveMessage(@NonNull NoticeMessage message) {

    }

    @Override
    public void saveMessages(@NonNull List<NoticeMessage> messages) {

    }



    @Override
    public void refreshMessage() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }






}
