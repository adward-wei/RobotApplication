package com.ubtechinc.alpha2ctrlapp.entity.business.shop;

import java.io.Serializable;
/**
 * @ClassName ActionInfo
 * @date 5/15/2017
 * @author tanghongyu
 * @Description 机器人动作
 * @modifier
 * @modify_time
 */
public class ActionInfo implements Serializable{
	
	private static final long serialVersionUID = 2120064319896337425L;
	//动作id
	private int actionId;
	//	动作名称
	private String actionName;
	//动作标题
	private String actionTitle;
	//动作路径
	private String actionPath;
	//动作图片路径
	private String actionImagePath;
	//动作描述
	private String actionDesciber;
   //动作视频路径
	private String actionVideoPath;
	private int actionType;
	private String actionDate;
	private long actionTime;
	//动作点赞次数
	private String actionPraiseTime;
	//动作评论次数
	private String actionCommentTime;
	//动作原始id（全局唯一）
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
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
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
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
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
	public long getActionTime() {
		return actionTime;
	}
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
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
