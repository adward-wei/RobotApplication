package com.ubtechinc.alpha2ctrlapp.entity.business.shop;
/**
 * @ClassName ActionDetail
 * @date 6/7/2017 
 * @author tanghongyu
 * @Description 动作详情
 * @modifier
 * @modify_time
 */
public class ActionDetail {
	private int actionId;
	private String actionName;
	private String actionTitle;
	private String actionPath;
	private String actionImagePath;
	private String actionDesciber;
	private String actionVideoPath;
	private int actionType;
	private String actionDate;
	private long actionTime;
	private String actionPraiseTime;
	private String actionCommentTime;
	private int isCollect;
	private int isPraise;
	private String actionOriginalId;
	//动作描述（根据语言的不同，显示不同内容）
	private String actionLangDesciber;
	//动作名称（根据语言的不同，显示不同内容）
	private String actionLangName;
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionTitle() {
		return actionTitle;
	}
	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}
	public String getActionPath() {
		return actionPath;
	}
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	public String getActionImagePath() {
		return actionImagePath;
	}
	public void setActionImagePath(String actionImagePath) {
		this.actionImagePath = actionImagePath;
	}
	public String getActionDesciber() {
		return actionDesciber;
	}
	public void setActionDesciber(String actionDesciber) {
		this.actionDesciber = actionDesciber;
	}
	public String getActionVideoPath() {
		return actionVideoPath;
	}
	public void setActionVideoPath(String actionVideoPath) {
		this.actionVideoPath = actionVideoPath;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	public long getActionTime() {
		return actionTime;
	}
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}
	public String getActionPraiseTime() {
		return actionPraiseTime;
	}
	public void setActionPraiseTime(String actionPraiseTime) {
		this.actionPraiseTime = actionPraiseTime;
	}
	public String getActionCommentTime() {
		return actionCommentTime;
	}
	public void setActionCommentTime(String actionCommentTime) {
		this.actionCommentTime = actionCommentTime;
	}
	public int getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}
	public int getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}
	public String getActionOriginalId() {
		return actionOriginalId;
	}
	public void setActionOriginalId(String actionOriginalId) {
		this.actionOriginalId = actionOriginalId;
	}
	public String getActionLangDesciber() {
		return actionLangDesciber;
	}
	public void setActionLangDesciber(String actionLangDesciber) {
		this.actionLangDesciber = actionLangDesciber;
	}
	public String getActionLangName() {
		return actionLangName;
	}
	public void setActionLangName(String actionLangName) {
		this.actionLangName = actionLangName;
	}
	
}
