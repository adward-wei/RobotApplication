package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * [动作库下载]
 * 
 * @author zengdengyi
 * @version 1.0
 *  0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示解压成功，5表示解压失败
 **/
public class ActionFileEntrity {
	private int actionId;
	private int actionType;
	private String actionName;
	private String actionFilePath;
	private int downloadState;
	private String actionOriginalId;
	

	public int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionFilePath() {
		return actionFilePath;
	}

	public void setActionFilePath(String actionFilePath) {
		this.actionFilePath = actionFilePath;
	}

	public String getActionOriginalId() {
		return actionOriginalId;
	}

	public void setActionOriginalId(String actionOriginalId) {
		this.actionOriginalId = actionOriginalId;
	}

	@Override
	public String toString() {
		return "ActionFileEntrity{" +
				"actionId=" + actionId +
				", actionType=" + actionType +
				", actionName='" + actionName + '\'' +
				", actionFilePath='" + actionFilePath + '\'' +
				", downloadState=" + downloadState +
				", actionOriginalId='" + actionOriginalId + '\'' +
				'}';
	}
}
