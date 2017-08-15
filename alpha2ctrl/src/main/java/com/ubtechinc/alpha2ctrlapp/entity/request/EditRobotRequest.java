package com.ubtechinc.alpha2ctrlapp.entity.request;

public class EditRobotRequest extends CommonRequest {
 private String userOtherName;
	private String userId;
	private String userImage;
	public String getUserOtherName() {
		return userOtherName;
	}
	public void setUserOtherName(String userOtherName) {
		this.userOtherName = userOtherName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	
}
