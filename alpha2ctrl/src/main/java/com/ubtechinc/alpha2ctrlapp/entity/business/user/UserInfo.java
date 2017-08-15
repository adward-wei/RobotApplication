package com.ubtechinc.alpha2ctrlapp.entity.business.user;
/**
 * @ClassName UserInfo
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 用户个人信息
 * @modifier
 * @modify_time
 */
public class UserInfo {
	private String userId;
	private String nickName;
	private String userName;
	private String userEmail;
	private String userPhone;
	private String userImage;
	private String userRoleType;
	private int userGender;
	private String token;
	private String countryCode;
	private String userBirthday;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(String userRoleType) {
		this.userRoleType = userRoleType;
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

	public String getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	@Override
	public String toString() {
		return "Result{" +
				"userId='" + userId + '\'' +
				", nickName='" + nickName + '\'' +
				", userName='" + userName + '\'' +
				", userEmail='" + userEmail + '\'' +
				", userPhone='" + userPhone + '\'' +
				", userImage='" + userImage + '\'' +
				", userRoleType='" + userRoleType + '\'' +
				", userGender=" + userGender +
				", token='" + token + '\'' +
				", countryCode='" + countryCode + '\'' +
				", userBirthday='" + userBirthday + '\'' +
				'}';
	}
}



