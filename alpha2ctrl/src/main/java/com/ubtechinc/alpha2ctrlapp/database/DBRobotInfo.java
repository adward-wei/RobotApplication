package com.ubtechinc.alpha2ctrlapp.database;

import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Id;
import com.ubtechinc.framework.db.annotation.Table;

/**
 * @ClassName DBRobotInfo
 * @date 5/23/2017
 * @author tanghongyu
 * @Description 机器人信息数据类
 * @modifier
 * @modify_time
 */
@Table(version = EntityManagerHelper.DB_ROBOT_VERSION)
public class DBRobotInfo {


	@Id(strategy = GenerationType.AUTO_INCREMENT)
	public int id;
	@Column
	private String userId;
	@Column
	private String userName;
	@Column
	private String userImage;
	@Column
	private int relationStatus;
	@Column
	private String relationId;
	@Column
	private String equipmentId;
	//设备所有权用户的ID，如果为0则该设备所有权是自己的。
	@Column
	private String upUserId;
	//设备用户ID
	@Column
	private String equipmentUserId;
	//机器人主人ID
	@Column
	private int controlUserId;
	@Column
	private String controlUserImage;
	@Column
	private String userOtherName;
	@Column
	private String status; //1表示关系正常，0表示关系不正常
	@Column
	private String controlUserName;
	@Column
	private String upUserName;
	@Column
	private String macAddress;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public int getRelationStatus() {
		return relationStatus;
	}

	public void setRelationStatus(int relationStatus) {
		this.relationStatus = relationStatus;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getUpUserId() {
		return upUserId;
	}

	public void setUpUserId(String upUserId) {
		this.upUserId = upUserId;
	}

	public String getEquipmentUserId() {
		return equipmentUserId;
	}

	public void setEquipmentUserId(String equipmentUserId) {
		this.equipmentUserId = equipmentUserId;
	}

	public int getControlUserId() {
		return controlUserId;
	}

	public void setControlUserId(int controlUserId) {
		this.controlUserId = controlUserId;
	}

	public String getControlUserImage() {
		return controlUserImage;
	}

	public void setControlUserImage(String controlUserImage) {
		this.controlUserImage = controlUserImage;
	}

	public String getUserOtherName() {
		return userOtherName;
	}

	public void setUserOtherName(String userOtherName) {
		this.userOtherName = userOtherName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getControlUserName() {
		return controlUserName;
	}

	public void setControlUserName(String controlUserName) {
		this.controlUserName = controlUserName;
	}

	public String getUpUserName() {
		return upUserName;
	}

	public void setUpUserName(String upUserName) {
		this.upUserName = upUserName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
}
