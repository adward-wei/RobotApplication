package com.ubtechinc.alpha2ctrlapp.entity.request;

public class AcceptAuthorizeRequest extends CommonRequest{

	private String token;
	 private int relationStatus;
	 private String userId;
    	private String  upUserId;
    	private String equipmentId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getRelationStatus() {
		return relationStatus;
	}
	public void setRelationStatus(int relationStatus) {
		this.relationStatus = relationStatus;
	}
//	public String getRelationId() {
//		return relationId;
//	}
//	public void setRelationId(String relationId) {
//		this.relationId = relationId;
//	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	
}