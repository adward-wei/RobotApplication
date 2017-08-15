package com.ubtechinc.alpha2ctrlapp.entity;

import java.io.Serializable;

public class SearchRelationInfo implements Serializable {
	
	private static final long serialVersionUID = 6018233364808402553L;
	private String userId;
	private String userName;
	private String userImage;
	private String userEmail;
	private String userPhone;
	private int relationStatus; 
	private String relationId;
	private String equipmentId;
	private int userGender;
	private int upUserId;
	private int equipmentUserId;
	private int controlUserId;
	private String controlUserImage;

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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
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
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public int getUpUserId() {
		return upUserId;
	}
	public void setUpUserId(int upUserId) {
		this.upUserId = upUserId;
	}
	public int getEquipmentUserId() {
		return equipmentUserId;
	}
	public void setEquipmentUserId(int equipmentUserId) {
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
}
