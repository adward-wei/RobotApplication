package com.ubtechinc.alpha2ctrlapp.entity.request;

public class DeleRequest extends CommonRequest {
	private String relationId;
	private String token;
	private String userId;
	private String equipmentUserId;
	private String equipmentId;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

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

	public String getEquipmentUserId() {
		return equipmentUserId;
	}

	public void setEquipmentUserId(String equipmentUserId) {
		this.equipmentUserId = equipmentUserId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

}
