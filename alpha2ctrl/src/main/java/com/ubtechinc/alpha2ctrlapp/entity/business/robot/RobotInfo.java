package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

import android.os.Parcel;
import android.os.Parcelable;

import com.ubtech.utilcode.utils.StringUtils;

import java.io.Serializable;

/*************************
* @date 2016/7/4
* @author
* @Description 机器人详细信息
* @modify
* @modify_time
**************************/
public class RobotInfo implements Serializable, Parcelable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7461680897583115773L;
	private String userId;
	private String userName;
	private String userImage;
	private String userEmail;
	private String userPhone;
	private int relationStatus;
	private String relationId;
	private String equipmentId;
	private int userGender;
	//设备所有权用户的ID，如果为0则该设备所有权是自己的。
	private int upUserId;
	//设备用户ID
	private int equipmentUserId;
	//机器人主人ID
	private int controlUserId;
	private String controlUserImage;
	//机器人的昵称
	private String userOtherName;
	private String status; //1表示关系正常，0表示关系不正常
	private long relationDate;
	private String controlUserName;
	private String upUserName;
	private String macAddress;
	private int connectionState;

	public int getConnectionState() {
		return connectionState;
	}

	public void setConnectionState(int connectionState) {
		this.connectionState = connectionState;
	}

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
	public int getUpUserId() {
		return upUserId;
	}
	public void setUpUserId(int upUserId) {
		this.upUserId = upUserId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
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

	public long getRelationDate() {
		return relationDate;
	}

	public void setRelationDate(long relationDate) {
		this.relationDate = relationDate;
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

	@Override
	public int describeContents() {
		return 0;
	}



	//注意写入变量和读取变量的顺序应该一致 不然得不到正确的结果
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userId);
		dest.writeString(userName);
		dest.writeString(userImage);
		dest.writeString(userEmail);
		dest.writeString(userPhone);
		dest.writeInt(relationStatus);
		dest.writeString(relationId);
		dest.writeString(equipmentId);
		dest.writeInt(userGender);
		dest.writeInt(upUserId);
		dest.writeInt(equipmentUserId);
		dest.writeInt(controlUserId);
		dest.writeString(controlUserImage);
		dest.writeString(userOtherName);
		dest.writeString(status);
		dest.writeLong(relationDate);
		dest.writeString(controlUserName);
		dest.writeString(upUserName);
		dest.writeString(macAddress);

	}
	//注意读取变量和写入变量的顺序应该一致 不然得不到正确的结果
	public void readFromParcel(Parcel source) {

		userId = source.readString();
		userName = source.readString();
		userImage = source.readString();
		userEmail = source.readString();
		userPhone = source.readString();
		relationStatus = source.readInt();
		relationId = source.readString();
		equipmentId = source.readString();
		userGender = source.readInt();
		upUserId = source.readInt();
		equipmentUserId = source.readInt();
		controlUserId = source.readInt();
		controlUserImage = source.readString();
		userOtherName = source.readString();
		status = source.readString();
		relationDate = source.readLong();
		controlUserName = source.readString();
		upUserName = source.readString();
		macAddress = source.readString();


	}

	public RobotInfo() {

	}

	private RobotInfo(Parcel source) {
		readFromParcel(source);
	}

	//必须提供一个名为CREATOR的static final属性 该属性需要实现android.os.Parcelable.Creator<T>接口
	public static final Creator<RobotInfo> CREATOR = new Creator<RobotInfo>() {

		@Override
		public RobotInfo createFromParcel(Parcel source) {
			return new RobotInfo(source);
		}

		@Override
		public RobotInfo[] newArray(int size) {
			return new RobotInfo[size];
		}
	};



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RobotInfo that = (RobotInfo) o;

		return StringUtils.isEquals(equipmentId, that.equipmentId);

	}

	@Override
	public int hashCode() {
		int result = equipmentId != null ? equipmentId.hashCode() : 0;
		result = 31 * result + equipmentUserId;
		return result;
	}
}
