package com.ubtechinc.alpha2ctrlapp.database;

import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Id;
import com.ubtechinc.framework.db.annotation.Table;

/**
 * @ClassName RobotApp
 * @date 5/22/2017
 * @author tanghongyu
 * @Description 数据库实体，存储机器人端返回的App列表信息
 * @modifier
 * @modify_time
 */
@Table(version = EntityManagerHelper.DB_ACTION_VERSION)
public class ActionEntrityInfo {
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	public int id;
	@Column
	private String actionName;
	//后台存储的动作ID
	@Column
	private String actionId;
	@Column
	private int actionType;
	@Column
	private String actionFilePath="";
	/** 下载状态 **/
	//0表示初始状态，1，表示下载中，2表示下载成功，3表示下载失败，4表示解压成功，5表示解压失败,6表示是系统保护的应用不能删除, 7被客户端占用，8表示存储空间不足
	@Column
	private int downloadState;
	@Column
	private String actionLanName="";
	@Column
	private String actionLanDesciber;

	@Column
	private String language;
	//动作唯一编码
	@Column
	private String actionOriginalId;
	/** 有按钮时间 **/

	@Column
	private String serialNo;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getActionFilePath() {
		return actionFilePath;
	}

	public void setActionFilePath(String actionFilePath) {
		this.actionFilePath = actionFilePath;
	}

	public int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}

	public String getActionLanName() {
		return actionLanName;
	}

	public void setActionLanName(String actionLanName) {
		this.actionLanName = actionLanName;
	}

	public String getActionLanDesciber() {
		return actionLanDesciber;
	}

	public void setActionLanDesciber(String actionLanDesciber) {
		this.actionLanDesciber = actionLanDesciber;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getActionOriginalId() {
		return actionOriginalId;
	}

	public void setActionOriginalId(String actionOriginalId) {
		this.actionOriginalId = actionOriginalId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}
