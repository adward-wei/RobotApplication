package com.ubtechinc.alpha2ctrlapp.entity.business.user;


import com.ubtech.utilcode.utils.TimeUtils;

import java.io.Serializable;
import java.util.Date;

/*************************
* @date 2016/6/24
* @author 唐宏宇
* @Description 消息通知实体
* @modify
* @modify_time
**************************/
public class NoticeMessage implements Serializable, Comparable<NoticeMessage>  {

	private String userId;
	//消息ID
	private String noticeId;
	//来源用户ID
	private int fromId;
	//来源用户名
	private String fromUserName;
	// 标题，属于推送或离线消息字段
	private String title;

	//机器人Id
	private String robotId;
	private String relationId;
	//机器人序列号
	private String robotSeq;
	//来源用户图片Url，用来表示新照片
	private String fromUserImage;
	//消息目标用户id， 一般是自己的UserId
	private String toId;
	//1.好友请求 2.系统消息
	private int noticeType;
	//1已读 0未读
	private int isRead;
	//消息生成时间
	private String noticeTime;
	//消息详细类型（区分授权类型）
	private int noticeDetailType;
	//消息内容
	private String noticeDes;
	//内容拓展
	private String noticeExtend;
	//消息内容,推送或离线消息字段
	private String noticeContent;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNoticeDes() {
		return noticeDes;
	}

	public void setNoticeDes(String noticeDes) {
		this.noticeDes = noticeDes;
	}

	public String getNoticeExtend() {
		return noticeExtend;
	}

	public void setNoticeExtend(String noticeExtend) {
		this.noticeExtend = noticeExtend;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getRobotId() {
		return robotId;
	}

	public void setRobotId(String robotId) {
		this.robotId = robotId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRobotSeq() {
		return robotSeq;
	}

	public void setRobotSeq(String robotSeq) {
		this.robotSeq = robotSeq;
	}

	public String getFromUserImage() {
		return fromUserImage;
	}

	public void setFromUserImage(String fromUserImage) {
		this.fromUserImage = fromUserImage;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	public int getNoticeDetailType() {
		return noticeDetailType;
	}

	public void setNoticeDetailType(int noticeDetailType) {
		this.noticeDetailType = noticeDetailType;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	@Override
	public int compareTo(NoticeMessage oth) {
		if (null == this.getNoticeTime() || null == oth.getNoticeTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getNoticeTime().length() == oth.getNoticeTime().length()
				&& this.getNoticeTime().length() == 23) {
			time1 = this.getNoticeTime();
			time2 = oth.getNoticeTime();
		} else {
			time1 = this.getNoticeTime().substring(0, 19);
			time2 = oth.getNoticeTime().substring(0, 19);
		}
		Date da1 = TimeUtils.stringFormatToDate(time1, TimeUtils.DEFAULT_DATE_FORMAT);
		Date da2 = TimeUtils.stringFormatToDate(time2, TimeUtils.DEFAULT_DATE_FORMAT);
		if (da1.before(da2)) {
			return 1;
		}
		if (da2.before(da1)) {
			return -1;
		}

		return 0;
	}

	@Override
	public String toString() {
		return "NoticeMessage{" +
				"userId='" + userId + '\'' +
				", noticeId='" + noticeId + '\'' +
				", fromId=" + fromId +
				", fromUserName='" + fromUserName + '\'' +
				", title='" + title + '\'' +
				", robotId='" + robotId + '\'' +
				", relationId='" + relationId + '\'' +
				", robotSeq='" + robotSeq + '\'' +
				", fromUserImage='" + fromUserImage + '\'' +
				", toId='" + toId + '\'' +
				", noticeType=" + noticeType +
				", isRead=" + isRead +
				", noticeTime='" + noticeTime + '\'' +
				", noticeDetailType=" + noticeDetailType +
				", noticeDes='" + noticeDes + '\'' +
				", noticeExtend='" + noticeExtend + '\'' +
				", noticeContent='" + noticeContent + '\'' +
				'}';
	}
}
