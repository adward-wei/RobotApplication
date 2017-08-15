package com.ubtechinc.alpha2ctrlapp.data.user;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.database.NoticeMessageInfo;
import com.ubtechinc.alpha2ctrlapp.database.NoticeMessageProvider;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;

import java.util.List;


/**
 * @author：tanghongyu
 * @date：4/7/2017 3:36 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:36 PM
 * [A brief description]
 * version
 */

public class MessageLocalDataSource implements IMessageDataSource {

    private static MessageLocalDataSource INSTANCE;
    Context context;
    private MessageLocalDataSource(@NonNull Context context) {
       this.context = context;
    }
    public static MessageLocalDataSource getInstance(@NonNull Context context) {
        Preconditions.checkNotNull(context);
        if (INSTANCE == null) {
            INSTANCE = new MessageLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMessages(String userId,@NonNull LoadMessageCallback callback) {

        callback.onMessageLoaded(NoticeMessageProvider.get().findNoticeListByParam(ImmutableMap.of("userId", (Object) userId)));
    }

    @Override
    public void getMessageByMessageId(String userId, @NonNull String messageId, @NonNull GetMessageCallback callback) {
        NoticeMessageInfo noticeMessageInfo = NoticeMessageProvider.get().findNoticeByParam( ImmutableMap.of("noticeId", (Object) messageId));
        if(noticeMessageInfo == null) {
            callback.onDataNotAvailable();
        }else {
            NoticeMessage noticeMessage = new NoticeMessage();
            BeanUtils.copyBean(noticeMessageInfo,noticeMessage );
            callback.onMessageLoaded(noticeMessage);;
        }

    }

    @Override
    public void saveMessage(@NonNull NoticeMessage message) {
     //   message.save();
        NoticeMessageInfo noticeMessageInfo = new NoticeMessageInfo();
        BeanUtils.copyBean(message,noticeMessageInfo );
        NoticeMessageProvider.get().save(noticeMessageInfo);
    }

    @Override
    public void saveMessages(@NonNull List<NoticeMessage> messages) {
       // MessageResponse.saveAll(messages);
         ImmutableList.Builder builder = ImmutableList.builder();
        for(NoticeMessage noticeMessage : messages) {
            NoticeMessageInfo noticeMessageInfo = new NoticeMessageInfo();
            BeanUtils.copyBean(noticeMessage,noticeMessageInfo );
            builder.add(noticeMessageInfo);
        }
        NoticeMessageProvider.get().saveAll(builder.build());
    }

    @Override
    public void getUnreadMessageAuthorize(String userId,LoadMessageCallback callback) {
        NoticeMessageProvider.get().findNoticeListByParam( ImmutableMap.of("userId", userId, "noticeType", (Object) BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG, "noticeDetailType", BusinessConstants.AUTHORIZATION_TYPE_AUTHORIZE, "isRead", false));
    }

    @Override
    public void refreshMessage() {

    }

    @Override
    public void deleteAllMessage(String userId) {
        NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("userId", (Object) userId));
    }

    @Override
    public void deleteMessageByMessageId(String userId,@NonNull String messageId) {
        NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("userId", userId, "noticeId", (Object) messageId));
    }

    @Override
    public void deleteMessageByMessageType(String userId,@NonNull int messageType) {
        NoticeMessageProvider.get().deleteNoticeByParam(ImmutableMap.of("userId", userId, "noticeType", (Object) messageType));
    }


}
