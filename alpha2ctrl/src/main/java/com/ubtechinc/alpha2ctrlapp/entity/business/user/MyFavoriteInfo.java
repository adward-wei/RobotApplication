package com.ubtechinc.alpha2ctrlapp.entity.business.user;
/**
 * @ClassName MyFavoriteInfo
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 我的收藏
 * @modifier
 * @modify_time
 */
public class MyFavoriteInfo {
	private int collectId;
	private int collectType;
	private int collectRelationId;
	private String collectDate;
	private String collectName;
	private String collectImage;
	private String collectObjectType;
	private String collectDescriber;
	private String collectUrl;
	private int collectUserId;

	public int getCollectId() {
		return collectId;
	}

	public void setCollectId(int collectId) {
		this.collectId = collectId;
	}

	public int getCollectType() {
		return collectType;
	}

	public void setCollectType(int collectType) {
		this.collectType = collectType;
	}

	public int getCollectRelationId() {
		return collectRelationId;
	}

	public void setCollectRelationId(int collectRelationId) {
		this.collectRelationId = collectRelationId;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public String getCollectName() {
		return collectName;
	}

	public void setCollectName(String collectName) {
		this.collectName = collectName;
	}

	public String getCollectImage() {
		return collectImage;
	}

	public void setCollectImage(String collectImage) {
		this.collectImage = collectImage;
	}

	public String getCollectObjectType() {
		return collectObjectType;
	}

	public void setCollectObjectType(String collectObjectType) {
		this.collectObjectType = collectObjectType;
	}

	public String getCollectDescriber() {
		return collectDescriber;
	}

	public void setCollectDescriber(String collectDescriber) {
		this.collectDescriber = collectDescriber;
	}

	public String getCollectUrl() {
		return collectUrl;
	}

	public void setCollectUrl(String collectUrl) {
		this.collectUrl = collectUrl;
	}

	public int getCollectUserId() {
		return collectUserId;
	}

	public void setCollectUserId(int collectUserId) {
		this.collectUserId = collectUserId;
	}
}
