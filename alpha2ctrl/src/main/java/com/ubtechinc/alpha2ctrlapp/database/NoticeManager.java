package com.ubtechinc.alpha2ctrlapp.database;

import com.google.common.collect.ImmutableMap;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;

import java.util.List;


/**
 * 通知，消息管理.
 *
 * @author shimiso
 */
public class NoticeManager {
    private static NoticeManager noticeManager = null;

    private NoticeManager() {
    }

    public static NoticeManager getInstance() {

        if (noticeManager == null) {
            noticeManager = new NoticeManager();
        }

        return noticeManager;
    }






    /**
     * 将文件改为已读
     *
     * @author shimiso
     * @update 2012-5-16 下午3:22:44
     */
    public void updateNoticeStatus(String noticeId, boolean isRead) {

        NoticeMessageProvider.get().updateParamByNoticeId(noticeId, ImmutableMap.of("isRead", (Object) isRead));
    }

    public List<NoticeMessage> getUnReadAuthorizeNoticeList(String userId) {

        return  NoticeMessageProvider.get().findNoticeListByParam( ImmutableMap.of("userId",userId, "noticeType", (Object) BusinessConstants.NOTICE_TYPE_AUTHORIZE_MSG, "noticeDetailType", BusinessConstants.AUTHORIZATION_TYPE_AUTHORIZE, "isRead", BusinessConstants.NOTICE_STATE_UNREAD));
    }

    /**
     * 获取所有未读消息.(分类)1 好友添加 2系统 消息 3 聊天4授权消息
     *
     * @return
     * @author shimiso
     * @update 2012-5-16 下午3:22:53
     */
    public List<NoticeMessage> getUnReadNoticeListByType(int type) {

        return NoticeMessageProvider.get().findNoticeListByParam(ImmutableMap.of("userId", Alpha2Application.getAlpha2().getUserId(), "noticeType", type, "isRead", (Object)BusinessConstants.NOTICE_STATE_UNREAD));

    }

    /**
     * 获取某个类型的消息
     *
     * @return
     * @author shimiso
     * @update 2012-5-16 下午3:22:53
     */
//    public List<NoticeMessage> getNoticeListByType(int type) {
//        return NoticeMessage.where("userId = ? and noticeType = ? ", mApplication.getUserId(), String.valueOf(type)).order("noticeTime desc").find(NoticeMessage.class);
//
//    }



    public Integer getUnReadNoticeCount() {
return NoticeMessageProvider.get().getNoticeListCountByParam( ImmutableMap.of("userId",  Alpha2Application.getAlpha2().getUserId(), "isRead", (Object) BusinessConstants.NOTICE_STATE_UNREAD));
    }


    /**
     * 根据id删除记录
     *
     * @param noticeId
     * @author Juan强
     */
    public void delById(String userId, String noticeId) {
        NoticeMessageProvider.get().deleteNoticeByParam( ImmutableMap.of("userId", userId, "noticeId", (Object) noticeId));

    }




}
