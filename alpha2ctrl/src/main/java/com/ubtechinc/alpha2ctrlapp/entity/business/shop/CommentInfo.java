package com.ubtechinc.alpha2ctrlapp.entity.business.shop;
/**
 * @ClassName CommentInfo
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 评论实体类
 * @modifier
 * @modify_time
 */
public class CommentInfo {

	private int actionId;
	private String  userName;
	private int commentId;
	private String commentContext;
	private String  commentTime;
	private String  commentNewTime;
	private String commentType;
	private int commentUserId;
	private int replyCommentId;
	private String  userImage;
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCommentContext() {
		return commentContext;
	}

	public void setCommentContext(String commentContext) {
		this.commentContext = commentContext;
	}



	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public int getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(int commentUserId) {
		this.commentUserId = commentUserId;
	}

	public int getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(int replyCommentId) {
		this.replyCommentId = replyCommentId;
	}
}
