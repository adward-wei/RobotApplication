package com.ubtechinc.alpha2ctrlapp.entity.business.user;
/**
 * @ClassName EditUserInfoRequest
 * @date 7/6/2017
 * @author tanghongyu
 * @Description 编辑用户信息
 * @modifier
 * @modify_time
 */
public class EditUserInfoRequest {
	private String userName;
	private String userImage;
	private String userEmail;
	private String userPhone;
	private int userGender;
	private String countryCode;
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
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
